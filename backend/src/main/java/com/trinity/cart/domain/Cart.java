package com.trinity.cart.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NonNull
    @Column(nullable = false)
    private UUID customerId;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<CartItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private CartStatus status = CartStatus.ACTIVE;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @Embedded
    private Money totalAmount = new Money(BigDecimal.ZERO, "USD");

    public void addItem(UUID productId, String productName, BigDecimal unitPrice, int quantity) {
        if (status != CartStatus.ACTIVE) {
            throw new IllegalStateException("Cannot modify a cart that is not ACTIVE.");
        }

        CartItem existingItem = findCartItem(productId);
        if (existingItem != null) {
            existingItem.updateQuantity(existingItem.getQuantity() + quantity);
        } else {
            items.add(new CartItem(productId, productName, unitPrice, quantity));
        }
        calculateTotal();
    }

    public void removeItem(UUID productId) {
        items.removeIf(item -> item.getProductId().equals(productId));
        calculateTotal();
    }

    public void validate() {
        if (items.isEmpty()) {
            throw new IllegalStateException("Cannot validate an empty cart.");
        }
        this.status = CartStatus.VALIDATED;
    }

    public void cancel() {
        this.status = CartStatus.CANCELLED;
    }

    private CartItem findCartItem(UUID productId) {
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
