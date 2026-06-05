package com.trinity.user.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.trinity.common.domain.exception.BusinessRuleViolation;
import com.trinity.user.constant.UserType;


class CustomerTest {

    @Test
    void testCustomerBuilder() {
        // Given
        String email = "test@example.com";
        String hashedPassword = "hashedPassword";
        String stripeUserId = "stripeUserId";
        String stripeAccessToken = "accessToken";
        String stripeRefreshToken = "refreshToken";
        Instant tokenExpiresAt = Instant.now().plusSeconds(3600);

        // When
        Customer customer = Customer.builder()
                .email(email)
                .hashedPassword(hashedPassword)
                .stripeUserId(stripeUserId)
                .stripeAccessToken(stripeAccessToken)
                .stripeRefreshToken(stripeRefreshToken)
                .tokenExpiresAt(tokenExpiresAt)
                .type(UserType.CUSTOMER)
                .build();

        // Then
        assertNotNull(customer);
        assertEquals("test@example.com", customer.getEmail());
        assertEquals("hashedPassword", customer.getHashedPassword());
        assertEquals("stripeUserId", customer.getStripeUserId());
        assertEquals("accessToken", customer.getStripeAccessToken());
        assertEquals("refreshToken", customer.getStripeRefreshToken());
        assertEquals(tokenExpiresAt, customer.getTokenExpiresAt());
        assertEquals(UserType.CUSTOMER, customer.getType());
    }

    @Test
    void testNoArgsConstructor() {
        // Given & When
        Customer customer = new Customer();

        // Then
        assertNotNull(customer);
        assertEquals(UserType.CUSTOMER, customer.getType());
    }

    @Test
    void testTokenNotExpired() {
        // Given
        Customer customer = Customer.builder()
                .tokenExpiresAt(Instant.now().plusSeconds(3600))
                .build();

        // When
        boolean isTokenExpired = customer.isTokenExpired();

        // Then
        assertFalse(isTokenExpired);
    }

    @Test
    void testTokenExpired() {
        // Given
        Customer customer = Customer.builder()
                .tokenExpiresAt(Instant.now().minusSeconds(3600))
                .build();

        // When
        boolean isTokenExpired = customer.isTokenExpired();

        // Then
        assertTrue(isTokenExpired);
    }

    @Test
    void testTokenNotExpiredWhenExpirationUndefined() {
        // Given a customer without a token expiration date
        Customer customer = new Customer();

        // When / Then — must not throw and report not expired
        assertFalse(customer.isTokenExpired());
    }

    @Test
    void testUpdateStripeToken() {
        // Given
        Customer customer = new Customer();

        // When
        customer.updateStripeToken("newAccessToken", "newRefreshToken", 3600L);

        // Then
        assertEquals("newAccessToken", customer.getStripeAccessToken());
        assertEquals("newRefreshToken", customer.getStripeRefreshToken());
        assertFalse(customer.isTokenExpired());
    }

    @Test
    void testUpdateStripeTokenNullAccessToken() {
        // Given
        Customer customer = new Customer();

        // When & Then
        assertThrows(BusinessRuleViolation.class, () ->
                customer.updateStripeToken(null, "refreshToken", 3600L));
    }

    @Test
    void testUpdateStripeTokenNullRefreshToken() {
        // Given
        Customer customer = new Customer();

        // When & Then
        assertThrows(BusinessRuleViolation.class, () ->
                customer.updateStripeToken("accessToken", null, 3600L));
    }

    @Test
    void testUpdateStripeTokenNullExpiresIn() {
        // Given
        Customer customer = new Customer();

        // When & Then
        assertThrows(BusinessRuleViolation.class, () ->
                customer.updateStripeToken("accessToken", "refreshToken", null));
    }

    @Test
    void testDefaultRole() {
        // Given
        Customer customer = new Customer();

        // When
        UserType role = customer.getType();

        // Then
        assertEquals(UserType.CUSTOMER, role);
    }
}