package com.trinity.cart.controller;

import com.trinity.cart.domain.Cart;
import com.trinity.cart.domain.CartItem;
import com.trinity.cart.domain.Money;
import com.trinity.cart.service.CartService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Create a new cart
    @PostMapping("/{customerId}")
    public ResponseEntity<Cart> createCart(@PathVariable UUID customerId) {
        return ResponseEntity.ok(cartService.createCart(customerId));
    }

    // Retrieve a customer's cart
    @GetMapping("/{customerId}")
    public ResponseEntity<Cart> getCart(@PathVariable UUID customerId) {
        return ResponseEntity.ok(cartService.getCart(customerId));
    }

    // Add an item to the cart
    @PostMapping("/{customerId}/items") 
    public ResponseEntity<Void> addItemToCart(
        @PathVariable UUID customerId, @RequestBody CartItem cartItem) {
        cartService.addItemToCart(customerId, cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Remove an item from the cart
    @DeleteMapping("/{customerId}/items/{productId}")
    public ResponseEntity<Void> removeItemFromCart(
            @PathVariable UUID customerId,
            @PathVariable UUID productId,
            @RequestParam int quantity) {
        cartService.removeItemFromCart(customerId, productId, quantity);
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

    // Get the total amount of the cart
    @GetMapping("/{customerId}/total")
    public ResponseEntity<Money> getTotalAmount(@PathVariable UUID customerId) {
        return ResponseEntity.ok(cartService.getTotalAmount(customerId));
    }
}
