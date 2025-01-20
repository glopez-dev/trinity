package com.trinity.cart.controller;

import com.trinity.cart.domain.Cart;
import com.trinity.cart.domain.Money;
import com.trinity.cart.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<Cart> createCart(@RequestParam UUID customerId) {
        Cart cart = cartService.createCart(customerId);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable UUID cartId) {
        return ResponseEntity.ok(cartService.getCart(cartId));
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<Void> addItemToCart(
            @PathVariable UUID cartId,
            @RequestParam UUID productId,
            @RequestParam String productName,
            @RequestParam BigDecimal unitPrice,
            @RequestParam int quantity
    ) {
        cartService.addItemToCart(cartId, productId, productName, unitPrice, quantity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable UUID cartId, @PathVariable UUID productId) {
        cartService.removeItemFromCart(cartId, productId);
        return ResponseEntity.ok().build();
    }
}
