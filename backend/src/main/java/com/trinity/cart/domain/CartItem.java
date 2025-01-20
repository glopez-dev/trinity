package com.trinity.cart.domain;

import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
//@AllArgsConstructor
@Entity
public class CartItem {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID productId;
    private String productName;
    private int quantity;

    private BigDecimal unitPrice;

    private BigDecimal totalPrice;

    public CartItem(UUID productId, String productName, BigDecimal unitPrice, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        calculateTotalPrice();
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
