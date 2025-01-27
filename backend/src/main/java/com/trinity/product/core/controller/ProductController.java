package com.trinity.product.core.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trinity.product.core.dto.SearchRequest;
import com.trinity.product.core.dto.TrinitySearchResponse;
import com.trinity.product.core.service.OpenFoodFactsService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/api/v1/product")
@AllArgsConstructor
public class ProductController {

    private final OpenFoodFactsService openFoodFactsService;

    /**
    public TrinitySearchResponse searchProducts(@Valid @RequestBody SearchRequest request) {
     *
     * @param request the search request containing the search term
     * @return a response containing the search results
     */
    @PostMapping("/search")
    public ResponseEntity<TrinitySearchResponse> searchProducts(@Valid @RequestBody SearchRequest request) {

        String searchTerm = request.getSearchTerm();
        TrinitySearchResponse response = openFoodFactsService.searchProducts(searchTerm);
        return ResponseEntity.ok(response);
    }
 
}
