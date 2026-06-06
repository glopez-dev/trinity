package com.trinity.user.infrastructure.persistence.adapter;

import com.trinity.user.domain.model.Customer;
import com.trinity.user.domain.model.UserStatus;
import com.trinity.user.domain.model.UserType;
import com.trinity.user.domain.port.CustomerRepositoryPort;
import com.trinity.user.infrastructure.persistence.mapper.CustomerPersistenceMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Round-trips the customer aggregate through the port + hand-written mapper.
 * Pins the contracts the unit tests cannot reach: the generated id is populated on
 * save (the JWT depends on it), and the NOT-NULL ordinal columns (status/type) are
 * copied by the mapper even when the caller relies on the domain default.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import({CustomerRepositoryAdapter.class, CustomerPersistenceMapper.class})
class CustomerRepositoryAdapterTest {

    @Autowired
    private CustomerRepositoryPort port;

    @Test
    void save_populatesGeneratedId_andDefaultsAreCopied() {
        // Build WITHOUT setting id or status -> relies on the domain default (ACTIVE).
        Customer saved = port.save(Customer.builder()
                .email("alice@example.com")
                .hashedPassword("hash")
                .build());

        assertThat(saved.getId()).isNotNull();          // id-on-save contract (JWT)
        assertThat(saved.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(saved.getType()).isEqualTo(UserType.CUSTOMER);
        assertThat(saved.getVersion()).isNotNull();     // optimistic-lock column populated
    }

    @Test
    void save_thenFindByEmail_roundTripsDomain() {
        port.save(Customer.builder()
                .email("bob@example.com")
                .hashedPassword("hash")
                .stripeUserId("stripe-1")
                .build());

        assertThat(port.findByEmail("bob@example.com"))
                .isPresent()
                .get()
                .satisfies(c -> {
                    assertThat(c.getStripeUserId()).isEqualTo("stripe-1");
                    assertThat(c.getStatus()).isEqualTo(UserStatus.ACTIVE);
                    assertThat(c.getType()).isEqualTo(UserType.CUSTOMER);
                });
    }

    @Test
    void save_existingCustomer_keepsSameId() {
        Customer created = port.save(Customer.builder()
                .email("carol@example.com")
                .hashedPassword("hash")
                .build());
        UUID id = created.getId();

        Customer reloaded = port.findById(id).orElseThrow();
        reloaded.setFirstName("Carol");
        Customer updated = port.save(reloaded);

        assertThat(updated.getId()).isEqualTo(id);
        assertThat(port.findById(id).orElseThrow().getFirstName()).isEqualTo("Carol");
        assertThat(port.findAll()).hasSize(1);
    }

    @Test
    void existsByEmail_and_delete() {
        Customer created = port.save(Customer.builder()
                .email("dan@example.com")
                .hashedPassword("hash")
                .build());

        assertThat(port.existsByEmail("dan@example.com")).isTrue();

        port.delete(created);

        assertThat(port.findById(created.getId())).isEmpty();
    }
}
