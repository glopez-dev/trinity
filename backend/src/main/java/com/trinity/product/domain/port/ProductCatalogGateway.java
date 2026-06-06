package com.trinity.product.domain.port;

import com.trinity.product.domain.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Outbound anti-corruption port to the external product catalogue (OpenFoodFacts).
 * Speaks domain {@link Product}; the adapter confines the WebClient and the
 * OpenFoodFacts wire format, including the legacy null-on-error behaviour.
 */
public interface ProductCatalogGateway {

    /** Full-text search; never null, empty on no match or error. */
    List<Product> search(String searchTerm);

    /** Barcode lookup; empty on not-found or any error. */
    Optional<Product> findByBarcode(String barcode);
}
