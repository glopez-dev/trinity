package com.trinity.cart.mapper;

import com.trinity.cart.domain.Cart;
import com.trinity.cart.domain.CartItem;
import com.trinity.cart.model.CartEntity;
import com.trinity.cart.model.CartItemEntity;
import com.trinity.cart.model.MoneyEmbeddable;
import com.trinity.common.domain.vo.Money;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Hand-written conversion boundary between the rich cart domain aggregate and
 * its anemic JPA entity. Hand-written (not MapStruct) because the conversion has
 * to drive the validating CartItem builder, the Money static factory, and the
 * bidirectional back-reference / id-preserving update-in-place — all of which
 * fight code generation.
 */
@Component
public class CartPersistenceMapper {

    /** Domain -> new detached entity (used on first persist). */
    public CartEntity toEntity(Cart cart) {
        CartEntity entity = CartEntity.builder()
                .customerId(cart.getCustomerId())
                .status(cart.getStatus())
                .totalAmount(toEmbeddable(cart.getTotalAmount()))
                .build();
        cart.getItems().forEach(item -> entity.getItems().add(toItemEntity(item, entity)));
        return entity;
    }

    /** Entity -> domain aggregate, bypassing recompute so persisted total/status pass through. */
    public Cart toDomain(CartEntity entity) {
        Set<CartItem> items = new HashSet<>();
        entity.getItems().forEach(e -> items.add(toDomainItem(e)));
        return new Cart(entity.getCustomerId(), items, entity.getStatus(), toMoney(entity.getTotalAmount()));
    }

    private Money toMoney(MoneyEmbeddable embeddable) {
        // Hibernate maps an all-null embeddable to a null reference; fall back to
        // zero so a legacy/ddl-update row never breaks the read path.
        if (embeddable == null || embeddable.getAmount() == null || embeddable.getCurrency() == null) {
            return Money.zero("USD");
        }
        return Money.of(embeddable.getAmount(), embeddable.getCurrency());
    }

    /**
     * Syncs a managed entity from a mutated domain aggregate. The items List is
     * mutated IN PLACE (so JPA orphanRemoval deletes dropped lines) and the
     * surrogate id is preserved.
     */
    public void updateEntity(CartEntity target, Cart source) {
        target.setStatus(source.getStatus());
        target.setTotalAmount(toEmbeddable(source.getTotalAmount()));
        target.getItems().clear();
        source.getItems().forEach(item -> target.getItems().add(toItemEntity(item, target)));
    }

    private MoneyEmbeddable toEmbeddable(Money money) {
        return new MoneyEmbeddable(money.amount(), money.currency());
    }

    private CartItemEntity toItemEntity(CartItem item, CartEntity parent) {
        CartItemEntity entity = CartItemEntity.builder()
                .productId(item.getProductId())
                .productName(item.getProductName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .totalPrice(item.getTotalPrice())
                .build();
        entity.setCart(parent);
        return entity;
    }

    private CartItem toDomainItem(CartItemEntity entity) {
        return CartItem.builder()
                .productId(entity.getProductId())
                .productName(entity.getProductName())
                .unitPrice(entity.getUnitPrice())
                .quantity(entity.getQuantity())
                .build();
    }
}
