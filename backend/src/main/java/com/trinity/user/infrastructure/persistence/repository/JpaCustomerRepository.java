package com.trinity.user.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trinity.user.infrastructure.persistence.entity.CustomerJpaEntity;

public interface JpaCustomerRepository extends JpaRepository<CustomerJpaEntity, UUID> {

    Optional<CustomerJpaEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
