package com.trinity.product.domain.port;

import com.trinity.product.domain.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Outbound persistence port for the product aggregate. Speaks domain types only;
 * the JPA implementation lives in infrastructure behind a hand-written mapper.
 */
public interface ProductRepositoryPort {

    Product save(Product product);

    Optional<Product> findById(UUID id);

    Optional<Product> findByBarcode(String barcode);

    List<Product> findAll();

    void delete(Product product);
}
