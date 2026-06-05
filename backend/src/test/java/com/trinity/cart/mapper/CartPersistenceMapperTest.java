package com.trinity.cart.mapper;

import com.trinity.cart.constant.CartStatus;
import com.trinity.cart.domain.Cart;
import com.trinity.cart.domain.CartItem;
import com.trinity.cart.model.CartEntity;
import com.trinity.cart.model.CartItemEntity;
import com.trinity.cart.model.MoneyEmbeddable;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CartPersistenceMapperTest {

    private final CartPersistenceMapper mapper = new CartPersistenceMapper();

    private Cart domainCartWithTwoItems(UUID customerId) {
        Cart cart = new Cart(customerId);
        cart.addItem(new CartItem(UUID.randomUUID(), "Coffee", new BigDecimal("10.00"), 2));
        cart.addItem(new CartItem(UUID.randomUUID(), "Tea", new BigDecimal("5.00"), 1));
        return cart;
    }

    @Test
    void toEntity_copiesFieldsAndWiresBackReferences() {
        UUID customerId = UUID.randomUUID();
        Cart cart = domainCartWithTwoItems(customerId);

        CartEntity entity = mapper.toEntity(cart);

        assertThat(entity.getCustomerId()).isEqualTo(customerId);
        assertThat(entity.getStatus()).isEqualTo(CartStatus.EDITED);
        assertThat(entity.getTotalAmount().getAmount()).isEqualByComparingTo("25.00");
        assertThat(entity.getTotalAmount().getCurrency()).isEqualTo("USD");
        assertThat(entity.getItems()).hasSize(2);
        // every child must point back to its parent (owning side of the FK)
        assertThat(entity.getItems()).allSatisfy(i -> assertThat(i.getCart()).isSameAs(entity));
    }

    @Test
    void toDomain_rebuildsAggregateWithValidatedItemsAndMoney() {
        CartEntity entity = CartEntity.builder()
                .customerId(UUID.randomUUID())
                .status(CartStatus.VALIDATED)
                .totalAmount(new MoneyEmbeddable(new BigDecimal("20.00"), "USD"))
                .items(new ArrayList<>())
                .build();
        CartItemEntity itemEntity = CartItemEntity.builder()
                .productId(UUID.randomUUID())
                .productName("Coffee")
                .quantity(2)
                .unitPrice(new BigDecimal("10.00"))
                .totalPrice(new BigDecimal("20.00"))
                .build();
        itemEntity.setCart(entity);
        entity.getItems().add(itemEntity);

        Cart cart = mapper.toDomain(entity);

        assertThat(cart.getCustomerId()).isEqualTo(entity.getCustomerId());
        assertThat(cart.getStatus()).isEqualTo(CartStatus.VALIDATED);
        assertThat(cart.getTotalAmount().amount()).isEqualByComparingTo("20.00");
        assertThat(cart.getTotalAmount().currency()).isEqualTo("USD");
        assertThat(cart.getItems()).hasSize(1);
        CartItem item = cart.getItems().iterator().next();
        assertThat(item.getQuantity()).isEqualTo(2);
        assertThat(item.getUnitPrice()).isEqualByComparingTo("10.00");
        assertThat(item.getTotalPrice()).isEqualByComparingTo("20.00");
    }

    @Test
    void toDomain_nullTotalAmount_fallsBackToZero() {
        // Hibernate materializes an all-null embeddable as a null reference;
        // toDomain must not NPE on a legacy/ddl-update row.
        CartEntity entity = CartEntity.builder()
                .customerId(UUID.randomUUID())
                .status(CartStatus.CREATED)
                .totalAmount(null)
                .items(new ArrayList<>())
                .build();

        Cart cart = mapper.toDomain(entity);

        assertThat(cart.getTotalAmount().amount()).isEqualByComparingTo("0.00");
        assertThat(cart.getTotalAmount().currency()).isEqualTo("USD");
    }

    @Test
    void updateEntity_preservesIdsAndRemovesDroppedLinesInPlace() {
        // existing managed entity with two items and assigned surrogate ids
        UUID customerId = UUID.randomUUID();
        CartEntity entity = mapper.toEntity(domainCartWithTwoItems(customerId));
        UUID entityId = UUID.randomUUID();
        entity.setId(entityId);
        entity.getItems().get(0).setId(UUID.randomUUID());
        entity.getItems().get(1).setId(UUID.randomUUID());
        var managedList = entity.getItems(); // keep the same List reference

        // domain now has only one of the two products
        Cart mutated = new Cart(customerId);
        mutated.addItem(new CartItem(
                entity.getItems().get(0).getProductId(), "Coffee", new BigDecimal("10.00"), 2));

        mapper.updateEntity(entity, mutated);

        // surrogate id is preserved, the managed List instance is mutated in place
        assertThat(entity.getId()).isEqualTo(entityId);
        assertThat(entity.getItems()).isSameAs(managedList);
        assertThat(entity.getItems()).hasSize(1);
        assertThat(entity.getItems().get(0).getCart()).isSameAs(entity);
        assertThat(entity.getStatus()).isEqualTo(mutated.getStatus());
        assertThat(entity.getTotalAmount().getAmount()).isEqualByComparingTo("20.00");
    }
}
