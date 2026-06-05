package com.trinity.cart.service;

import com.trinity.cart.domain.Cart;
import com.trinity.cart.domain.port.CartRepositoryPort;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * In-memory fake of the domain port for fast, framework-free service tests.
 * Stores domain aggregates directly — no JPA entity, no mapper.
 */
class InMemoryCartRepositoryPort implements CartRepositoryPort {

    private final Map<UUID, Cart> byCustomerId = new HashMap<>();

    @Override
    public Optional<Cart> findByCustomerId(UUID customerId) {
        return Optional.ofNullable(byCustomerId.get(customerId));
    }

    @Override
    public boolean existsByCustomerId(UUID customerId) {
        return byCustomerId.containsKey(customerId);
    }

    @Override
    public void save(Cart cart) {
        byCustomerId.put(cart.getCustomerId(), cart);
    }

    @Override
    public void deleteByCustomerId(UUID customerId) {
        byCustomerId.remove(customerId);
    }
}
