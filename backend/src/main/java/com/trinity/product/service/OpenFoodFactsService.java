package com.trinity.product.service;

import java.net.URI;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.trinity.product.adapter.OpenFoodFactsAdapter;
import com.trinity.product.dto.TrinitySearchResponse;
import com.trinity.product.dto.open_food_facts.OpenFoodFactSearchResponse;
import com.trinity.product.exception.ApiException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OpenFoodFactsService {

    private final WebClient webClient;

    static final String FIELDS_TO_GET = "products,allergens_imported,allergens,code,brands,brand_imported,compared_to_category,grade,ingredients_text_fr,nutrient_levels,nutriments,product_name_fr_imported,quantity_imported,selected_images,nutriscore_grade,generic_name_fr,generic_name_en,ingredients_text_en"; 
    
    public URI buildUri(String searchTerm) {
        return UriComponentsBuilder.fromHttpUrl("https://world.openfoodfacts.org/cgi/search.pl")
                .queryParam("search_terms", searchTerm)
                .queryParam("fields", OpenFoodFactsService.FIELDS_TO_GET)
                .queryParam("page_size", 10)
                .queryParam("json", 1)
                .queryParam("sort_by", "unique_scans_n")
                .build()
                .toUri();
    }

    public OpenFoodFactSearchResponse getSearchResponseJson(URI uri) {
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