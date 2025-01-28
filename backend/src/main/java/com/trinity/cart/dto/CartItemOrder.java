package com.trinity.cart.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;


@Data
public class CartItemOrder {
    private UUID productId;
    private String productName;
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal totalPrice;
}


