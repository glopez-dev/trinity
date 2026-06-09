package com.trinity.user.interfaces.rest.dto;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.Instant;
import java.util.Set;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.trinity.user.domain.model.EmployeeRole;


class CreateEmployeeRequestTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void givenValidDTO_whenValidated_thenNoViolations() {
        // Given
        CreateEmployeeRequest dto = new CreateEmployeeRequest(
                "test@example.com",
                "secretPassword",
                "John",
                "Doe",
                EmployeeRole.EMPLOYEE,
                Instant.now()
        );

        // When
        Set<ConstraintViolation<CreateEmployeeRequest>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    void givenEmptyEmail_whenValidated_thenViolation() {
        // Given
        CreateEmployeeRequest dto = new CreateEmployeeRequest(
                "",
                "secretPassword",
                "John",
                "Doe",
                EmployeeRole.EMPLOYEE,
                Instant.now()
        );

        // When
        Set<ConstraintViolation<CreateEmployeeRequest>> violations = validator.validate(dto);

        // Then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Email cannot be empty");
    }

    @Test
    void givenInvalidEmailFormat_whenValidated_thenViolation() {
        // Given
        CreateEmployeeRequest dto = new CreateEmployeeRequest(
                "invalid-email",
                "secretPassword",
                "John",
                "Doe",
                EmployeeRole.EMPLOYEE,
                Instant.now()
        );

        // When
        Set<ConstraintViolation<CreateEmployeeRequest>> violations = validator.validate(dto);

        // Then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Invalid email format");
    }

    @Test
    void givenEmptyPassword_whenValidated_thenViolation() {
        // Given
        CreateEmployeeRequest dto = new CreateEmployeeRequest(
                "test@example.com",
                "",
                "John",
                "Doe",
                EmployeeRole.EMPLOYEE,
                Instant.now()
        );

        // When
        Set<ConstraintViolation<CreateEmployeeRequest>> violations = validator.validate(dto);

        // Then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Password cannot be empty");
    }

    @Test
    void givenEmptyFirstName_whenValidated_thenViolation() {
        // Given
        CreateEmployeeRequest dto = new CreateEmployeeRequest(
                "test@example.com",
                "secretPassword",
                "",
                "Doe",
                EmployeeRole.EMPLOYEE,
                Instant.now()
        );

        // When
        Set<ConstraintViolation<CreateEmployeeRequest>> violations = validator.validate(dto);

        // Then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("First name cannot be empty");
    }

    @Test
    void givenEmptyLastName_whenValidated_thenViolation() {
        // Given
        CreateEmployeeRequest dto = new CreateEmployeeRequest(
                "test@example.com",
                "secretPassword",
                "John",
                "",
                EmployeeRole.EMPLOYEE,
                Instant.now()
        );

        // When
        Set<ConstraintViolation<CreateEmployeeRequest>> violations = validator.validate(dto);

        // Then
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Last name cannot be empty");
    }
}