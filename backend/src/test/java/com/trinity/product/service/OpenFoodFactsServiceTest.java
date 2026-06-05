package com.trinity.product.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;

import com.trinity.product.adapter.OpenFoodFactsAdapter;
import com.trinity.product.dto.api.ReadProductDTO;
import com.trinity.product.dto.open_food_facts.OpenFoodFactSearchResponse;
import com.trinity.product.dto.open_food_facts.OpenFoodFactsProduct;
import com.trinity.product.interfaces.rest.mapper.ProductApiMapper;
import com.trinity.product.model.Product;

import reactor.core.publisher.Mono;

class OpenFoodFactsServiceBarcodeTest {

    @Mock
    private WebClient webClient;

    @Mock
    private ProductApiMapper productMapper;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private OpenFoodFactsService openFoodFactsService;

    private String barcode;
    private OpenFoodFactSearchResponse openFoodFactsResponse;
    private Product product;
    private ReadProductDTO readProductDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        barcode = "3017620422003";

        // Set up the OpenFoodFactsProduct
        OpenFoodFactsProduct openFoodFactsProduct = new OpenFoodFactsProduct();
        openFoodFactsProduct.setCode(barcode);
        openFoodFactsProduct.setBrands("Ferrero");
        openFoodFactsProduct.setGenericNameFr("Nutella");

        // Set up the OpenFoodFactSearchResponse
        openFoodFactsResponse = new OpenFoodFactSearchResponse();
        openFoodFactsResponse.setProducts(Collections.singletonList(openFoodFactsProduct));

        // Set up the Product
        product = Product.builder()
                .barcode(barcode)
                .brand("Ferrero")
                .name("Nutella")
                .build();

        // Set up the ReadProductDTO
        readProductDTO = new ReadProductDTO();
        readProductDTO.setBarcode(barcode);
        readProductDTO.setBrand("Ferrero");
        readProductDTO.setName("Nutella");
    }

    @SuppressWarnings("unchecked")
    @Test
    void getProductByBarcode_ValidBarcode_ReturnsProduct() {
        // Set up the WebClient mock chain
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OpenFoodFactSearchResponse.class)).thenReturn(Mono.just(openFoodFactsResponse));

        // Mock OpenFoodFactsAdapter
        try (var mockedStatic = mockStatic(OpenFoodFactsAdapter.class)) {
            mockedStatic.when(() -> OpenFoodFactsAdapter.adapt(any()))
                    .thenReturn(Collections.singletonList(product));

            when(productMapper.toDTO(product)).thenReturn(readProductDTO);

            // When
            ReadProductDTO result = openFoodFactsService.getProductByBarcode(barcode);

            // Then
            assertNotNull(result);
            assertEquals(barcode, result.getBarcode());
            assertEquals("Ferrero", result.getBrand());
            assertEquals("Nutella", result.getName());

            // Verify URI
            ArgumentCaptor<URI> uriCaptor = ArgumentCaptor.forClass(URI.class);
            verify(requestHeadersUriSpec).uri(uriCaptor.capture());
            String capturedUri = uriCaptor.getValue().toString();
            assertTrue(capturedUri.contains(barcode));
            assertTrue(capturedUri.contains("world.openfoodfacts.org/api/v0/product/"));
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    void getProductByBarcode_ProductNotFound_ReturnsNull() {
        // Set up the WebClient mock chain
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        // Create a response with no products
        OpenFoodFactSearchResponse emptyResponse = new OpenFoodFactSearchResponse();
        emptyResponse.setProducts(Collections.emptyList());
        when(responseSpec.bodyToMono(OpenFoodFactSearchResponse.class)).thenReturn(Mono.just(emptyResponse));

        // When
        ReadProductDTO result = openFoodFactsService.getProductByBarcode(barcode);

        // Then
        assertNull(result);
    }

    @SuppressWarnings("unchecked")
    @Test
    void getProductByBarcode_ApiError_ReturnsNull() {
        // Set up the WebClient mock chain to throw an exception
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OpenFoodFactSearchResponse.class))
                .thenReturn(Mono.error(new RuntimeException("API Error")));

        // When
        ReadProductDTO result = openFoodFactsService.getProductByBarcode(barcode);

        // Then
        assertNull(result);
    }

    @SuppressWarnings("unchecked")
    @Test
    void getProductByBarcode_NullResponse_ReturnsNull() {

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OpenFoodFactSearchResponse.class)).thenReturn(Mono.empty());

        // When
        ReadProductDTO result = openFoodFactsService.getProductByBarcode(barcode);

        // Then
        assertNull(result);
    }
}