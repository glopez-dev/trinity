package com.trinity.user.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


class CustomerTest {

    @Test
    void testCustomerBuilder() {
        // Given
        String email = "test@example.com";
        String hashedPassword = "hashedPassword";

        // When
        Customer customer = Customer.builder()
                .email(email)
                .hashedPassword(hashedPassword)
                .type(UserType.CUSTOMER)
                .build();

        // Then
        assertNotNull(customer);
        assertEquals("test@example.com", customer.getEmail());
        assertEquals("hashedPassword", customer.getHashedPassword());
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
    void testDefaultRole() {
        // Given
        Customer customer = new Customer();

        // When
        UserType role = customer.getType();

        // Then
        assertEquals(UserType.CUSTOMER, role);
    }
}
