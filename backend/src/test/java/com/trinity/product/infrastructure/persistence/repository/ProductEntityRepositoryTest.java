package com.trinity.product.infrastructure.persistence.repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import com.trinity.product.infrastructure.persistence.entity.ProductEntity;
import com.trinity.product.infrastructure.persistence.entity.ProductImageUrlEmbeddable;

/**
 * Boots Hibernate against the frozen V1 schema (ddl-auto=validate). Any drift in
 * {@link ProductEntity}'s mapping fails the @DataJpaTest at startup, so this is
 * the schema-identity gate for the product dedoublement.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ProductEntityRepositoryTest {

    @Autowired
    private JpaProductRepository productRepository;

    private ProductEntity product;
    private UUID productId;

    @BeforeEach
    void setUp() {
        product = ProductEntity.builder()
            .barcode("123456789")
            .name("Test Product")
            .brand("Test Brand")
            .price(new BigDecimal("9.99"))
            .category("Test Category")
            .nutriscoreGrade("a")
            .build();

        product = productRepository.save(product);
        productId = product.getId();
    }

    @Test
    void findById_ShouldReturnProduct_WhenProductExists() {
        // When
        Optional<ProductEntity> foundProduct = productRepository.findById(productId);

        // Then
        assertThat(foundProduct)
            .isPresent()
            .get()
            .satisfies(p -> {
                assertThat(p.getId()).isEqualTo(productId);
                assertThat(p.getBarcode()).isEqualTo("123456789");
                assertThat(p.getName()).isEqualTo("Test Product");
                assertThat(p.getBrand()).isEqualTo("Test Brand");
                assertThat(p.getPrice()).isEqualTo(new BigDecimal("9.99"));
                assertThat(p.getCategory()).isEqualTo("Test Category");
                assertThat(p.getNutriscoreGrade()).isEqualTo("a");
            });
    }

    @Test
    void findById_ShouldReturnEmpty_WhenProductDoesNotExist() {
        // Given
        UUID nonExistentId = UUID.randomUUID();

        // When
        Optional<ProductEntity> foundProduct = productRepository.findById(nonExistentId);

        // Then
        assertThat(foundProduct).isEmpty();
    }

    @Test
    void save_ShouldPersistProduct() {
        // Given
        ProductEntity newProduct = ProductEntity.builder()
            .barcode("987654321")
            .name("New Product")
            .brand("New Brand")
            .price(new BigDecimal("19.99"))
            .category("New Category")
            .nutriscoreGrade("b")
            .build();

        // When
        ProductEntity savedProduct = productRepository.save(newProduct);

        // Then
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(productRepository.findById(savedProduct.getId()))
            .isPresent()
            .get()
            .satisfies(p -> {
                assertThat(p.getBarcode()).isEqualTo("987654321");
                assertThat(p.getName()).isEqualTo("New Product");
                assertThat(p.getBrand()).isEqualTo("New Brand");
                assertThat(p.getPrice()).isEqualTo(new BigDecimal("19.99"));
                assertThat(p.getCategory()).isEqualTo("New Category");
                assertThat(p.getNutriscoreGrade()).isEqualTo("b");
            });
    }

    @Test
    void delete_ShouldRemoveProduct() {
        // When
        productRepository.delete(product);

        // Then
        assertThat(productRepository.findById(productId)).isEmpty();
    }

    @Test
    void findByBarcode_ShouldReturnProduct_WhenProductExists() {
        // When
        Optional<ProductEntity> foundProduct = productRepository.findByBarcode("123456789");

        // Then
        assertThat(foundProduct)
                .isPresent()
                .get()
                .satisfies(p -> {
                    assertThat(p.getId()).isEqualTo(productId);
                    assertThat(p.getBarcode()).isEqualTo("123456789");
                    assertThat(p.getName()).isEqualTo("Test Product");
                    assertThat(p.getBrand()).isEqualTo("Test Brand");
                });
    }

    @Test
    void findByBarcode_ShouldReturnEmpty_WhenProductDoesNotExist() {
        // Given
        String nonExistentBarcode = "000000000";

        // When
        Optional<ProductEntity> foundProduct = productRepository.findByBarcode(nonExistentBarcode);

        // Then
        assertThat(foundProduct).isEmpty();
    }

    @Test
    void save_ShouldPersistSelectedImages_inDistinctColumns() {
        // Given a product with per-size, per-locale image URLs
        ProductEntity withImages = ProductEntity.builder()
            .barcode("555000111")
            .name("Imaged Product")
            .price(new BigDecimal("4.50"))
            .selectedImages(ProductEntity.SelectedImages.builder()
                .display(ProductImageUrlEmbeddable.builder().en("display-en.jpg").fr("display-fr.jpg").build())
                .small(ProductImageUrlEmbeddable.builder().en("small-en.jpg").fr("small-fr.jpg").build())
                .thumb(ProductImageUrlEmbeddable.builder().en("thumb-en.jpg").fr("thumb-fr.jpg").build())
                .build())
            .build();

        // When
        UUID id = productRepository.save(withImages).getId();
        productRepository.flush();

        // Then — images must round-trip (regression: they were never persisted before)
        assertThat(productRepository.findById(id))
            .isPresent()
            .get()
            .satisfies(p -> {
                assertThat(p.getSelectedImages().getDisplay().getEn()).isEqualTo("display-en.jpg");
                assertThat(p.getSelectedImages().getDisplay().getFr()).isEqualTo("display-fr.jpg");
                assertThat(p.getSelectedImages().getSmall().getEn()).isEqualTo("small-en.jpg");
                assertThat(p.getSelectedImages().getThumb().getFr()).isEqualTo("thumb-fr.jpg");
            });
    }
}
