package com.trinity.auth.repository;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.trinity.auth.model.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TestCustomerRepository {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    public void setUp() {
        customer = Customer.builder()
            .email("test@example.com")
            .hashedPassword("superSecretPassword")
            .paypalUserId("paypalUserId")
            .paypalAccessToken("accessToken")
            .paypalRefreshToken("refreshToken")
            .tokenExpiresAt(Instant.now().plusSeconds(3600))
            .build();
        customerRepository.save(customer);
    }

    @Test
    void testFindByEmail() {
        // Given
        String email = "test@example.com";

        // When
        Optional<Customer> foundCustomer = customerRepository.findByEmail(email);

        // Then
        assertThat(foundCustomer).isPresent();
        assertThat(foundCustomer.get().getEmail()).isEqualTo(email);
    }
}