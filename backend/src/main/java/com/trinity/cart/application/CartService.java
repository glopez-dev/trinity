package com.trinity.cart.application;

import com.trinity.cart.domain.model.Cart;
import com.trinity.cart.domain.model.CartItem;
import com.trinity.cart.domain.port.CartRepositoryPort;
import com.trinity.common.domain.exception.BusinessRuleViolation;
import com.trinity.common.domain.exception.NotFoundException;
import com.trinity.common.domain.vo.Money;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepositoryPort cartRepository;

    @Transactional
    public void createCart(UUID customerId) {
        if (cartRepository.existsByCustomerId(customerId)) {
            throw new BusinessRuleViolation("A cart already exists for customer: " + customerId);
        }
        cartRepository.save(Cart.builder()
                .customerId(customerId)
                .build());
    }

    @Transactional(readOnly = true)
    public Cart getCart(UUID customerId) {
        return loadCart(customerId);
    }

    @Transactional
    public void addItemToCart(UUID customerId, CartItem item) {
        mutate(customerId, cart -> cart.addItem(item));
    }

    @Transactional
    public void removeItemFromCart(UUID customerId, CartItem item) {
        mutate(customerId, cart -> cart.removeItem(item));
    }

    @Transactional
    public void cancelCart(UUID customerId) {
        mutate(customerId, Cart::cancel);
    }

    @Transactional
    public void validateCart(UUID customerId) {
        Cart cart = loadCart(customerId);
        cart.validate();
        // TODO: notify the payment service before clearing the cart
        cartRepository.deleteByCustomerId(customerId);
    }

    @Transactional
    public void removeCart(UUID customerId) {
        loadCart(customerId);
        cartRepository.deleteByCustomerId(customerId);
    }

    @Transactional(readOnly = true)
    public Money getTotalAmount(UUID customerId) {
        return loadCart(customerId).getTotalAmount();
    }

    /** Load the domain aggregate, apply a domain mutation, persist it back. */
    private void mutate(UUID customerId, Consumer<Cart> mutation) {
        Cart cart = loadCart(customerId);
        mutation.accept(cart);
        cartRepository.save(cart);
    }

    private Cart loadCart(UUID customerId) {
        return cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new NotFoundException("Cart not found for customer: " + customerId));
    }
}
