package com.trinity.cart.service;

import com.trinity.cart.domain.Cart;
import com.trinity.cart.domain.CartItem;
import com.trinity.cart.dto.CartItemRequest;
import com.trinity.cart.dto.CartRequest;
import com.trinity.cart.mapper.CartPersistenceMapper;
import com.trinity.cart.model.CartEntity;
import com.trinity.cart.repository.CartRepository;
import com.trinity.common.domain.exception.BusinessRuleViolation;
import com.trinity.common.domain.exception.NotFoundException;
import com.trinity.common.domain.vo.Money;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartPersistenceMapper cartPersistenceMapper;

    @Transactional
    public void createCart(UUID customerId) {
        if (cartRepository.existsByCustomerId(customerId)) {
            throw new BusinessRuleViolation("A cart already exists for customer: " + customerId);
        }
        Cart cart = Cart.builder()
                .customerId(customerId)
                .build();
        cartRepository.save(cartPersistenceMapper.toEntity(cart));
    }

    @Transactional(readOnly = true)
    public CartRequest getCart(UUID customerId) {
        Cart cart = loadDomainCart(customerId);
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
        CartEntity entity = loadEntity(customerId);
        Cart cart = cartPersistenceMapper.toDomain(entity);
        cart.validate();
        // TODO: notify the payment service before clearing the cart
        cartRepository.deleteByCustomerId(customerId);
    }

    @Transactional
    public void removeCart(UUID customerId) {
        loadEntity(customerId);
        cartRepository.deleteByCustomerId(customerId);
    }

    @Transactional(readOnly = true)
    public Money getTotalAmount(UUID customerId) {
        return loadDomainCart(customerId).getTotalAmount();
    }

    /**
     * Load-mutate-save: the domain mutates in place and is detached, so the
     * managed entity must be updated and saved explicitly (no dirty checking).
     */
    private void mutate(UUID customerId, java.util.function.Consumer<Cart> mutation) {
        CartEntity entity = loadEntity(customerId);
        Cart cart = cartPersistenceMapper.toDomain(entity);
        mutation.accept(cart);
        cartPersistenceMapper.updateEntity(entity, cart);
        cartRepository.save(entity);
    }

    private CartEntity loadEntity(UUID customerId) {
        return cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new NotFoundException("Cart not found for customer: " + customerId));
    }

    private Cart loadDomainCart(UUID customerId) {
        return cartPersistenceMapper.toDomain(loadEntity(customerId));
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
