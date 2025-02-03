package com.trinity.stock.dto;

import com.trinity.stock.constant.AddStockResponseType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddStockResponse {
    private boolean success;
    private AddStockResponseType type;
    private String message;
}
