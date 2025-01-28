package com.trinity.cart.service;

import com.trinity.cart.domain.Cart;
import com.trinity.cart.domain.CartItem;
import com.trinity.cart.domain.Money;
import com.trinity.cart.dto.CartItemRequest;
import com.trinity.cart.dto.CartRequest;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Service
public class CartService {

    // In-memory storage for carts
    private final Map<UUID, Cart> carts = new ConcurrentHashMap<>();

    public void createCart(UUID customerId) {
        Cart cart = Cart.builder()
                .customerId(customerId)
                .build();
        carts.put(customerId, cart);
    }
    
    public CartRequest getCart(UUID customerId) {
        Cart cart = getCartForCustomer(customerId);
        return CartRequest.builder()
                .customerId(cart.getCustomerId())
                .items(setCartItemToSetCartItemRequest(cart.getItems()))
                .totalAmount(cart.getTotalAmount().getAmount())
                .currency(cart.getTotalAmount().getCurrency())
                .build();
    }

    private Set<CartItemRequest> setCartItemToSetCartItemRequest(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::cartItemtoCartItemRequest)
                .collect(Collectors.toSet());
    }

    private CartItemRequest cartItemtoCartItemRequest(CartItem cartItem) {
        return CartItemRequest.builder()
                .productId(cartItem.getProductId())
                .productName(cartItem.getProductName())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getUnitPrice())
                .build();
    }

    private Cart getCartForCustomer(UUID customerId) {
        Cart cart = carts.get(customerId);
        if (cart == null) {
            throw new IllegalArgumentException("Cart not found for customer: " + customerId);
        }
        return cart;
    }

    private CartItem toCartItem(CartItemRequest cartItemRequest) {
        return CartItem.builder()
                .productId(cartItemRequest.getProductId())
                .productName(cartItemRequest.getProductName())
                .quantity(cartItemRequest.getQuantity())
                .unitPrice(cartItemRequest.getUnitPrice())
                .build();
    }

    public void addItemToCart(UUID customerId, CartItemRequest cartItem) {
        Cart cart = getCartForCustomer(customerId);
        cart.addItem(toCartItem(cartItem));
    }
    
    public void removeItemFromCart(UUID customerId, CartItemRequest cartItem) {
        Cart cart = getCartForCustomer(customerId);
        cart.removeItem(toCartItem(cartItem));
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
        getCartForCustomer(customerId);
        carts.remove(customerId);
    }

    public void cancelCart(UUID customerId) {
        Cart cart = getCartForCustomer(customerId);
        cart.cancel();
    }

    public Money getTotalAmount(UUID customerId) {
        Cart cart = getCartForCustomer(customerId);
        return cart.getTotalAmount();
    }
}
