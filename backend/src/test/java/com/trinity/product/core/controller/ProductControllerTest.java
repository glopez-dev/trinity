package com.trinity.product.core.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.trinity.product.core.dto.SearchRequest;
import com.trinity.product.core.dto.TrinitySearchResponse;
import com.trinity.product.core.service.OpenFoodFactsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;


class ProductControllerTest {

    @Mock
    private OpenFoodFactsService openFoodFactsService;

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
        SearchRequest request = new SearchRequest();
        request.setSearchTerm(searchTerm);
        TrinitySearchResponse expectedResponse = new TrinitySearchResponse();

        when(openFoodFactsService.searchProducts(searchTerm)).thenReturn(expectedResponse);

        // When
        ResponseEntity<TrinitySearchResponse> responseEntity = productController.searchProducts(request);

        // Then
        assertEquals(ResponseEntity.ok(expectedResponse), responseEntity);
        verify(openFoodFactsService, times(1)).searchProducts(searchTerm);
    }

    @Test
    void testSearchProducts_Failure() {
        // Given
        String searchTerm = "test";
        SearchRequest request = new SearchRequest();
        request.setSearchTerm(searchTerm);

        when(openFoodFactsService.searchProducts(searchTerm)).thenThrow(new RuntimeException("API error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> productController.searchProducts(request));
        verify(openFoodFactsService, times(1)).searchProducts(searchTerm);
    }
}