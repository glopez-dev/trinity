package com.trinity.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.trinity.product.dto.api.CreateProductDTO;
import com.trinity.product.dto.api.ReadProductDTO;
import com.trinity.product.dto.api.UpdateProductDTO;
import com.trinity.product.exception.InvalidProductDataException;
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
        CreateProductDTO.StockDto stockDTO = new CreateProductDTO.StockDto();
        stockDTO.setMinThreshold(0);
        stockDTO.setQuantity(5);
        stockDTO.setMaxThreshold(10);
        createProductDTO.setStock(stockDTO);
        
        product = new Product();
        product.setId(UUID.randomUUID());
        
        savedProduct = new Product();
        savedProduct.setId(UUID.randomUUID());
        
        readProductDTO = new ReadProductDTO();
        readProductDTO.setId(savedProduct.getId());
    }

    @Test
    void testCreateProduct() {
        // GIVEN
        when(productMapper.toEntity(createProductDTO)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(productMapper.toDTO(savedProduct)).thenReturn(readProductDTO);

        // WHEN
        ReadProductDTO result = productService.createProduct(createProductDTO);

        // THEN
        verify(productMapper).toEntity(createProductDTO);
        verify(productRepository).save(product);
        verify(productMapper).toDTO(savedProduct);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(savedProduct.getId());
    }



    @Test
    void testUpdateProduct_Success() {
        // GIVEN
        UUID productId = UUID.randomUUID();
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        Product.Stock stock = new Product.Stock();
        stock.setQuantity(10);
        existingProduct.setStock(stock);

        UpdateProductDTO updateDTO = new UpdateProductDTO();
        updateDTO.setName(Optional.of("Updated Name"));
        updateDTO.setPrice(Optional.of(BigDecimal.valueOf(19)));
        updateDTO.setQuantity(Optional.of(4));

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);
        ReadProductDTO updatedDTO = new ReadProductDTO();
        updatedDTO.setId(productId);
        when(productMapper.toDTO(existingProduct)).thenReturn(updatedDTO);

        // WHEN
        ReadProductDTO result = productService.updateProduct(productId, updateDTO);

        // THEN
        verify(productRepository).findById(productId);
        verify(productRepository).save(existingProduct);
        assertThat(existingProduct.getName()).isEqualTo("Updated Name");
        assertThat(existingProduct.getPrice()).isEqualTo(BigDecimal.valueOf(19));
        assertThat(existingProduct.getStock().getQuantity()).isEqualTo(14);
        assertThat(result.getId()).isEqualTo(productId);
    }

    @Test
    void testUpdateProduct_NegativePrice() {
        // GIVEN
        UUID productId = UUID.randomUUID();
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        UpdateProductDTO updateDTO = new UpdateProductDTO();
        updateDTO.setPrice(Optional.of(BigDecimal.valueOf(-2)));

        // WHEN - THEN
        assertThatExceptionOfType(InvalidProductDataException.class)
            .isThrownBy(() -> productService.updateProduct(productId, updateDTO))
            .withMessageContaining("Price cannot be negative");
    }

    @Test
    void testUpdateProduct_NegativeStock() {
        // GIVEN
        UUID productId = UUID.randomUUID();
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        Product.Stock stock = new Product.Stock();
        stock.setQuantity(4);
        existingProduct.setStock(stock);
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        UpdateProductDTO updateDTO = new UpdateProductDTO();
        updateDTO.setQuantity(Optional.of(-11));

        // WHEN - THEN
        assertThatExceptionOfType(InvalidProductDataException.class)
            .isThrownBy(() -> productService.updateProduct(productId, updateDTO))
            .withMessageContaining("Stock quantity cannot be negative");
    }



}