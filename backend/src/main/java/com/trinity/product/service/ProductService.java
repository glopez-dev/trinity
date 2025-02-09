package com.trinity.product.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.trinity.product.dto.api.CreateProductDTO;
import com.trinity.product.dto.api.ReadProductDTO;
import com.trinity.product.dto.api.UpdateProductDTO;
import com.trinity.product.mapper.ProductMapper;
import com.trinity.product.model.Product;
import com.trinity.product.repository.ProductRepository;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ReadProductDTO createProduct(CreateProductDTO productDTO) {
        Product mappedProduct = productMapper.toEntity(productDTO);
        Product savedProduct = productRepository.save(mappedProduct);
        return productMapper.toDTO(savedProduct);
    }

    public List<ReadProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            return Collections.emptyList();
        }

        return products.stream()
            .map(productMapper::toDTO)
            .toList();
    }

    public ReadProductDTO getProduct(UUID productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        return productMapper.toDTO(product);
    }

    public ReadProductDTO updateProduct(UUID productId, UpdateProductDTO request) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        request.getName().ifPresent(product::setName);
        request.getPrice().ifPresent(product::setPrice);

        request.getQuantity().ifPresent(quantity -> {
            Product.Stock stock = product.getStock();
            stock.setQuantity(stock.getQuantity() + quantity);
            product.setStock(stock);
        });


        Product updatedProduct = productRepository.save(product);

        return productMapper.toDTO(updatedProduct);
    }

}
