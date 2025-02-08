package com.trinity.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.trinity.product.dto.api.CreateProductDTO;
import com.trinity.product.dto.api.ReadProductDTO;
import com.trinity.product.mapper.ProductMapper;
import com.trinity.product.model.Product;
import com.trinity.product.repository.ProductRepository;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private CreateProductDTO createProductDTO;
    private Product product;
    private Product savedProduct;
    private ReadProductDTO readProductDTO;

    @BeforeEach
    void setUp() {
        createProductDTO = new CreateProductDTO();
        createProductDTO.setBarcode("1234567890123");
        createProductDTO.setBrand("Test Brand");
        createProductDTO.setName("Test Product");
        createProductDTO.setPrice(BigDecimal.TEN);
        // Set other required fields...
        
        product = new Product();
        product.setId(UUID.randomUUID());
        
        savedProduct = new Product();
        savedProduct.setId(UUID.randomUUID());
        
        readProductDTO = new ReadProductDTO();
        readProductDTO.setId(savedProduct.getId());
    }

    @Test
    void testCreateProduct() {
        // Given
        when(productMapper.toEntity(createProductDTO)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(productMapper.toDTO(savedProduct)).thenReturn(readProductDTO);

        // When
        ReadProductDTO result = productService.createProduct(createProductDTO);

        // Then
        verify(productMapper).toEntity(createProductDTO);
        verify(productRepository).save(product);
        verify(productMapper).toDTO(savedProduct);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(savedProduct.getId());
    }
}