package com.trinity.product.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trinity.product.application.command.UpdateProductCommand;
import com.trinity.product.domain.model.Product;
import com.trinity.product.domain.port.ProductCatalogGateway;
import com.trinity.product.domain.port.ProductRepositoryPort;
import com.trinity.product.domain.exception.ProductNotFoundException;
import com.trinity.product.domain.exception.InvalidProductDataException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepositoryPort productRepository;
    private final ProductCatalogGateway productCatalogGateway;

    @Transactional
    public Product createProduct(Product product) {
        validateProduct(product);
        Product savedProduct = productRepository.save(product);
        logger.info("Created new product with ID: {}", savedProduct.getId());
        return savedProduct;
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        logger.debug("Retrieved {} products", products.size());
        return products;
    }

    @Transactional(readOnly = true)
    public Product getProduct(UUID productId) {
        return findProductById(productId);
    }

    @Transactional
    public Product updateProduct(UUID productId, UpdateProductCommand command) {
        Product product = findProductById(productId);

        command.name().ifPresent(product::setName);
        command.price().ifPresent(product::changePrice);
        command.quantity().ifPresent(product::adjustStock);

        Product updatedProduct = productRepository.save(product);
        logger.info("Updated product with ID: {}", productId);
        return updatedProduct;
    }

    /** Deducts a sold quantity from the stock; rejects going negative. */
    @Transactional
    public Product deductStock(UUID productId, int quantity) {
        Product product = findProductById(productId);
        product.adjustStock(-quantity);
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(UUID productId) {
        Product product = findProductById(productId);
        productRepository.delete(product);
        logger.info("Deleted product with ID: {}", productId);
    }

    // No transaction: pure external HTTP call, nothing is read or written locally.
    public List<Product> searchProducts(String searchTerm) {
        return productCatalogGateway.search(searchTerm);
    }

    // No method-level transaction: the OpenFoodFacts lookup is an external HTTP
    // call that must not hold a DB connection. The save inside importFromCatalog
    // is transactional at the adapter level, which is enough for this
    // single-aggregate write.
    public Product scanProduct(String barcode) {
        return productRepository.findByBarcode(barcode)
                .map(product -> {
                    logger.info("Product with barcode {} found in database", barcode);
                    return product;
                })
                .orElseGet(() -> {
                    logger.info("Product with barcode {} not found in database, checking OpenFoodFacts", barcode);
                    return productCatalogGateway.findByBarcode(barcode)
                            .map(this::importFromCatalog)
                            .orElseThrow(() -> new ProductNotFoundException(
                                    String.format("Product with barcode %s not found", barcode)));
                });
    }

    private Product importFromCatalog(Product fromCatalog) {
        fromCatalog.applyImportDefaults();
        validateProduct(fromCatalog);
        Product savedProduct = productRepository.save(fromCatalog);
        logger.info("Created new product with ID: {}", savedProduct.getId());
        return savedProduct;
    }

    private Product findProductById(UUID productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(
                String.format("Product not found with ID: %s", productId)));
    }

    private void validateProduct(Product product) {
        if (product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidProductDataException("Price cannot be negative");
        }
        if (product.getStock().getQuantity() < 0) {
            throw new InvalidProductDataException("Initial stock quantity cannot be negative");
        }
    }
}
