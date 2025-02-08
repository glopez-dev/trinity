package com.trinity.product.dto.api;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;


class ReadProductDTOTest {

    private Validator validator;
    private ReadProductDTO readProductDTO;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        readProductDTO = new ReadProductDTO();
    }

    private void setRequiredValidFields() {
        readProductDTO.setId(UUID.randomUUID());
        readProductDTO.setBarcode("123456789");
        readProductDTO.setPrice(new BigDecimal("9.99"));
        ReadProductDTO.StockDto stockDto = new ReadProductDTO.StockDto();
        stockDto.setQuantity(10);
        readProductDTO.setStock(stockDto);
    }

    @Test
    void whenAllValidFieldsProvided_thenNoValidationErrors() {
        // Given
        setRequiredValidFields();
        readProductDTO.setNutriscoreGrade("a");

        // When
        Set<ConstraintViolation<ReadProductDTO>> violations = validator.validate(readProductDTO);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    void whenIdIsNull_thenValidationError() {
        // Given
        setRequiredValidFields();
        readProductDTO.setId(null);

        // When
        Set<ConstraintViolation<ReadProductDTO>> violations = validator.validate(readProductDTO);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Id cannot be null");
    }

    @Test
    void whenBarcodeIsNull_thenValidationError() {
        // Given
        setRequiredValidFields();
        readProductDTO.setBarcode(null);

        // When
        Set<ConstraintViolation<ReadProductDTO>> violations = validator.validate(readProductDTO);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Barcode cannot be null");
    }

    @Test
    void whenPriceIsNull_thenValidationError() {
        // Given
        setRequiredValidFields();
        readProductDTO.setPrice(null);

        // When
        Set<ConstraintViolation<ReadProductDTO>> violations = validator.validate(readProductDTO);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Price cannot be null");
    }

    @Test
    void whenPriceIsNegative_thenValidationError() {
        // Given
        setRequiredValidFields();
        readProductDTO.setPrice(new BigDecimal("-10"));

        // When
        Set<ConstraintViolation<ReadProductDTO>> violations = validator.validate(readProductDTO);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Price must be positive");
    }

    @Test
    void whenStockIsNull_thenValidationError() {
        // Given
        setRequiredValidFields();
        readProductDTO.setStock(null);

        // When
        Set<ConstraintViolation<ReadProductDTO>> violations = validator.validate(readProductDTO);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Stock cannot be null");
    }

    @Test
    void whenNutriscoreGradeIsInvalid_thenValidationError() {
        // Given
        setRequiredValidFields();
        readProductDTO.setNutriscoreGrade("z");

        // When
        Set<ConstraintViolation<ReadProductDTO>> violations = validator.validate(readProductDTO);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo("Nutriscore grade must be between a and e");
    }
}