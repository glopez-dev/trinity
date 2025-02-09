package com.trinity.product.dto.api;

import java.math.BigDecimal;
import java.util.Optional;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "DTO for updating product information. Only filled fields will be updated.")
@Data
public class UpdateProductDTO {
 
    @Schema(description = "Product name", example = "Organic Coffee Beans")
    private String name; 

    @Schema(description = "Product price", example = "9.99")
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal price; 

    @Schema(description = "Available quantity in stock", example = "100")
    @Min(value = 0)
    private Integer quantity; 
    
    // Helper methods to wrap/unwrap Optional
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<BigDecimal> getPrice() {
        return Optional.ofNullable(price);
    }

    public Optional<Integer> getQuantity() {
        return Optional.ofNullable(quantity);
    }

    public void setName(Optional<String> name) {
        this.name = name.orElse(null);
    }

    public void setPrice(Optional<BigDecimal> price) {
        this.price = price.orElse(null);
    }

    public void setQuantity(Optional<Integer> quantity) {
        this.quantity = quantity.orElse(null);
    }
}