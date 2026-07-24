package com.trinity.authentication.interfaces.rest.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class RegisterRequestTest {

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = RegisterRequest.builder()
                .email("test@example.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .build();
    }

    @Test
    void testGetEmail() {
        // Given
        String expectedEmail = "test@example.com";

        // When
        String actualEmail = registerRequest.getEmail();

        // Then
        assertEquals(expectedEmail, actualEmail);
    }

    @Test
    void testSetEmail() {
        // Given
        String newEmail = "new@example.com";

        // When
        registerRequest.setEmail(newEmail);

        // Then
        assertEquals(newEmail, registerRequest.getEmail());
    }

    @Test
    void testGetPassword() {
        // Given
        String expectedPassword = "password";

        // When
        String actualPassword = registerRequest.getPassword();

        // Then
        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    void testSetPassword() {
        // Given
        String newPassword = "newPassword";

        // When
        registerRequest.setPassword(newPassword);

        // Then
        assertEquals(newPassword, registerRequest.getPassword());
    }

    @Test
    void testGetFirstName() {
        // Given
        String expectedFirstName = "John";

        // When
        String actualFirstName = registerRequest.getFirstName();

        // Then
        assertEquals(expectedFirstName, actualFirstName);
    }

    @Test
    void testSetFirstName() {
        // Given
        String newFirstName = "Jane";

        // When
        registerRequest.setFirstName(newFirstName);

        // Then
        assertEquals(newFirstName, registerRequest.getFirstName());
    }

    @Test
    void testGetLastName() {
        // Given
        String expectedLastName = "Doe";

        // When
        String actualLastName = registerRequest.getLastName();

        // Then
        assertEquals(expectedLastName, actualLastName);
    }

    @Test
    void testSetLastName() {
        // Given
        String newLastName = "Smith";

        // When
        registerRequest.setLastName(newLastName);

        // Then
        assertEquals(newLastName, registerRequest.getLastName());
    }

    @Test
    void testToString() {
        // Given
        String expectedString = "RegisterRequest(email=test@example.com, password=password, firstName=John, lastName=Doe)";

        // When
        String actualString = registerRequest.toString();

        // Then
        assertEquals(expectedString, actualString);
    }

    @Test
    void testEquals() {
        // Given
        RegisterRequest anotherRequest = RegisterRequest.builder()
                .email("test@example.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .build();

        // When & Then
        assertEquals(registerRequest, anotherRequest);
    }

    @Test
    void testHashCode() {
        // Given
        RegisterRequest anotherRequest = RegisterRequest.builder()
                .email("test@example.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .build();

        // When & Then
        assertEquals(registerRequest.hashCode(), anotherRequest.hashCode());
    }
}