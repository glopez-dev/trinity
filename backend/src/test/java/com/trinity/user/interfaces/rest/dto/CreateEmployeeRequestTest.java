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

    @Test
    void givenRequest_whenAccessingRoleAndHireDate_thenGettersReturnValues() {
        Instant hireDate = Instant.now();
        CreateEmployeeRequest dto = new CreateEmployeeRequest(
                "test@example.com", "secretPassword", "John", "Doe", EmployeeRole.MANAGER, hireDate);

        assertThat(dto.getRole()).isEqualTo(EmployeeRole.MANAGER);
        assertThat(dto.getHireDate()).isEqualTo(hireDate);
        assertThat(dto.getEmail()).isEqualTo("test@example.com");
        assertThat(dto.getPassword()).isEqualTo("secretPassword");
        assertThat(dto.getFirstName()).isEqualTo("John");
        assertThat(dto.getLastName()).isEqualTo("Doe");
    }

    @Test
    void givenRequest_whenSettersUsed_thenGettersReflectChanges() {
        CreateEmployeeRequest dto = new CreateEmployeeRequest(
                "test@example.com", "secretPassword", "John", "Doe", EmployeeRole.EMPLOYEE, Instant.now());

        Instant newHireDate = Instant.now().plusSeconds(60);
        dto.setEmail("new@example.com");
        dto.setPassword("newPassword");
        dto.setFirstName("Jane");
        dto.setLastName("Smith");
        dto.setRole(EmployeeRole.ADMIN);
        dto.setHireDate(newHireDate);

        assertThat(dto.getEmail()).isEqualTo("new@example.com");
        assertThat(dto.getPassword()).isEqualTo("newPassword");
        assertThat(dto.getFirstName()).isEqualTo("Jane");
        assertThat(dto.getLastName()).isEqualTo("Smith");
        assertThat(dto.getRole()).isEqualTo(EmployeeRole.ADMIN);
        assertThat(dto.getHireDate()).isEqualTo(newHireDate);
    }

    @Test
    void givenTwoEqualRequests_thenEqualsHashCodeAndToStringConsistent() {
        Instant hireDate = Instant.now();
        CreateEmployeeRequest a = new CreateEmployeeRequest(
                "test@example.com", "secretPassword", "John", "Doe", EmployeeRole.EMPLOYEE, hireDate);
        CreateEmployeeRequest b = new CreateEmployeeRequest(
                "test@example.com", "secretPassword", "John", "Doe", EmployeeRole.EMPLOYEE, hireDate);

        assertThat(a).isEqualTo(b);
        assertThat(a).hasSameHashCodeAs(b);
        assertThat(a.toString()).contains("test@example.com");
    }
}