package com.trinity.product.dto.api;

import java.util.List;

import com.trinity.product.model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchProductResponse {

    private List<Product> products;
    
}
