package com.trinity.product.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trinity.product.dto.api.CreateProductDTO;
import com.trinity.product.dto.api.ReadProductDTO;
import com.trinity.product.dto.api.SearchProductDTO;
import com.trinity.product.dto.api.UpdateProductDTO;
import com.trinity.product.service.OpenFoodFactsService;
import com.trinity.product.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/product")
@AllArgsConstructor
@Tag(name = "Product", description = "Product management APIs")
public class ProductController {

    private final OpenFoodFactsService openFoodFactsService;
    private final ProductService productService;

    @PostMapping("/search")
    @Operation(summary = "Search products", description = "Search for products using a search term")
    public ResponseEntity<List<ReadProductDTO>> searchProducts(@Valid @RequestBody SearchProductDTO request) {
        String searchTerm = request.getSearchTerm();
        List<ReadProductDTO> products = openFoodFactsService.searchProducts(searchTerm);
        return ResponseEntity.ok(products);
    }

    @PostMapping()
    @Operation(summary = "Create a product", description = "Create a new product")
    public ResponseEntity<ReadProductDTO> createProduct(@Valid @RequestBody CreateProductDTO request) {
        ReadProductDTO product = productService.createProduct(request);
        return ResponseEntity.ok(product);
    }

    @GetMapping()
    @Operation(summary = "Get all products", description = "Retrieve all products")
    public ResponseEntity<List<ReadProductDTO>> getAllProducts() {
        List<ReadProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get a product", description = "Retrieve a product by its ID")
    public ResponseEntity<ReadProductDTO> getProduct(@PathVariable UUID productId) {
        ReadProductDTO product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update a product", description = "Update an existing product by its ID")
    public ResponseEntity<ReadProductDTO> updateProduct(@PathVariable UUID productId, @Valid @RequestBody UpdateProductDTO request) {
        ReadProductDTO product = productService.updateProduct(productId, request);
        return ResponseEntity.ok(product);
    }

}
