package com.trinity.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CartRequest {
    @NotEmpty(message = "Customer ID cannot be empty")
    private UUID customerId;

    private Set<CartItemRequest> items;
    private BigDecimal totalAmount;
    private String currency;
}