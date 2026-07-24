package com.trinity.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.trinity.common.domain.exception.BusinessRuleViolation;
import com.trinity.product.domain.model.Product;
import com.trinity.product.domain.port.ProductCatalogGateway;
import com.trinity.product.domain.port.ProductRepositoryPort;
import com.trinity.product.domain.exception.ProductNotFoundException;

@ExtendWith(MockitoExtension.class)
class ProductServiceStockTest {

    @Mock
    private ProductRepositoryPort productRepository;

    @Mock
    private ProductCatalogGateway productCatalogGateway;

    @InjectMocks
    private ProductService productService;

    private Product productWithStock(int quantity) {
        return Product.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .price(new BigDecimal("9.99"))
                .stock(Product.Stock.builder().quantity(quantity).minThreshold(5).maxThreshold(100).build())
                .build();
    }

    @Test
    void deductStock_decrementsAndSaves() {
        Product product = productWithStock(10);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product result = productService.deductStock(product.getId(), 4);

        assertThat(result.getStock().getQuantity()).isEqualTo(6);
        verify(productRepository).save(product);
    }

    @Test
    void deductStock_unknownProduct_throwsNotFound() {
        UUID productId = UUID.randomUUID();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productService.deductStock(productId, 1));
    }

    @Test
    void deductStock_insufficientStock_throwsBusinessRuleViolation() {
        Product product = productWithStock(2);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        assertThatExceptionOfType(BusinessRuleViolation.class)
                .isThrownBy(() -> productService.deductStock(product.getId(), 5));

        verify(productRepository, never()).save(any(Product.class));
    }
}
