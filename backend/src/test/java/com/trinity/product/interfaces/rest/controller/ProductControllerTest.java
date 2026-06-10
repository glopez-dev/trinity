package com.trinity.product.interfaces.rest.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.trinity.product.domain.model.Product;
import com.trinity.product.application.command.UpdateProductCommand;
import com.trinity.product.interfaces.rest.dto.CreateProductRequest;
import com.trinity.product.interfaces.rest.dto.ProductResponse;
import com.trinity.product.interfaces.rest.dto.SearchProductRequest;
import com.trinity.product.interfaces.rest.dto.UpdateProductRequest;
import com.trinity.product.interfaces.rest.mapper.ProductApiMapper;
import com.trinity.product.domain.exception.ProductNotFoundException;
import com.trinity.product.application.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
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

    @Test
    void searchProducts_ReturnsMappedProductList() {
        SearchProductRequest request = new SearchProductRequest("coke");
        when(productService.searchProducts("coke")).thenReturn(List.of(product));

        ResponseEntity<List<ProductResponse>> response = productController.searchProducts(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(productId, response.getBody().get(0).getId());
        verify(productService).searchProducts("coke");
    }

    @Test
    void searchProducts_EmptyResult_ReturnsEmptyList() {
        SearchProductRequest request = new SearchProductRequest("unknown");
        when(productService.searchProducts("unknown")).thenReturn(Collections.emptyList());

        ResponseEntity<List<ProductResponse>> response = productController.searchProducts(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(productService).searchProducts("unknown");
    }

    @Test
    void createProduct_ReturnsMappedProduct() {
        CreateProductRequest request = new CreateProductRequest();
        request.setBarcode(barcode);
        request.setBrand("Test Brand");
        request.setName("Test Product");
        request.setPrice(new BigDecimal("9.99"));
        CreateProductRequest.StockDto stock = new CreateProductRequest.StockDto();
        stock.setQuantity(10);
        stock.setMinThreshold(5);
        stock.setMaxThreshold(100);
        request.setStock(stock);
        when(productService.createProduct(any(Product.class))).thenReturn(product);

        ResponseEntity<ProductResponse> response = productController.createProduct(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(productId, response.getBody().getId());
        verify(productService).createProduct(any(Product.class));
    }

    @Test
    void getAllProducts_ReturnsMappedList() {
        when(productService.getAllProducts()).thenReturn(List.of(product));

        ResponseEntity<List<ProductResponse>> response = productController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(productService).getAllProducts();
    }

    @Test
    void getProduct_ReturnsMappedProduct() {
        when(productService.getProduct(productId)).thenReturn(product);

        ResponseEntity<ProductResponse> response = productController.getProduct(productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(productId, response.getBody().getId());
        verify(productService).getProduct(productId);
    }

    @Test
    void updateProduct_BuildsCommandAndReturnsMappedProduct() {
        UpdateProductRequest request = new UpdateProductRequest();
        request.setName(Optional.of("New Name"));
        request.setPrice(Optional.of(new BigDecimal("12.50")));
        request.setQuantity(Optional.of(20));
        when(productService.updateProduct(eq(productId), any(UpdateProductCommand.class)))
                .thenReturn(product);

        ResponseEntity<ProductResponse> response = productController.updateProduct(productId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        ArgumentCaptor<UpdateProductCommand> captor = ArgumentCaptor.forClass(UpdateProductCommand.class);
        verify(productService).updateProduct(eq(productId), captor.capture());
        UpdateProductCommand command = captor.getValue();
        assertEquals("New Name", command.name().orElseThrow());
        assertEquals(new BigDecimal("12.50"), command.price().orElseThrow());
        assertEquals(20, command.quantity().orElseThrow());
    }

    @Test
    void deleteProduct_ReturnsNoContent() {
        doNothing().when(productService).deleteProduct(productId);

        ResponseEntity<Void> response = productController.deleteProduct(productId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(productService).deleteProduct(productId);
    }
}