package com.trinity.stock.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddStockRequest {
    private UUID productId;
    private int quantity;
}
