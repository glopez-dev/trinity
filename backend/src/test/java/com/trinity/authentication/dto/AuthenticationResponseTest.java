package com.trinity.authentication.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class AuthenticationResponseTest {

    private AuthenticationResponse authenticationResponse;

    @BeforeEach
    void setUp() {
        authenticationResponse = AuthenticationResponse.builder()
                .jwt("sample-jwt-token")
                .build();
    }

    @Test
    void testGetJwt() {
        // Given
        String expectedJwt = "sample-jwt-token";

        // When
        String actualJwt = authenticationResponse.getJwt();

        // Then
        assertEquals(expectedJwt, actualJwt);
    }

    @Test
    void testSetJwt() {
        // Given
        String newJwt = "new-jwt-token";

        // When
        authenticationResponse.setJwt(newJwt);

        // Then
        assertEquals(newJwt, authenticationResponse.getJwt());
    }

    @Test
    void testToString() {
        // Given
        String expectedString = "AuthenticationResponse(jwt=sample-jwt-token)";

        // When
        String actualString = authenticationResponse.toString();

        // Then
        assertEquals(expectedString, actualString);
    }

    @Test
    void testEquals() {
        // Given
        AuthenticationResponse anotherResponse = AuthenticationResponse.builder()
                .jwt("sample-jwt-token")
                .build();

        // When & Then
        assertEquals(authenticationResponse, anotherResponse);
    }

    @Test
    void testHashCode() {
        // Given
        AuthenticationResponse anotherResponse = AuthenticationResponse.builder()
                .jwt("sample-jwt-token")
                .build();

        // When & Then
        assertEquals(authenticationResponse.hashCode(), anotherResponse.hashCode());
    }
}