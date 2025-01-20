package com.trinity.user.infrastructure.persistence;

import com.trinity.user.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerJpaRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findByEmail(String email);
}
