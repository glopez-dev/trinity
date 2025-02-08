package com.trinity.product.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.trinity.product.dto.api.CreateProductDTO;
import com.trinity.product.dto.api.ReadProductDTO;
import com.trinity.product.dto.api.SearchProductDTO;
import com.trinity.product.service.OpenFoodFactsService;
import com.trinity.product.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class ProductControllerTest {

    @Mock
    private OpenFoodFactsService openFoodFactsService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchProducts_Success() {
        // Given
        String searchTerm = "test";
        SearchProductDTO request = new SearchProductDTO();
        request.setSearchTerm(searchTerm);
        List<ReadProductDTO> expectedResponse = new ArrayList<>();
        expectedResponse.add(new ReadProductDTO());

        when(openFoodFactsService.searchProducts(searchTerm)).thenReturn(expectedResponse);

        // When
        ResponseEntity<List<ReadProductDTO>> responseEntity = productController.searchProducts(request);

        // Then
        assertEquals(ResponseEntity.ok(expectedResponse), responseEntity);
        verify(openFoodFactsService, times(1)).searchProducts(searchTerm);
    }

    @Test
    void testSearchProducts_Failure() {
        // Given
        String searchTerm = "test";
        SearchProductDTO request = new SearchProductDTO();
        request.setSearchTerm(searchTerm);

        when(openFoodFactsService.searchProducts(searchTerm)).thenThrow(new RuntimeException("API error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> productController.searchProducts(request));
        verify(openFoodFactsService, times(1)).searchProducts(searchTerm);
    }

    @Test 
    void testCreateProduct_Success() {
        // Given
        CreateProductDTO dto = new CreateProductDTO();
        dto.setBarcode("1234567890");
        dto.setBrand("brand");
        dto.setName("name");
        dto.setPrice(new BigDecimal("1.23"));

        ReadProductDTO expectedProduct = new ReadProductDTO();
        expectedProduct.setBarcode("1234567890");
        expectedProduct.setBrand("brand");
        expectedProduct.setName("name");
        expectedProduct.setPrice(new BigDecimal("1.23"));

        when(productService.createProduct(dto)).thenReturn(expectedProduct);

        // When 
        ResponseEntity<ReadProductDTO> responseEntity = productController.createProduct(dto);

        // Then
        assertEquals(ResponseEntity.ok(expectedProduct), responseEntity);
        verify(productService, times(1)).createProduct(dto);
    }

    @Test
    void testCreateProduct_Failure() {
        // Given
        CreateProductDTO dto = new CreateProductDTO();
        dto.setBarcode("1234567890");
        dto.setBrand("brand");
        dto.setName("name");
        dto.setPrice(new BigDecimal("1.23"));

        when(productService.createProduct(dto)).thenThrow(new RuntimeException("Creation error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> productController.createProduct(dto));
        verify(productService, times(1)).createProduct(dto);
    }
}