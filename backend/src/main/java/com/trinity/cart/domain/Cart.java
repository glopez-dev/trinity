package com.trinity.cart.domain;

import com.trinity.cart.constant.CartStatus;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@RequiredArgsConstructor
public class Cart {
    @NonNull
    private UUID customerId;

    private List<CartItem> items = new ArrayList<>();

    //cart status
    private CartStatus status = CartStatus.CREATED;

    private Money totalAmount = new Money(BigDecimal.ZERO, "USD");

    public void addItem(CartItem item) {
        CartItem existingItem = findItem(item.getProductId());
        if (existingItem != null) {
            existingItem.updateQuantity(existingItem.getQuantity() + item.getQuantity());
        } else {
            items.add(item);
        }
        calculateTotal();
        this.status = CartStatus.EDITED;
    }

    public void removeItem(UUID productId, int quantity) {
        CartItem existingItem = findItem(productId);
        if (existingItem == null) {
            throw new IllegalStateException("Item not found in the cart.");
        }
        if (quantity > existingItem.getQuantity()) {
            throw new IllegalStateException("Quantity to remove is greater than the existing quantity.");
        }
        if (quantity == existingItem.getQuantity()) {
            items.remove(existingItem);
        } else {
            existingItem.updateQuantity(existingItem.getQuantity() - quantity);
        }
        calculateTotal();
        this.status = CartStatus.EDITED;
    }

    public void validate() {
        if (items.isEmpty()) {
            throw new IllegalStateException("Cannot validate an empty cart.");
        }
        this.status = CartStatus.VALIDATED;
    }

    public void cancel() {
        items.clear();
        calculateTotal();
        this.status = CartStatus.CANCELLED;
    }

    private CartItem findItem(UUID productId) {
        return items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    private void calculateTotal() {
        this.totalAmount = new Money(
                items.stream()
                        .map(CartItem::getTotalPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add),
                totalAmount.getCurrency()
        );
    }
}
