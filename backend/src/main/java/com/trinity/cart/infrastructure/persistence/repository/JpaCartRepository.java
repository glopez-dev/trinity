package com.trinity.cart.infrastructure.persistence.repository;

import com.trinity.cart.infrastructure.persistence.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaCartRepository extends JpaRepository<CartEntity, UUID> {

    Optional<CartEntity> findByCustomerId(UUID customerId);

    boolean existsByCustomerId(UUID customerId);

    void deleteByCustomerId(UUID customerId);
}
