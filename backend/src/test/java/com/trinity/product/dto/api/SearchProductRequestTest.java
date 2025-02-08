package com.trinity.product.dto.api;

import static org.assertj.core.api.Assertions.assertThat;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.trinity.product.dto.api.SearchProductRequest;

import java.util.Set;


class SearchProductRequestTest {

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
        SearchProductRequest request = new SearchProductRequest(searchTerm);

        // When
        String result = request.getSearchTerm();

        // Then
        assertThat(result).isEqualTo(searchTerm);
    }

    @Test
    void testSetSearchTerm() {
        // Given
        String searchTerm = "NewSearch";
        SearchProductRequest request = new SearchProductRequest();

        // When
        request.setSearchTerm(searchTerm);

        // Then
        assertThat(request.getSearchTerm()).isEqualTo(searchTerm);
    }

    @Test
    void testSearchTermNotEmpty() {
        // Given
        SearchProductRequest request = new SearchProductRequest();

        // When
        Set<ConstraintViolation<SearchProductRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Search term must not be empty");
    }

    @Test
    void testSearchTermPattern() {
        // Given
        SearchProductRequest request = new SearchProductRequest("Invalid123");

        // When
        Set<ConstraintViolation<SearchProductRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Search term must contain only alphabetic characters");
    }

    @Test
    void testValidSearchTerm() {
        // Given
        SearchProductRequest request = new SearchProductRequest("ValidSearch");

        // When
        Set<ConstraintViolation<SearchProductRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }
}