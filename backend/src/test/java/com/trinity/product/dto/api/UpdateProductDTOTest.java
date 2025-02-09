package com.trinity.product.dto.api;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class UpdateProductDTOTest {

    private UpdateProductDTO dto;

    @BeforeEach
    void setUp() {
        dto = new UpdateProductDTO();
    }

    @Test
    void testGetSetNameWithValue() {
        // Given
        String name = "Test Product";
        
        // When
        dto.setName(Optional.of(name));
        
        // Then
        assertThat(dto.getName()).isPresent();
        assertThat(dto.getName().get()).isEqualTo(name);
    }

    @Test
    void testGetSetNameWithNull() {
        // Given
        dto.setName(Optional.empty());
        
        // When
        Optional<String> result = dto.getName();
        
        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void testGetSetPriceWithValue() {
        // Given
        BigDecimal price = new BigDecimal("99.99");
        
        // When
        dto.setPrice(Optional.of(price));
        
        // Then
        assertThat(dto.getPrice()).isPresent();
        assertThat(dto.getPrice().get()).isEqualByComparingTo(price);
    }

    @Test
    void testGetSetPriceWithNull() {
        // Given
        dto.setPrice(Optional.empty());
        
        // When
        Optional<BigDecimal> result = dto.getPrice();
        
        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void testGetSetQuantityWithValue() {
        // Given
        Integer quantity = 100;
        
        // When
        dto.setQuantity(Optional.of(quantity));
        
        // Then
        assertThat(dto.getQuantity()).isPresent();
        assertThat(dto.getQuantity().get()).isEqualTo(quantity);
    }

    @Test
    void testGetSetQuantityWithNull() {
        // Given
        dto.setQuantity(Optional.empty());
        
        // When
        Optional<Integer> result = dto.getQuantity();
        
        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void testAllFieldsPopulated() {
        // Given
        String name = "Test Product";
        BigDecimal price = new BigDecimal("99.99");
        Integer quantity = 100;
        
        // When
        dto.setName(Optional.of(name));
        dto.setPrice(Optional.of(price));
        dto.setQuantity(Optional.of(quantity));
        
        // Then
        assertThat(dto.getName()).isPresent().contains(name);
        assertThat(dto.getPrice()).isPresent().map(BigDecimal::doubleValue).contains(99.99);
        assertThat(dto.getQuantity()).isPresent().contains(quantity);
    }
}