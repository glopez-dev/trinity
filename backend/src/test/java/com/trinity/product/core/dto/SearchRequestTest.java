package com.trinity.product.core.dto;

import static org.assertj.core.api.Assertions.assertThat;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;


class SearchRequestTest {

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
        SearchRequest request = new SearchRequest(searchTerm);

        // When
        String result = request.getSearchTerm();

        // Then
        assertThat(result).isEqualTo(searchTerm);
    }

    @Test
    void testSetSearchTerm() {
        // Given
        String searchTerm = "NewSearch";
        SearchRequest request = new SearchRequest();

        // When
        request.setSearchTerm(searchTerm);

        // Then
        assertThat(request.getSearchTerm()).isEqualTo(searchTerm);
    }

    @Test
    void testSearchTermNotEmpty() {
        // Given
        SearchRequest request = new SearchRequest();

        // When
        Set<ConstraintViolation<SearchRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Search term must not be empty");
    }

    @Test
    void testSearchTermPattern() {
        // Given
        SearchRequest request = new SearchRequest("Invalid123");

        // When
        Set<ConstraintViolation<SearchRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Search term must contain only alphabetic characters");
    }

    @Test
    void testValidSearchTerm() {
        // Given
        SearchRequest request = new SearchRequest("ValidSearch");

        // When
        Set<ConstraintViolation<SearchRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }
}