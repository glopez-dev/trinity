package com.trinity.product.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import com.trinity.product.infrastructure.persistence.entity.ProductEntity;

public interface JpaProductRepository extends JpaRepository<ProductEntity, UUID> {

    @NonNull
    Optional<ProductEntity> findById(@NonNull UUID id);

    Optional<ProductEntity> findByBarcode(String barcode);
}
