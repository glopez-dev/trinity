package com.trinity.cart.interfaces.rest.controller;

import com.trinity.cart.interfaces.rest.dto.CartItemRequest;
import com.trinity.cart.interfaces.rest.dto.CartResponse;
import com.trinity.cart.interfaces.rest.mapper.CartApiMapper;
import com.trinity.cart.application.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartApiMapper cartApiMapper;

    // Create a new cart
    @PostMapping("/{customerId}")
    public ResponseEntity<Void> createCart(@PathVariable UUID customerId) {
        cartService.createCart(customerId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Retrieve a customer's cart
    @GetMapping("/{customerId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable UUID customerId) {
        return ResponseEntity.ok(cartApiMapper.toResponse(cartService.getCart(customerId)));
    }

    // Add an item to the cart
    @PutMapping("/{customerId}/items")
    public ResponseEntity<Void> addItemToCart(
        @PathVariable UUID customerId, @Valid @RequestBody CartItemRequest cartItem) {
        cartService.addItemToCart(customerId, cartItem.getProductId(), cartItem.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Remove an item from the cart
    @DeleteMapping("/{customerId}/items")
    public ResponseEntity<Void> removeItemFromCart(
            @PathVariable UUID customerId, @Valid @RequestBody CartItemRequest cartItem) {
        cartService.removeItemFromCart(customerId, cartItem.getProductId(), cartItem.getQuantity());
        return ResponseEntity.ok().build();
    }

    // Validate the cart (checkout)
    @PostMapping("/{customerId}/validate")
    public ResponseEntity<Void> validateCart(@PathVariable UUID customerId) {
        cartService.validateCart(customerId);
        return ResponseEntity.ok().build();
    }

    // Cancel the cart: empties it without destroying the aggregate
    @PostMapping("/{customerId}/cancel")
    public ResponseEntity<Void> cancelCart(@PathVariable UUID customerId) {
        cartService.cancelCart(customerId);
        return ResponseEntity.ok().build();
    }

    // Remove a customer's cart
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> removeCart(@PathVariable UUID customerId) {
        cartService.removeCart(customerId);
        return ResponseEntity.ok().build();
    }

}
