package com.trinity.product.dto;

import static org.assertj.core.api.Assertions.assertThat;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;


class TrinitySearchRequestTest {

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
        TrinitySearchRequest request = new TrinitySearchRequest(searchTerm);

        // When
        String result = request.getSearchTerm();

        // Then
        assertThat(result).isEqualTo(searchTerm);
    }

    @Test
    void testSetSearchTerm() {
        // Given
        String searchTerm = "NewSearch";
        TrinitySearchRequest request = new TrinitySearchRequest();

        // When
        request.setSearchTerm(searchTerm);

        // Then
        assertThat(request.getSearchTerm()).isEqualTo(searchTerm);
    }

    @Test
    void testSearchTermNotEmpty() {
        // Given
        TrinitySearchRequest request = new TrinitySearchRequest();

        // When
        Set<ConstraintViolation<TrinitySearchRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Search term must not be empty");
    }

    @Test
    void testSearchTermPattern() {
        // Given
        TrinitySearchRequest request = new TrinitySearchRequest("Invalid123");

        // When
        Set<ConstraintViolation<TrinitySearchRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Search term must contain only alphabetic characters");
    }

    @Test
    void testValidSearchTerm() {
        // Given
        TrinitySearchRequest request = new TrinitySearchRequest("ValidSearch");

        // When
        Set<ConstraintViolation<TrinitySearchRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }
}