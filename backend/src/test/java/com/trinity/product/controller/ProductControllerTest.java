package com.trinity.product.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.UUID;

import com.trinity.product.dto.api.ProductResponse;
import com.trinity.product.exception.ProductNotFoundException;
import com.trinity.product.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private String barcode;
    private UUID productId;
    private ProductResponse readProductDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        barcode = "1234567890123";
        productId = UUID.randomUUID();

        ProductResponse.StockDto stockDto = new ProductResponse.StockDto();
        stockDto.setQuantity(10);
        stockDto.setMinThreshold(5);
        stockDto.setMaxThreshold(100);

        readProductDTO = new ProductResponse();
        readProductDTO.setId(productId);
        readProductDTO.setBarcode(barcode);
        readProductDTO.setName("Test Product");
        readProductDTO.setBrand("Test Brand");
        readProductDTO.setPrice(new BigDecimal("9.99"));
        readProductDTO.setStock(stockDto);
    }

    @Test
    void scanProduct_ValidBarcode_ReturnsProduct() {
        // Given
        when(productService.scanProduct(barcode)).thenReturn(readProductDTO);

        // When
        ResponseEntity<ProductResponse> response = productController.scanProduct(barcode);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(readProductDTO, response.getBody());
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