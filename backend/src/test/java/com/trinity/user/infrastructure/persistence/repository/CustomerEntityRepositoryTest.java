package com.trinity.user.infrastructure.persistence.repository;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.trinity.user.domain.model.UserStatus;
import com.trinity.user.domain.model.UserType;
import com.trinity.user.infrastructure.persistence.entity.CustomerJpaEntity;

/**
 * Boots Hibernate against the frozen V1 schema. The anemic entity has no
 * @Builder.Default, so type is supplied explicitly; status persisted as smallint
 * (ORDINAL) gates the {@code status smallint check(0..4)} column.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CustomerEntityRepositoryTest {

    @Autowired
    private JpaCustomerRepository customerRepository;

    private CustomerJpaEntity customer;

    @BeforeEach
    public void setUp() {
        customer = CustomerJpaEntity.builder()
                .email("test@example.com")
                .hashedPassword("superSecretPassword")
                .lastLoginAt(Instant.now())
                .status(UserStatus.ACTIVE)
                .type(UserType.CUSTOMER)
                .firstName("John")
                .lastName("Doe")
                .build();

        customerRepository.save(customer);
    }

    @Test
    void testFindByEmail() {
        // Given
        String email = "test@example.com";

        // When
        Optional<CustomerJpaEntity> foundCustomer = customerRepository.findByEmail(email);

        // Then
        assertThat(foundCustomer).isPresent();
        assertThat(foundCustomer.get().getEmail()).isEqualTo(email);
        assertThat(foundCustomer.get().getType()).isEqualTo(UserType.CUSTOMER);
    }
}
