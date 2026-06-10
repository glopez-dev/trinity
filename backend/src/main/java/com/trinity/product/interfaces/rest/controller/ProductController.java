package com.trinity.product.interfaces.rest.controller;

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

import com.trinity.product.application.ProductService;
import com.trinity.product.application.command.UpdateProductCommand;
import com.trinity.product.interfaces.rest.dto.CreateProductRequest;
import com.trinity.product.interfaces.rest.dto.ProductResponse;
import com.trinity.product.interfaces.rest.dto.SearchProductRequest;
import com.trinity.product.interfaces.rest.dto.UpdateProductRequest;
import com.trinity.product.interfaces.rest.mapper.ProductApiMapper;

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

    private final ProductService productService;
    private final ProductApiMapper productApiMapper;

    @PostMapping("/search")
    @Operation(summary = "Search products", description = "Search for products using a search term")
    public ResponseEntity<List<ProductResponse>> searchProducts(@Valid @RequestBody SearchProductRequest request) {
        List<ProductResponse> products = productService.searchProducts(request.getSearchTerm()).stream()
                .map(productApiMapper::toDTO)
                .toList();
        return ResponseEntity.ok(products);
    }

    @PostMapping()
    @Operation(summary = "Create a product", description = "Create a new product")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        ProductResponse product = productApiMapper.toDTO(
                productService.createProduct(productApiMapper.toEntity(request)));
        return ResponseEntity.ok(product);
    }

    @GetMapping()
    @Operation(summary = "Get all products", description = "Retrieve all products")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts().stream()
                .map(productApiMapper::toDTO)
                .toList();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get a product", description = "Retrieve a product by its ID")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable UUID productId) {
        ProductResponse product = productApiMapper.toDTO(productService.getProduct(productId));
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update a product", description = "Update an existing product by its ID")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable UUID productId, @Valid @RequestBody UpdateProductRequest request) {
        UpdateProductCommand command = new UpdateProductCommand(
                request.getName(), request.getPrice(), request.getQuantity());
        ProductResponse product = productApiMapper.toDTO(productService.updateProduct(productId, command));
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
        ProductResponse product = productApiMapper.toDTO(productService.scanProduct(barcode));
        return ResponseEntity.ok(product);
    }
}
