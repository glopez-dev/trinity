package com.trinity.product.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trinity.product.dto.api.CreateProductDTO;
import com.trinity.product.dto.api.ReadProductDTO;
import com.trinity.product.dto.api.SearchProductRequest;
import com.trinity.product.dto.api.SearchProductResponse;
import com.trinity.product.service.OpenFoodFactsService;
import com.trinity.product.service.ProductService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/api/v1/product")
@AllArgsConstructor
public class ProductController {

    private final OpenFoodFactsService openFoodFactsService;
    private final ProductService productService;

    @PostMapping("/search")
    public ResponseEntity<SearchProductResponse> searchProducts(@Valid @RequestBody SearchProductRequest request) {

        String searchTerm = request.getSearchTerm();
        SearchProductResponse response = openFoodFactsService.searchProducts(searchTerm);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<ReadProductDTO> createProduct(@Valid @RequestBody CreateProductDTO request) {
        ReadProductDTO product = productService.createProduct(request);
        return ResponseEntity.ok(product);
    }

}
