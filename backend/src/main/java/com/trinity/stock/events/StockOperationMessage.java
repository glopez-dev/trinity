package com.trinity.stock.events;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class StockOperationMessage {
    protected UUID stockId;
    protected UUID productId;
    protected Instant timestamp;
}