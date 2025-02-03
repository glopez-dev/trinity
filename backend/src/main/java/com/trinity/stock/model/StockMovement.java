package com.trinity.stock.model;

import java.time.Instant;
import java.util.UUID;

import com.trinity.stock.constant.MovementType;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockMovement {

    private UUID id;

    @Builder.Default
    private Instant timestamp = Instant.now();

    private int quantity;

    private MovementType type;

    private String reason;
}
