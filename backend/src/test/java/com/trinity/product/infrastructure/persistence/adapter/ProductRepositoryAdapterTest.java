package com.trinity.product.infrastructure.persistence.adapter;

import com.trinity.product.domain.model.Product;
import com.trinity.product.domain.port.ProductRepositoryPort;
import com.trinity.product.infrastructure.persistence.mapper.ProductPersistenceMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies the domain-port persistence adapter round-trips the aggregate through
 * the hand-written mapper and that save() on an existing product takes the
 * in-place update path (preserving id behind the port).
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import({ProductRepositoryAdapter.class, ProductPersistenceMapper.class})
class ProductRepositoryAdapterTest {

    @Autowired
    private ProductRepositoryPort port;

    @Test
    void save_thenFindByBarcode_roundTripsDomain() {
        Product saved = port.save(Product.builder()
            .barcode("3017620422003").name("Nutella").brand("Ferrero")
            .price(new BigDecimal("4.99")).category("Spreads")
            .stock(Product.Stock.builder().quantity(10).minThreshold(2).maxThreshold(50).build())
            .build());

        assertThat(saved.getId()).isNotNull();
        assertThat(port.findByBarcode("3017620422003"))
            .isPresent()
            .get()
            .satisfies(p -> {
                assertThat(p.getName()).isEqualTo("Nutella");
                assertThat(p.getBrand()).isEqualTo("Ferrero");
                assertThat(p.getPrice()).isEqualByComparingTo("4.99");
                assertThat(p.getStock().getQuantity()).isEqualTo(10);
            });
    }

    @Test
    void save_existingProduct_updatesInPlace() {
        Product created = port.save(Product.builder()
            .barcode("111222333").name("Old name").price(new BigDecimal("1.00"))
            .build());
        UUID id = created.getId();

        // Reload, mutate the domain, save again — must update, not insert a second row.
        Product reloaded = port.findById(id).orElseThrow();
        reloaded.setName("New name");
        reloaded.changePrice(new BigDecimal("2.50"));
        Product updated = port.save(reloaded);

        assertThat(updated.getId()).isEqualTo(id);
        assertThat(port.findById(id))
            .isPresent()
            .get()
            .satisfies(p -> {
                assertThat(p.getName()).isEqualTo("New name");
                assertThat(p.getPrice()).isEqualByComparingTo("2.50");
            });
        assertThat(port.findAll()).hasSize(1);
    }

    @Test
    void findById_mapsToDomain() {
        Product created = port.save(Product.builder()
            .barcode("444555666").name("Findable").price(new BigDecimal("3.00"))
            .build());

        assertThat(port.findById(created.getId()))
            .isPresent()
            .get()
            .satisfies(p -> assertThat(p.getBarcode()).isEqualTo("444555666"));
    }

    @Test
    void findAll_mapsAllToDomain() {
        port.save(Product.builder().barcode("a1").name("A").price(new BigDecimal("1.00")).build());
        port.save(Product.builder().barcode("b2").name("B").price(new BigDecimal("2.00")).build());

        assertThat(port.findAll()).hasSize(2);
    }

    @Test
    void delete_removesProduct() {
        Product created = port.save(Product.builder()
            .barcode("777888999").name("Doomed").price(new BigDecimal("5.00"))
            .build());

        port.delete(created);

        assertThat(port.findById(created.getId())).isEmpty();
    }
}
