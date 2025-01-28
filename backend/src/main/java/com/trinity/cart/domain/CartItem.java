package com.trinity.cart.domain;

import lombok.Data;
//import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
//@AllArgsConstructor
public class CartItem {
    private UUID productId;
    private String productName;
    private int quantity;

    private BigDecimal unitPrice;

    private BigDecimal totalPrice;

    public CartItem(UUID productId, String productName, BigDecimal unitPrice, int quantity) {
        if (unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Unit price must be greater than zero.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        calculateTotalPrice();
    }

    public void updateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalStateException("Quantity cannot be less than or equal to zero.");
        }
        this.quantity = quantity;
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
