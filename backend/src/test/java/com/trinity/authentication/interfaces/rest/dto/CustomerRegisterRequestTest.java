package com.trinity.authentication.interfaces.rest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerRegisterRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private CustomerRegisterRequest validRequest() {
        return CustomerRegisterRequest.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .password("password123")
                .build();
    }

    @Test
    void givenValidRequest_whenValidated_thenNoViolations() {
        Set<ConstraintViolation<CustomerRegisterRequest>> violations = validator.validate(validRequest());

        assertThat(violations).isEmpty();
    }

    @Test
    void givenEmptyFirstName_whenValidated_thenViolation() {
        CustomerRegisterRequest request = validRequest();
        request.setFirstName("");

        assertThat(validator.validate(request))
                .extracting(ConstraintViolation::getMessage)
                .contains("First name is required");
    }

    @Test
    void givenEmptyLastName_whenValidated_thenViolation() {
        CustomerRegisterRequest request = validRequest();
        request.setLastName("");

        assertThat(validator.validate(request))
                .extracting(ConstraintViolation::getMessage)
                .contains("Last name is required");
    }

    @Test
    void givenEmptyEmail_whenValidated_thenViolation() {
        CustomerRegisterRequest request = validRequest();
        request.setEmail("");

        assertThat(validator.validate(request))
                .extracting(ConstraintViolation::getMessage)
                .contains("Email is required");
    }

    @Test
    void givenInvalidEmailFormat_whenValidated_thenViolation() {
        CustomerRegisterRequest request = validRequest();
        request.setEmail("not-an-email");

        assertThat(validator.validate(request))
                .extracting(ConstraintViolation::getMessage)
                .contains("Email should be valid");
    }

    @Test
    void givenEmptyPassword_whenValidated_thenViolation() {
        CustomerRegisterRequest request = validRequest();
        request.setPassword("");

        assertThat(validator.validate(request))
                .extracting(ConstraintViolation::getMessage)
                .contains("Password is required");
    }
}
