package com.trinity.authentication.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class LoginRequestTest {

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();
    }

    @Test
    void testGetEmail() {
        // Given
        String expectedEmail = "test@example.com";

        // When
        String actualEmail = loginRequest.getEmail();

        // Then
        assertEquals(expectedEmail, actualEmail);
    }

    @Test
    void testSetEmail() {
        // Given
        String newEmail = "new@example.com";

        // When
        loginRequest.setEmail(newEmail);

        // Then
        assertEquals(newEmail, loginRequest.getEmail());
    }

    @Test
    void testGetPassword() {
        // Given
        String expectedPassword = "password";

        // When
        String actualPassword = loginRequest.getPassword();

        // Then
        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    void testSetPassword() {
        // Given
        String newPassword = "newPassword";

        // When
        loginRequest.setPassword(newPassword);

        // Then
        assertEquals(newPassword, loginRequest.getPassword());
    }

    @Test
    void testToString() {
        // Given
        String expectedString = "LoginRequest(email=test@example.com, password=password)";

        // When
        String actualString = loginRequest.toString();

        // Then
        assertEquals(expectedString, actualString);
    }

    @Test
    void testEquals() {
        // Given
        LoginRequest anotherRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        // When & Then
        assertEquals(loginRequest, anotherRequest);
    }

    @Test
    void testHashCode() {
        // Given
        LoginRequest anotherRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        // When & Then
        assertEquals(loginRequest.hashCode(), anotherRequest.hashCode());
    }
}