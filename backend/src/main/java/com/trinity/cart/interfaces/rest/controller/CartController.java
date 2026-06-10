package com.trinity.cart.interfaces.rest.controller;

import com.trinity.cart.interfaces.rest.dto.CartItemRequest;
import com.trinity.cart.interfaces.rest.dto.CartResponse;
import com.trinity.cart.interfaces.rest.mapper.CartApiMapper;
import com.trinity.cart.application.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Customer cart management APIs")
public class CartController {

    private final CartService cartService;
    private final CartApiMapper cartApiMapper;

    @Operation(summary = "Create a cart", description = "Creates an empty cart for the customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cart created"),
        @ApiResponse(responseCode = "409", description = "A cart already exists for this customer")
    })
    @PostMapping("/{customerId}")
    public ResponseEntity<Void> createCart(@PathVariable UUID customerId) {
        cartService.createCart(customerId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Get a cart", description = "Returns the customer's cart with its lines and total")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart found"),
        @ApiResponse(responseCode = "404", description = "No cart for this customer")
    })
    @GetMapping("/{customerId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable UUID customerId) {
        return ResponseEntity.ok(cartApiMapper.toResponse(cartService.getCart(customerId)));
    }

    @Operation(summary = "Add an item",
            description = "Adds a quantity of a product; name and unit price are resolved server-side from the catalogue")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Item added"),
        @ApiResponse(responseCode = "404", description = "Cart or product not found")
    })
    @PutMapping("/{customerId}/items")
    public ResponseEntity<Void> addItemToCart(
        @PathVariable UUID customerId, @Valid @RequestBody CartItemRequest cartItem) {
        cartService.addItemToCart(customerId, cartItem.getProductId(), cartItem.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Remove an item", description = "Removes a quantity of a product from the cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item removed"),
        @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @DeleteMapping("/{customerId}/items")
    public ResponseEntity<Void> removeItemFromCart(
            @PathVariable UUID customerId, @Valid @RequestBody CartItemRequest cartItem) {
        cartService.removeItemFromCart(customerId, cartItem.getProductId(), cartItem.getQuantity());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Validate the cart",
            description = "Validates the cart, publishes the CartValidatedEvent (stock deduction) and deletes the cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart validated"),
        @ApiResponse(responseCode = "404", description = "Cart not found"),
        @ApiResponse(responseCode = "409", description = "Cart is empty")
    })
    @PostMapping("/{customerId}/validate")
    public ResponseEntity<Void> validateCart(@PathVariable UUID customerId) {
        cartService.validateCart(customerId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Cancel the cart", description = "Empties the cart without deleting it")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart emptied"),
        @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @PostMapping("/{customerId}/cancel")
    public ResponseEntity<Void> cancelCart(@PathVariable UUID customerId) {
        cartService.cancelCart(customerId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete the cart", description = "Deletes the customer's cart entirely")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart deleted"),
        @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> removeCart(@PathVariable UUID customerId) {
        cartService.removeCart(customerId);
        return ResponseEntity.ok().build();
    }

}
