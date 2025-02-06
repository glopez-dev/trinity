package com.trinity.cart.domain;

import com.trinity.cart.constant.CartStatus;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@Builder
public class Cart {
    @NonNull
    private UUID customerId;

    @Builder.Default
    private Set<CartItem> items = new HashSet<>(); // Use Set instead of List

    // Cart status
    @Builder.Default
    private CartStatus status = CartStatus.CREATED;

    @Builder.Default
    private Money totalAmount = new Money(BigDecimal.ZERO, "USD");

    public Cart(UUID customerId, Set<CartItem> items, CartStatus status, Money totalAmount) {
        this.customerId = customerId;
        this.items = items != null ? items : new HashSet<>();
        this.status = status != null ? status : CartStatus.CREATED;
        this.totalAmount = totalAmount != null ? totalAmount : new Money(BigDecimal.ZERO, "USD");
    }

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

    public void removeItem(CartItem item) {
        CartItem existingItem = findItem(item.getProductId());
        if (existingItem == null) {
            throw new IllegalStateException("Item not found in the cart.");
        }
        if (item.getQuantity() > existingItem.getQuantity()) {
            throw new IllegalStateException("Quantity to remove is greater than the existing quantity.");
        }
        if (item.getQuantity() == existingItem.getQuantity()) {
            items.remove(existingItem);
        } else {
            existingItem.updateQuantity(existingItem.getQuantity() - item.getQuantity());
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
