package com.trinity.product.interfaces.rest.dto;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class CreateProductRequestTest {

    private Validator validator;
    private CreateProductRequest createProductDTO;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        createProductDTO = new CreateProductRequest();
    }

    private void setRequiredValidFields() {
        createProductDTO.setBarcode("123456789");
        createProductDTO.setBrand("Test Brand");
        createProductDTO.setName("Test Product");
        createProductDTO.setPrice(new BigDecimal("9.99"));
        CreateProductRequest.StockDto stockDto = new CreateProductRequest.StockDto();
        stockDto.setQuantity(10);
        createProductDTO.setStock(stockDto);
    }

    @Test
    void whenAllValidFieldsProvided_thenNoValidationErrors() {
        // Given
        setRequiredValidFields();
        createProductDTO.setNutriscoreGrade("a");

        // When
        Set<ConstraintViolation<CreateProductRequest>> violations = validator.validate(createProductDTO);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    void whenBarcodeIsNull_thenValidationError() {
        // Given
        setRequiredValidFields();
        createProductDTO.setBarcode(null);

        // When
        Set<ConstraintViolation<CreateProductRequest>> violations = validator.validate(createProductDTO);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Barcode cannot be null");
    }

    @Test
    void whenBrandIsBlank_thenValidationError() {
        // Given
        setRequiredValidFields();
        createProductDTO.setBrand("");

        // When
        Set<ConstraintViolation<CreateProductRequest>> violations = validator.validate(createProductDTO);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Brand cannot be blank");
    }

    @Test
    void whenPriceIsNegative_thenValidationError() {
        // Given
        setRequiredValidFields();
        createProductDTO.setPrice(new BigDecimal("-9.99"));

        // When
        Set<ConstraintViolation<CreateProductRequest>> violations = validator.validate(createProductDTO);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Price must be positive");
    }

    @Test
    void whenNutriscoreGradeIsInvalid_thenValidationError() {
        // Given
        setRequiredValidFields();
        createProductDTO.setNutriscoreGrade("f");

        // When
        Set<ConstraintViolation<CreateProductRequest>> violations = validator.validate(createProductDTO);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo("Nutriscore grade must be between a and e");
    }
}