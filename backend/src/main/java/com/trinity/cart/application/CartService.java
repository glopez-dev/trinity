package com.trinity.cart.application;

import com.trinity.cart.domain.model.Cart;
import com.trinity.cart.domain.model.CartItem;
import com.trinity.cart.domain.port.CartRepositoryPort;
import com.trinity.cart.interfaces.rest.dto.CartItemRequest;
import com.trinity.cart.interfaces.rest.dto.CartRequest;
import com.trinity.common.domain.exception.BusinessRuleViolation;
import com.trinity.common.domain.exception.NotFoundException;
import com.trinity.common.domain.vo.Money;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
    public CartRequest getCart(UUID customerId) {
        Cart cart = loadCart(customerId);
        return CartRequest.builder()
                .customerId(cart.getCustomerId())
                .items(setCartItemToSetCartItemRequest(cart.getItems()))
                .totalAmount(cart.getTotalAmount().amount())
                .currency(cart.getTotalAmount().currency())
                .build();
    }

    @Transactional
    public void addItemToCart(UUID customerId, CartItemRequest cartItem) {
        mutate(customerId, cart -> cart.addItem(toCartItem(cartItem)));
    }

    @Transactional
    public void removeItemFromCart(UUID customerId, CartItemRequest cartItem) {
        mutate(customerId, cart -> cart.removeItem(toCartItem(cartItem)));
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

    private CartItem toCartItem(CartItemRequest cartItemRequest) {
        return CartItem.builder()
                .productId(cartItemRequest.getProductId())
                .productName(cartItemRequest.getProductName())
                .quantity(cartItemRequest.getQuantity())
                .unitPrice(cartItemRequest.getUnitPrice())
                .build();
    }
}
