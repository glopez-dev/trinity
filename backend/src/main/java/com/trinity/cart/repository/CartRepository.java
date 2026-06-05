package com.trinity.cart.repository;

import com.trinity.cart.model.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, UUID> {

    Optional<CartEntity> findByCustomerId(UUID customerId);

    boolean existsByCustomerId(UUID customerId);

    void deleteByCustomerId(UUID customerId);
}
