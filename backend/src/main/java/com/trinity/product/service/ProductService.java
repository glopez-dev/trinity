package com.trinity.product.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.trinity.product.exception.ProductNotFoundException;
import com.trinity.product.exception.InvalidProductDataException;
import com.trinity.product.dto.api.*;
import com.trinity.product.mapper.ProductMapper;
import com.trinity.product.model.Product;
import com.trinity.product.repository.ProductRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ReadProductDTO createProduct(CreateProductDTO productDTO) {
        validateProductData(productDTO);
        Product mappedProduct = productMapper.toEntity(productDTO);
        Product savedProduct = productRepository.save(mappedProduct);
        logger.info("Created new product with ID: {}", savedProduct.getId());
        return productMapper.toDTO(savedProduct);
    }

    public List<ReadProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        logger.debug("Retrieved {} products", products.size());
        return products.isEmpty() ? 
            Collections.emptyList() :
            products.stream().map(productMapper::toDTO).toList();
    }

    public ReadProductDTO getProduct(UUID productId) {
        Product product = findProductById(productId);
        return productMapper.toDTO(product);
    }

    public ReadProductDTO updateProduct(UUID productId, UpdateProductDTO request) {
        Product product = findProductById(productId);
        
        request.getName().ifPresent(product::setName);
        request.getPrice().ifPresent(price -> {
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                throw new InvalidProductDataException("Price cannot be negative");
            }
            product.setPrice(price);
        });
        
        request.getQuantity().ifPresent(quantity -> updateProductStock(product, quantity));
        
        Product updatedProduct = productRepository.save(product);
        logger.info("Updated product with ID: {}", productId);
        return productMapper.toDTO(updatedProduct);
    }

    public void deleteProduct(UUID productId) {
        Product product = findProductById(productId);
        productRepository.delete(product);
        logger.info("Deleted product with ID: {}", productId);
    }

    private Product findProductById(UUID productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(
                String.format("Product not found with ID: %s", productId)));
    }

    private void updateProductStock(Product product, Integer quantityChange) {
        Product.Stock stock = product.getStock();
        int newQuantity = stock.getQuantity() + quantityChange;
        if (newQuantity < 0) {
            throw new InvalidProductDataException("Stock quantity cannot be negative");
        }
        stock.setQuantity(newQuantity);
        product.setStock(stock);
    }

    private void validateProductData(CreateProductDTO productDTO) {
        if (productDTO.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidProductDataException("Price cannot be negative");
        }
        if (productDTO.getStock().getQuantity() < 0) {
            throw new InvalidProductDataException("Initial stock quantity cannot be negative");
        }
    }
}