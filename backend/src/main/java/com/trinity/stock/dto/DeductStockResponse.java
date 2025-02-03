package com.trinity.stock.dto;

import com.trinity.stock.constant.DeductStockResponseType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeductStockResponse {
    private boolean success;
    private DeductStockResponseType type;
    private String message;
}
