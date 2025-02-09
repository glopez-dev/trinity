package com.trinity.product.dto.api;

import static org.assertj.core.api.Assertions.assertThat;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;


class SearchProductDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testGetSearchTerm() {
        // Given
        String searchTerm = "TestSearch";
        SearchProductDTO request = new SearchProductDTO(searchTerm);

        // When
        String result = request.getSearchTerm();

        // Then
        assertThat(result).isEqualTo(searchTerm);
    }

    @Test
    void testSetSearchTerm() {
        // Given
        String searchTerm = "NewSearch";
        SearchProductDTO request = new SearchProductDTO();

        // When
        request.setSearchTerm(searchTerm);

        // Then
        assertThat(request.getSearchTerm()).isEqualTo(searchTerm);
    }

    @Test
    void testSearchTermNotEmpty() {
        // Given
        SearchProductDTO request = new SearchProductDTO();

        // When
        Set<ConstraintViolation<SearchProductDTO>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Search term must not be empty");
    }

    @Test
    void testSearchTermPattern() {
        // Given
        SearchProductDTO request = new SearchProductDTO("Invalid123");

        // When
        Set<ConstraintViolation<SearchProductDTO>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Search term must contain only alphabetic characters");
    }

    @Test
    void testValidSearchTerm() {
        // Given
        SearchProductDTO request = new SearchProductDTO("ValidSearch");

        // When
        Set<ConstraintViolation<SearchProductDTO>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }
}