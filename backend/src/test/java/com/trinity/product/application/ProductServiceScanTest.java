package com.trinity.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.trinity.product.domain.model.Product;
import com.trinity.product.domain.port.ProductCatalogGateway;
import com.trinity.product.domain.port.ProductRepositoryPort;
import com.trinity.product.domain.exception.ProductNotFoundException;

@ExtendWith(MockitoExtension.class)
class ProductServiceScanTest {

    @Mock
    private ProductRepositoryPort productRepository;

    @Mock
    private ProductCatalogGateway productCatalogGateway;

    @InjectMocks
    private ProductService productService;

    private String barcode;
    private UUID productId;
    private Product product;

    @BeforeEach
    void setUp() {
        barcode = "1234567890123";
        productId = UUID.randomUUID();

        Product.Stock stock = Product.Stock.builder()
                .quantity(10)
                .minThreshold(5)
                .maxThreshold(100)
                .build();

        product = Product.builder()
                .id(productId)
                .barcode(barcode)
                .name("Test Product")
                .brand("Test Brand")
                .price(new BigDecimal("9.99"))
                .stock(stock)
                .build();
    }

    @Test
    void scanProduct_WhenProductFoundInDatabase_ShouldReturnIt() {
        // GIVEN
        when(productRepository.findByBarcode(barcode)).thenReturn(Optional.of(product));

        // WHEN
        Product result = productService.scanProduct(barcode);

        // THEN
        verify(productRepository).findByBarcode(barcode);
        verifyNoInteractions(productCatalogGateway);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productId);
        assertThat(result.getBarcode()).isEqualTo(barcode);
    }

    @Test
    void scanProduct_WhenProductNotInDatabaseButInOpenFoodFacts_ShouldCreateAndReturnIt() {
        // GIVEN
        when(productRepository.findByBarcode(barcode)).thenReturn(Optional.empty());
        when(productCatalogGateway.findByBarcode(barcode)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        // WHEN
        Product result = productService.scanProduct(barcode);

        // THEN
        verify(productRepository).findByBarcode(barcode);
        verify(productCatalogGateway).findByBarcode(barcode);
        verify(productRepository).save(product);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productId);
        assertThat(result.getBarcode()).isEqualTo(barcode);
    }

    @Test
    void scanProduct_WhenProductNotFoundAnywhere_ShouldThrowException() {
        // GIVEN
        when(productRepository.findByBarcode(barcode)).thenReturn(Optional.empty());
        when(productCatalogGateway.findByBarcode(barcode)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productService.scanProduct(barcode))
                .withMessageContaining("Product with barcode " + barcode + " not found");

        verify(productRepository).findByBarcode(barcode);
        verify(productCatalogGateway).findByBarcode(barcode);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void scanProduct_WhenProductFromOpenFoodFactsHasNullPrice_ShouldSetDefaultPrice() {
        // GIVEN
        Product catalogProduct = Product.builder()
                .barcode(barcode)
                .name("OpenFoodFacts Product")
                .brand("OpenFoodFacts Brand")
                .build();

        when(productRepository.findByBarcode(barcode)).thenReturn(Optional.empty());
        when(productCatalogGateway.findByBarcode(barcode)).thenReturn(Optional.of(catalogProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        // WHEN
        Product result = productService.scanProduct(barcode);

        // THEN
        verify(productRepository).save(any(Product.class));
        assertThat(result.getPrice()).isEqualTo(new BigDecimal("0.00"));
    }

    @Test
    void scanProduct_WhenProductFromOpenFoodFactsHasNullStock_ShouldSetDefaultStock() {
        // GIVEN
        Product catalogProduct = Product.builder()
                .barcode(barcode)
                .name("OpenFoodFacts Product")
                .brand("OpenFoodFacts Brand")
                .price(new BigDecimal("5.99"))
                .build();

        when(productRepository.findByBarcode(barcode)).thenReturn(Optional.empty());
        when(productCatalogGateway.findByBarcode(barcode)).thenReturn(Optional.of(catalogProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        // WHEN
        Product result = productService.scanProduct(barcode);

        // THEN
        verify(productRepository).save(any(Product.class));
        assertThat(result.getStock()).isNotNull();
        assertThat(result.getStock().getQuantity()).isZero();
        assertThat(result.getStock().getMinThreshold()).isEqualTo(5);
        assertThat(result.getStock().getMaxThreshold()).isEqualTo(100);
        assertThat(result.getPrice()).isEqualTo(new BigDecimal("5.99"));
    }
}
