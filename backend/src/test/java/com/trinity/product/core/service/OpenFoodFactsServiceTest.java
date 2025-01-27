package com.trinity.product.core.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import com.trinity.product.core.dto.open_food_facts.OpenFoodFactSearchResponse;
import com.trinity.product.core.exception.ApiException;
import reactor.core.publisher.Mono;

class OpenFoodFactsServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private OpenFoodFactsService openFoodFactsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSearchResponseJson_Success() {
        // Given
        URI uri = URI.create("https://world.openfoodfacts.org/cgi/search.pl?search_terms=test&fields=products,allergens_imported,allergens,code,brands,brand_imported,compared_to_category,grade,ingredients_text_fr,nutrient_levels,nutriments,product_name_fr_imported,quantity_imported,selected_images,nutriscore_grade,generic_name_fr,generic_name_en,ingredients_text_en&page_size=10&json=1&sort_by=unique_scans_n");
        OpenFoodFactSearchResponse expectedResponse = new OpenFoodFactSearchResponse();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(uri)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OpenFoodFactSearchResponse.class)).thenReturn(Mono.just(expectedResponse));

        // When
        OpenFoodFactSearchResponse actualResponse = openFoodFactsService.getSearchResponseJson(uri);

        // Then
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testGetSearchResponseJson_ApiException() {
        // Given
        URI uri = URI.create("https://world.openfoodfacts.org/cgi/search.pl?search_terms=test&fields=products,allergens_imported,allergens,code,brands,brand_imported,compared_to_category,grade,ingredients_text_fr,nutrient_levels,nutriments,product_name_fr_imported,quantity_imported,selected_images,nutriscore_grade,generic_name_fr,generic_name_en,ingredients_text_en&page_size=10&json=1&sort_by=unique_scans_n");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(uri)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OpenFoodFactSearchResponse.class)).thenReturn(Mono.error(new RuntimeException("API error")));

        // When & Then
        assertThrows(ApiException.class, () -> openFoodFactsService.getSearchResponseJson(uri));
    }
}