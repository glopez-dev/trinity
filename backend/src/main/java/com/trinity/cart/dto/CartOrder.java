package com.trinity.cart.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class CartOrder {

    private UUID customerId;
    private List<CartItemOrder> items;
    private BigDecimal totalAmount;
    private String currency;
}