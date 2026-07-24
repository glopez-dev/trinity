package com.trinity.cart.infrastructure.persistence.adapter;

import com.trinity.cart.domain.model.Cart;
import com.trinity.cart.domain.port.CartRepositoryPort;
import com.trinity.cart.infrastructure.persistence.mapper.CartPersistenceMapper;
import com.trinity.cart.infrastructure.persistence.entity.CartEntity;
import com.trinity.cart.infrastructure.persistence.repository.JpaCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA implementation of {@link CartRepositoryPort}. Confines Spring Data and the
 * persistence mapper here, so the application service only sees domain types.
 */
@Component
@RequiredArgsConstructor
public class CartRepositoryAdapter implements CartRepositoryPort {

    private final JpaCartRepository jpaCartRepository;
    private final CartPersistenceMapper cartPersistenceMapper;

    @Override
    public Optional<Cart> findByCustomerId(UUID customerId) {
        return jpaCartRepository.findByCustomerId(customerId)
                .map(cartPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByCustomerId(UUID customerId) {
        return jpaCartRepository.existsByCustomerId(customerId);
    }

    @Override
    public void save(Cart cart) {
        // Re-resolve the managed entity so the in-place update triggers
        // orphanRemoval on dropped lines; insert a fresh one otherwise.
        Optional<CartEntity> managed = jpaCartRepository.findByCustomerId(cart.getCustomerId());
        if (managed.isPresent()) {
            cartPersistenceMapper.updateEntity(managed.get(), cart);
            jpaCartRepository.save(managed.get());
        } else {
            jpaCartRepository.save(cartPersistenceMapper.toEntity(cart));
        }
    }

    @Override
    public void deleteByCustomerId(UUID customerId) {
        jpaCartRepository.deleteByCustomerId(customerId);
    }
}
