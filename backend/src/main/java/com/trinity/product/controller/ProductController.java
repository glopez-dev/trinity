package com.trinity.product.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trinity.product.dto.api.CreateProductRequest;
import com.trinity.product.dto.api.ProductResponse;
import com.trinity.product.dto.api.SearchProductRequest;
import com.trinity.product.dto.api.UpdateProductRequest;
import com.trinity.product.service.OpenFoodFactsService;
import com.trinity.product.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/product")
@AllArgsConstructor
@Tag(name = "Product", description = "Product management APIs")
public class ProductController {

    private final OpenFoodFactsService openFoodFactsService;
    private final ProductService productService;

    @PostMapping("/search")
    @Operation(summary = "Search products", description = "Search for products using a search term")
    public ResponseEntity<List<ProductResponse>> searchProducts(@Valid @RequestBody SearchProductRequest request) {
        String searchTerm = request.getSearchTerm();
        List<ProductResponse> products = openFoodFactsService.searchProducts(searchTerm);
        return ResponseEntity.ok(products);
    }

    @PostMapping()
    @Operation(summary = "Create a product", description = "Create a new product")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        ProductResponse product = productService.createProduct(request);
        return ResponseEntity.ok(product);
    }

    @GetMapping()
    @Operation(summary = "Get all products", description = "Retrieve all products")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get a product", description = "Retrieve a product by its ID")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable UUID productId) {
        ProductResponse product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update a product", description = "Update an existing product by its ID")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable UUID productId, @Valid @RequestBody UpdateProductRequest request) {
        ProductResponse product = productService.updateProduct(productId, request);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete a product", description = "Delete a product by its ID")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/scan/{barcode}")
    @Operation(summary = "Scan a product", description = "Scan a product by its barcode, retrieve from database or OpenFoodFacts")
    public ResponseEntity<ProductResponse> scanProduct(@PathVariable String barcode) {
        if (barcode == null || !barcode.matches("^[0-9]{8,13}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid barcode format");
        }
        ProductResponse product = productService.scanProduct(barcode);
        return ResponseEntity.ok(product);
    }

}
