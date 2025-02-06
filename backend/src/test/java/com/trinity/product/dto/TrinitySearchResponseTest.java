package com.trinity.product.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.trinity.product.model.Product;



class TrinitySearchResponseTest {

    private TrinitySearchResponse response;
    private List<Product> products;

    @BeforeEach
    void setUp() {
        response = new TrinitySearchResponse();
        products = new ArrayList<>();
        Product product = Product.builder()
                .barcode("1234567890123")
                .name("Test Product")
                .price(new BigDecimal("9.99"))
                .build();
        products.add(product);
    }

    @Test
    void testGetProducts() {
        // Given
        response.setProducts(products);

        // When
        List<Product> result = response.getProducts();

        // Then
        assertThat(result).isEqualTo(products);
    }

    @Test
    void testSetProducts() {
        // Given
        List<Product> newProducts = new ArrayList<>();
        Product newProduct = Product.builder()
                .barcode("9876543210987")
                .name("New Product")
                .price(new BigDecimal("19.99"))
                .build();
        newProducts.add(newProduct);

        // When
        response.setProducts(newProducts);

        // Then
        assertThat(response.getProducts()).isEqualTo(newProducts);
    }
}