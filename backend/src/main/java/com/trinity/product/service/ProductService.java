package com.trinity.product.service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.trinity.product.dto.api.CreateProductDTO;
import com.trinity.product.dto.api.ReadProductDTO;
import com.trinity.product.mapper.ProductMapper;
import com.trinity.product.model.Product;
import com.trinity.product.repository.ProductRepository;

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
 
}
