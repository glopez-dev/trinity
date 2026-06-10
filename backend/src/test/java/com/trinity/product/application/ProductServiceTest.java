package com.trinity.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.trinity.product.application.command.UpdateProductCommand;
import com.trinity.product.domain.exception.InvalidProductDataException;
import com.trinity.product.domain.exception.ProductNotFoundException;
import com.trinity.product.domain.model.Product;
import com.trinity.product.domain.port.ProductCatalogGateway;
import com.trinity.product.domain.port.ProductRepositoryPort;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepositoryPort productRepository;

    @Mock
    private ProductCatalogGateway productCatalogGateway;

    @InjectMocks
    private ProductService productService;

    private UUID productId;
    private Product product;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        product = Product.builder()
                .id(productId)
                .barcode("1234567890123")
                .name("Test Product")
                .brand("Test Brand")
                .price(new BigDecimal("9.99"))
                .stock(Product.Stock.builder().quantity(10).minThreshold(5).maxThreshold(100).build())
                .build();
    }

    @Test
    void createProduct_ValidProduct_SavesAndReturns() {
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.createProduct(product);

        assertThat(result).isEqualTo(product);
        verify(productRepository).save(product);
    }

    @Test
    void createProduct_NegativePrice_ThrowsInvalidProductDataException() {
        product.setPrice(new BigDecimal("-1"));

        assertThatExceptionOfType(InvalidProductDataException.class)
                .isThrownBy(() -> productService.createProduct(product))
                .withMessageContaining("Price cannot be negative");
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void createProduct_NegativeStockQuantity_ThrowsInvalidProductDataException() {
        product.setStock(Product.Stock.builder().quantity(-1).build());

        assertThatExceptionOfType(InvalidProductDataException.class)
                .isThrownBy(() -> productService.createProduct(product))
                .withMessageContaining("Initial stock quantity cannot be negative");
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void getAllProducts_ReturnsAll() {
        when(productRepository.findAll()).thenReturn(List.of(product, new Product()));

        List<Product> result = productService.getAllProducts();

        assertThat(result).hasSize(2);
        verify(productRepository).findAll();
    }

    @Test
    void getProduct_Existing_ReturnsProduct() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product result = productService.getProduct(productId);

        assertThat(result).isEqualTo(product);
        verify(productRepository).findById(productId);
    }

    @Test
    void getProduct_Unknown_ThrowsProductNotFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productService.getProduct(productId))
                .withMessageContaining("Product not found with ID:");
    }

    @Test
    void updateProduct_AllFieldsPresent_AppliesAndSaves() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateProductCommand command = new UpdateProductCommand(
                Optional.of("New Name"),
                Optional.of(new BigDecimal("12.50")),
                Optional.of(20));

        Product result = productService.updateProduct(productId, command);

        assertThat(result.getName()).isEqualTo("New Name");
        assertThat(result.getPrice()).isEqualTo(new BigDecimal("12.50"));
        // adjustStock applies a positive delta: 10 (initial) + 20 = 30
        assertThat(result.getStock().getQuantity()).isEqualTo(30);
        verify(productRepository).save(product);
    }

    @Test
    void updateProduct_EmptyCommand_LeavesUnchangedButSaves() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateProductCommand command =
                new UpdateProductCommand(Optional.empty(), Optional.empty(), Optional.empty());

        Product result = productService.updateProduct(productId, command);

        assertThat(result.getName()).isEqualTo("Test Product");
        assertThat(result.getPrice()).isEqualTo(new BigDecimal("9.99"));
        assertThat(result.getStock().getQuantity()).isEqualTo(10);
        verify(productRepository).save(product);
    }

    @Test
    void updateProduct_Unknown_ThrowsProductNotFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        UpdateProductCommand command =
                new UpdateProductCommand(Optional.empty(), Optional.empty(), Optional.empty());

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productService.updateProduct(productId, command));
    }

    @Test
    void deleteProduct_Existing_DeletesProduct() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.deleteProduct(productId);

        verify(productRepository).delete(product);
    }

    @Test
    void deleteProduct_Unknown_ThrowsProductNotFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productService.deleteProduct(productId));
    }

    @Test
    void searchProducts_DelegatesToGateway() {
        when(productCatalogGateway.search("coke")).thenReturn(List.of(product));

        List<Product> result = productService.searchProducts("coke");

        assertThat(result).hasSize(1);
        verify(productCatalogGateway).search("coke");
        verifyNoInteractions(productRepository);
    }
}
