package com.trinity.stock.repository;

import com.trinity.stock.model.Stock;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {
    Optional<Stock> findByStockId(UUID stockId);
    Optional<Stock> findByProductId(UUID productId);
}