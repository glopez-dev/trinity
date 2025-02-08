package com.trinity.product.dto.product;

import lombok.Data;

@Data
public class NutrimentsDTO {
    private Integer energyKcal100g;
    private Double proteins100g;
    private Double carbohydrates100g;
    private Integer fat100g;
    private Integer fiber100g;
    private Double salt100g;
    private Double sugars100g;
}