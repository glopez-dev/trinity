package com.trinity.product.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trinity.product.dto.api.CreateProductDTO;
import com.trinity.product.dto.api.ReadProductDTO;
import com.trinity.product.dto.api.SearchProductDTO;
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
    public ResponseEntity<List<ReadProductDTO>> searchProducts(@Valid @RequestBody SearchProductDTO request) {

        String searchTerm = request.getSearchTerm();
        List<ReadProductDTO> products = openFoodFactsService.searchProducts(searchTerm);
        return ResponseEntity.ok(products);
    }

    @PostMapping()
    public ResponseEntity<ReadProductDTO> createProduct(@Valid @RequestBody CreateProductDTO request) {
        ReadProductDTO product = productService.createProduct(request);
        return ResponseEntity.ok(product);
    }

    @GetMapping()
    public ResponseEntity<List<ReadProductDTO>> getAllProducts() {
        List<ReadProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ReadProductDTO> getProduct(@PathVariable UUID productId) {
        ReadProductDTO product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

}
