package com.trinity.cart.domain.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Data
public class CartItem {

    private static final int MONEY_SCALE = 2;

    private UUID productId;
    private String productName;
    private int quantity;
    private BigDecimal unitPrice;

    private BigDecimal totalPrice;

    @Builder
    public CartItem(UUID productId, String productName, BigDecimal unitPrice, int quantity) {
        if (unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Unit price must be greater than zero.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        // Normalize to the scale the money columns persist at, so the total stays
        // reproducible across a reload (no sub-cent drift).
        this.unitPrice = unitPrice.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
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
