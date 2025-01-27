package com.trinity.product.core.dto;

import java.util.List;

import com.trinity.product.core.model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrinitySearchResponse {

    private List<Product> products;
    
}
