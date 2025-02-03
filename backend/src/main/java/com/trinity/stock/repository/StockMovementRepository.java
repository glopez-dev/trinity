package com.trinity.stock.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trinity.stock.model.StockMovement;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, UUID> {
    Optional<StockMovement> findByStockMovementId(UUID movementId);
}
