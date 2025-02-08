package com.trinity.product.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import com.trinity.product.model.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    @NonNull Optional<Product> findById(@NonNull UUID id);

}
