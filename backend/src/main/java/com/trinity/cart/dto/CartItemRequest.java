package com.trinity.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;


@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemRequest {
    @NotEmpty(message = "Product ID cannot be empty")
    private UUID productId;

    @NotEmpty(message = "Product name cannot be empty")
    private String productName;

    @NotEmpty(message = "Product description cannot be empty")
    private BigDecimal unitPrice;

    @NotEmpty(message = "Quantity cannot be empty")
    private int quantity;

    private BigDecimal totalPrice;
}



