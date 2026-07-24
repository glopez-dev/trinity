package com.trinity.product.infrastructure.persistence.adapter;

import com.trinity.product.domain.model.Product;
import com.trinity.product.domain.port.ProductRepositoryPort;
import com.trinity.product.infrastructure.persistence.entity.ProductEntity;
import com.trinity.product.infrastructure.persistence.mapper.ProductPersistenceMapper;
import com.trinity.product.infrastructure.persistence.repository.JpaProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Persistence adapter implementing {@link ProductRepositoryPort} over Spring Data.
 *
 * <p>An existing-by-id aggregate is synced in-place via the mapper's
 * {@code updateEntity} (preserving the managed entity's id and Hibernate-owned
 * timestamps); a new aggregate is inserted directly so the unique-barcode
 * constraint keeps its INSERT semantics (a colliding barcode still raises a
 * DataIntegrityViolation rather than silently upserting).
 */
@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final JpaProductRepository jpaProductRepository;
    private final ProductPersistenceMapper mapper;

    @Override
    public Product save(Product product) {
        if (product.getId() != null) {
            Optional<ProductEntity> managed = jpaProductRepository.findById(product.getId());
            if (managed.isPresent()) {
                ProductEntity target = managed.get();
                mapper.updateEntity(target, product);
                return mapper.toDomain(jpaProductRepository.save(target));
            }
        }
        return mapper.toDomain(jpaProductRepository.save(mapper.toEntity(product)));
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpaProductRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Product> findByBarcode(String barcode) {
        return jpaProductRepository.findByBarcode(barcode).map(mapper::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return jpaProductRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void delete(Product product) {
        if (product.getId() != null) {
            jpaProductRepository.deleteById(product.getId());
        } else {
            jpaProductRepository.findByBarcode(product.getBarcode()).ifPresent(jpaProductRepository::delete);
        }
    }
}
