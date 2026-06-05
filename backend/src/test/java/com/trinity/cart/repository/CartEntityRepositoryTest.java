package com.trinity.cart.repository;

import com.trinity.cart.constant.CartStatus;
import com.trinity.cart.model.CartEntity;
import com.trinity.cart.model.CartItemEntity;
import com.trinity.cart.model.MoneyEmbeddable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CartEntityRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private TestEntityManager entityManager;

    private CartEntity cartWith(UUID customerId, CartItemEntity... items) {
        CartEntity cart = CartEntity.builder()
                .customerId(customerId)
                .status(CartStatus.EDITED)
                .totalAmount(new MoneyEmbeddable(new BigDecimal("20.00"), "USD"))
                .items(new ArrayList<>())
                .build();
        for (CartItemEntity item : items) {
            item.setCart(cart);
            cart.getItems().add(item);
        }
        return cart;
    }

    private CartItemEntity item(String name, int quantity, String unitPrice) {
        return CartItemEntity.builder()
                .productId(UUID.randomUUID())
                .productName(name)
                .quantity(quantity)
                .unitPrice(new BigDecimal(unitPrice))
                .totalPrice(new BigDecimal(unitPrice).multiply(BigDecimal.valueOf(quantity)))
                .build();
    }

    @Test
    void save_persistsCartWithItemsAndStringStatus() {
        CartEntity saved = cartRepository.save(cartWith(UUID.randomUUID(), item("Coffee", 2, "10.00")));
        UUID id = saved.getId();
        entityManager.flush();
        entityManager.clear();

        CartEntity reloaded = cartRepository.findById(id).orElseThrow();
        assertThat(reloaded.getId()).isNotNull();
        assertThat(reloaded.getItems()).hasSize(1);
        assertThat(reloaded.getStatus()).isEqualTo(CartStatus.EDITED);
        assertThat(reloaded.getTotalAmount().getAmount()).isEqualByComparingTo("20.00");
        assertThat(reloaded.getTotalAmount().getCurrency()).isEqualTo("USD");
    }

    @Test
    void findByCustomerId_returnsCart() {
        UUID customerId = UUID.randomUUID();
        cartRepository.save(cartWith(customerId, item("Tea", 1, "5.00")));
        entityManager.flush();
        entityManager.clear();

        assertThat(cartRepository.findByCustomerId(customerId)).isPresent();
        assertThat(cartRepository.existsByCustomerId(customerId)).isTrue();
    }

    @Test
    void orphanRemoval_deletesChildRowWhenRemovedFromCollection() {
        CartEntity cart = cartWith(UUID.randomUUID(), item("A", 1, "1.00"), item("B", 1, "2.00"));
        CartEntity saved = cartRepository.saveAndFlush(cart);
        UUID id = saved.getId();

        // remove one line from the managed collection
        CartItemEntity toRemove = saved.getItems().get(0);
        saved.getItems().remove(toRemove);
        cartRepository.saveAndFlush(saved);
        entityManager.clear();

        CartEntity reloaded = cartRepository.findById(id).orElseThrow();
        assertThat(reloaded.getItems()).hasSize(1);

        // clearing all items removes every child row
        reloaded.getItems().clear();
        cartRepository.saveAndFlush(reloaded);
        entityManager.clear();
        assertThat(cartRepository.findById(id).orElseThrow().getItems()).isEmpty();
    }

    @Test
    void customerId_isUnique() {
        UUID customerId = UUID.randomUUID();
        cartRepository.saveAndFlush(cartWith(customerId, item("X", 1, "1.00")));

        List<CartItemEntity> noItems = new ArrayList<>();
        CartEntity duplicate = CartEntity.builder()
                .customerId(customerId)
                .status(CartStatus.CREATED)
                .totalAmount(new MoneyEmbeddable(BigDecimal.ZERO, "USD"))
                .items(noItems)
                .build();

        assertThatThrownBy(() -> cartRepository.saveAndFlush(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
