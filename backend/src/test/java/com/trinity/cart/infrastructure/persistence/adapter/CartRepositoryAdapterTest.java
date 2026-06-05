package com.trinity.cart.infrastructure.persistence.adapter;

import com.trinity.cart.domain.Cart;
import com.trinity.cart.domain.CartItem;
import com.trinity.cart.domain.port.CartRepositoryPort;
import com.trinity.cart.mapper.CartPersistenceMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies the domain-port persistence adapter round-trips the aggregate and,
 * crucially, that save(Cart) on an existing cart still triggers orphanRemoval
 * (the in-place update path must be preserved behind the port).
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import({CartRepositoryAdapter.class, CartPersistenceMapper.class})
class CartRepositoryAdapterTest {

    @Autowired
    private CartRepositoryPort port;

    @Autowired
    private TestEntityManager entityManager;

    private CartItem item(String name, String unitPrice, int qty) {
        return new CartItem(UUID.randomUUID(), name, new BigDecimal(unitPrice), qty);
    }

    @Test
    void save_thenFindByCustomerId_roundTripsDomain() {
        UUID customerId = UUID.randomUUID();
        Cart cart = new Cart(customerId);
        cart.addItem(item("Coffee", "10.00", 2));
        port.save(cart);
        entityManager.flush();
        entityManager.clear();

        Cart reloaded = port.findByCustomerId(customerId).orElseThrow();
        assertThat(reloaded.getCustomerId()).isEqualTo(customerId);
        assertThat(reloaded.getItems()).hasSize(1);
        assertThat(reloaded.getTotalAmount().amount()).isEqualByComparingTo("20.00");
        assertThat(port.existsByCustomerId(customerId)).isTrue();
    }

    @Test
    void save_existingCartWithFewerItems_removesOrphanLines() {
        UUID customerId = UUID.randomUUID();
        Cart cart = new Cart(customerId);
        CartItem a = item("A", "1.00", 1);
        CartItem b = item("B", "2.00", 1);
        cart.addItem(a);
        cart.addItem(b);
        port.save(cart);
        entityManager.flush();
        entityManager.clear();

        // Reload, drop one line, save again through the port.
        Cart reloaded = port.findByCustomerId(customerId).orElseThrow();
        CartItem toRemove = reloaded.getItems().stream()
                .filter(i -> i.getProductName().equals("A")).findFirst().orElseThrow();
        reloaded.removeItem(toRemove);
        port.save(reloaded);
        entityManager.flush();
        entityManager.clear();

        Cart afterRemoval = port.findByCustomerId(customerId).orElseThrow();
        assertThat(afterRemoval.getItems()).hasSize(1);
        assertThat(afterRemoval.getItems().iterator().next().getProductName()).isEqualTo("B");
    }

    @Test
    void deleteByCustomerId_removesCart() {
        UUID customerId = UUID.randomUUID();
        Cart cart = new Cart(customerId);
        cart.addItem(item("X", "1.00", 1));
        port.save(cart);
        entityManager.flush();

        port.deleteByCustomerId(customerId);
        entityManager.flush();
        entityManager.clear();

        assertThat(port.findByCustomerId(customerId)).isEmpty();
    }
}
