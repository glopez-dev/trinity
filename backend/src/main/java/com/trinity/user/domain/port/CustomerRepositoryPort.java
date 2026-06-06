package com.trinity.user.domain.port;

import com.trinity.user.domain.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Outbound persistence port for the customer aggregate. Speaks domain types only.
 */
public interface CustomerRepositoryPort {

    Customer save(Customer customer);

    Optional<Customer> findById(UUID id);

    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Customer> findAll();

    void delete(Customer customer);
}
