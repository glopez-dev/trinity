package com.trinity.product.dto.product;

import lombok.Data;

@Data
public class StockDTO {
    private String currentQuantity;
    private String minThreshold;
    private String maxThreshold;
}