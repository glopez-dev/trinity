package com.trinity.cart.service;

import com.trinity.cart.domain.Cart;
import com.trinity.cart.domain.CartItem;
import com.trinity.cart.domain.Money;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class CartService {

    // In-memory storage for carts
    private final Map<UUID, Cart> carts = new ConcurrentHashMap<>();

    public Cart createCart(UUID customerId) {
        Cart cart = new Cart(customerId);
        carts.put(customerId, cart);
        return cart;
    }

    public Cart getCart(UUID customerId) {
        return carts.getOrDefault(customerId, new Cart(customerId));
    }


    public void addItemToCart(UUID customerId, CartItem cartItem) {
        Cart cart = carts.computeIfAbsent(customerId, Cart::new);
        cart.addItem(cartItem);
    }


    public void removeItemFromCart(UUID customerId, UUID productId, int quantity) {
        Cart cart = carts.get(customerId);
        if (cart != null) {
            cart.removeItem(productId, quantity);
        }
    }

    public void validateCart(UUID customerId) {
        Cart cart = carts.get(customerId);
        if (cart != null) {
            cart.validate();
            /// send a message to the payment service
            carts.remove(customerId);
        } else {
            throw new IllegalArgumentException("Cart not found for customer: " + customerId);
        }
    }

    public void removeCart(UUID customerId) {
        Cart cart = carts.get(customerId);
        if (cart != null) {
            carts.remove(customerId);
        }
    }

    public void cancelCart(UUID customerId) {
        Cart cart = carts.get(customerId);
        if (cart != null) {
            cart.cancel();
        }
    }

    public Money getTotalAmount(UUID customerId) {
        Cart cart = carts.get(customerId);
        if (cart != null) {
            return cart.getTotalAmount();
        }
        return new Money(BigDecimal.ZERO, "USD");
    }
}
