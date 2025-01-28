package com.trinity.cart.controller;

import com.trinity.cart.dto.CartItemRequest;
import com.trinity.cart.dto.CartRequest;
import com.trinity.cart.service.CartService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/V1/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Create a new cart
    @PostMapping("/{customerId}")
    public ResponseEntity<Void> createCart(@PathVariable UUID customerId) {
        cartService.createCart(customerId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Retrieve a customer's cart
    @GetMapping("/{customerId}")
    public ResponseEntity<CartRequest> getCart(@PathVariable UUID customerId) {
        return ResponseEntity.ok(cartService.getCart(customerId));
    }

    // Add an item to the cart
    @PutMapping("/{customerId}/items") 
    public ResponseEntity<Void> addItemToCart(
        @PathVariable UUID customerId, @RequestBody CartItemRequest cartItem) {
        cartService.addItemToCart(customerId, cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Remove an item from the cart
    @DeleteMapping("/{customerId}/items")
    public ResponseEntity<Void> removeItemFromCart(
            @PathVariable UUID customerId, @RequestBody CartItemRequest cartItem) {
        cartService.removeItemFromCart(customerId, cartItem);
        return ResponseEntity.ok().build();
    }

    // Validate the cart (checkout)
    @PostMapping("/{customerId}/validate")
    public ResponseEntity<Void> validateCart(@PathVariable UUID customerId) {
        cartService.validateCart(customerId);
        return ResponseEntity.ok().build();
    }

    // Remove a customer's cart
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> removeCart(@PathVariable UUID customerId) {
        cartService.removeCart(customerId);
        return ResponseEntity.ok().build();
    }

}
