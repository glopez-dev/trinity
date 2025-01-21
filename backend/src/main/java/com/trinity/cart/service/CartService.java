package com.trinity.cart.service;

import com.trinity.cart.domain.Cart;
import com.trinity.cart.repository.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Transactional
    public Cart createCart(UUID customerId) {
        Cart cart = new Cart(customerId);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart getCart(UUID cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found."));
    }

    @Transactional
    public void addItemToCart(UUID cartId, UUID productId, String productName, BigDecimal unitPrice, int quantity) {
        Cart cart = getCart(cartId);
        cart.addItem(productId, productName, unitPrice, quantity);
        cartRepository.save(cart);
    }

    @Transactional
    public void removeItemFromCart(UUID cartId, UUID productId) {
        Cart cart = getCart(cartId);
        cart.removeItem(productId);
        cartRepository.save(cart);
    }
}
