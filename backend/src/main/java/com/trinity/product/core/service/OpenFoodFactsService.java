package com.trinity.product.core.service;

import java.net.URI;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import com.trinity.product.core.adapter.OpenFoodFactsAdapter;
import com.trinity.product.core.dto.TrinitySearchResponse;
import com.trinity.product.core.dto.open_food_facts.OpenFoodFactSearchResponse;
import com.trinity.product.core.exception.ApiException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OpenFoodFactsService {

    private final WebClient webClient;
    private final UriBuilder uriBuilder;

    static final String FIELDS_TO_GET = "products,allergens_imported,allergens,code,brands,brand_imported,compared_to_category,grade,ingredients_text_fr,nutrient_levels,nutriments,product_name_fr_imported,quantity_imported,selected_images,nutriscore_grade,generic_name_fr,generic_name_en,ingredients_text_en"; 
    
    URI buildUri(String searchTerm) {
        return this.uriBuilder
            .scheme("https")
            .host("world.openfoodfacts.org")
            .path("/cgi/search.pl")
            .queryParam("search_terms", searchTerm)
            .queryParam("fields", OpenFoodFactsService.FIELDS_TO_GET)
            .queryParam("page_size", 10)
            .queryParam("json", 1)
            .queryParam("sort_by", "unique_scans_n")
            .build();
    }

    OpenFoodFactSearchResponse getSearchResponseJson(URI uri) {
        return webClient.get()
            .uri(uri)
            .retrieve()
            .bodyToMono(OpenFoodFactSearchResponse.class)
            .onErrorMap(error -> new ApiException("OpenFoodFacts API error", error))
            .block();
    }

    public TrinitySearchResponse searchProducts(String searchTerm) {
        URI uri = this.buildUri(searchTerm);

        OpenFoodFactSearchResponse externalResponse = this.getSearchResponseJson(uri);

        return OpenFoodFactsAdapter.adapt(externalResponse);
    }

}