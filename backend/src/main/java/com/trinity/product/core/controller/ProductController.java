package com.trinity.product.core.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trinity.product.core.dto.TrinitySearchRequest;
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
     * Handles the search request for products.
     *
     * @param request the search request containing the search term
     * @return a ResponseEntity containing the search response
     */
    @PostMapping("/search")
    public ResponseEntity<TrinitySearchResponse> searchProducts(@Valid @RequestBody TrinitySearchRequest request) {

        String searchTerm = request.getSearchTerm();
        TrinitySearchResponse response = openFoodFactsService.searchProducts(searchTerm);
        return ResponseEntity.ok(response);
    }

}
