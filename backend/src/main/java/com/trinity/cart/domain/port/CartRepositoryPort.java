package com.trinity.cart.domain.port;

import com.trinity.cart.domain.model.Cart;

import java.util.Optional;
import java.util.UUID;

/**
 * Outbound persistence port for the cart aggregate, expressed purely in domain
 * terms (never a JPA entity). Implemented by an infrastructure adapter so the
 * application service depends on this contract, not on Spring Data.
 */
public interface CartRepositoryPort {

    Optional<Cart> findByCustomerId(UUID customerId);

    boolean existsByCustomerId(UUID customerId);

    /**
     * Persists the aggregate. For an existing cart (same customerId) the adapter
     * updates the managed entity in place so JPA orphanRemoval deletes dropped
     * lines; otherwise it inserts a new one.
     */
    void save(Cart cart);

    void deleteByCustomerId(UUID customerId);
}
