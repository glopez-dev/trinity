package com.trinity.product.interfaces.rest.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.UUID;

import com.trinity.product.domain.model.Product;
import com.trinity.product.interfaces.rest.dto.ProductResponse;
import com.trinity.product.interfaces.rest.mapper.ProductApiMapper;
import com.trinity.product.domain.exception.ProductNotFoundException;
import com.trinity.product.application.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Spy
    private ProductApiMapper productApiMapper = Mappers.getMapper(ProductApiMapper.class);

    @InjectMocks
    private ProductController productController;

    private String barcode;
    private UUID productId;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        barcode = "1234567890123";
        productId = UUID.randomUUID();

        product = Product.builder()
                .id(productId)
                .barcode(barcode)
                .name("Test Product")
                .brand("Test Brand")
                .price(new BigDecimal("9.99"))
                .stock(Product.Stock.builder().quantity(10).minThreshold(5).maxThreshold(100).build())
                .build();
    }

    @Test
    void scanProduct_ValidBarcode_ReturnsProduct() {
        // Given
        when(productService.scanProduct(barcode)).thenReturn(product);

        // When
        ResponseEntity<ProductResponse> response = productController.scanProduct(barcode);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(productId, response.getBody().getId());
        assertEquals(barcode, response.getBody().getBarcode());
        assertEquals(new BigDecimal("9.99"), response.getBody().getPrice());
        assertEquals(10, response.getBody().getStock().getQuantity());
        verify(productService).scanProduct(barcode);
    }

    @Test
    void scanProduct_ProductNotFound_ThrowsException() {
        // Given
        when(productService.scanProduct(barcode)).thenThrow(new ProductNotFoundException("Product not found"));

        // When & Then
        assertThrows(ProductNotFoundException.class, () -> {
            productController.scanProduct(barcode);
        });

        verify(productService).scanProduct(barcode);
    }

    @Test
    void scanProduct_InvalidBarcode_ThrowsException() {
        // Given
        String invalidBarcode = "123"; // Too short

        // When & Then
        assertThrows(ResponseStatusException.class, () -> {
            productController.scanProduct(invalidBarcode);
        });

        verifyNoInteractions(productService);
    }

    @Test
    void scanProduct_NonNumericBarcode_ThrowsException() {
        // Given
        String invalidBarcode = "123abc456789";

        // When & Then
        assertThrows(ResponseStatusException.class, () -> {
            productController.scanProduct(invalidBarcode);
        });

        verifyNoInteractions(productService);
    }
}