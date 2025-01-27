package com.trinity.stock.model;

import java.util.UUID;

import com.trinity.stock.exception.NotEnoughStockException;
import com.trinity.stock.exception.NegativeStockException;
import com.trinity.stock.exception.TooMuchStockException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID productId;

    @Builder.Default
    @Column(nullable = false)
    private int currentQuantity = 0;

    @Builder.Default
    @Column(nullable = false)
    private int minTreshold = 10;

    @Builder.Default
    @Column(nullable = false)
    private int maxTreshold = 100; 

    /**
     * Sets the current quantity of the product in stock.
     * 
     * @param currentQuantity the current quantity of the product in stock.
     * @throws NegativeStockException if the current quantity is negative.
     */
    void setCurrentQuantity(int currentQuantity) throws NegativeStockException{
        if (currentQuantity < 0) {
            throw new NegativeStockException("Current quantity cannot be negative");
        }
        this.currentQuantity = currentQuantity;
    }

    /** 
     * Checks if the quantity requested is available in stock
     * @param desiredQuantity the quantity of the product requested
     * @return true if the quantity is available, false otherwise
    */
    public boolean canQuantityBeDeducted(int desiredQuantity) {
        return desiredQuantity <= this.getCurrentQuantity();
    }

    /**
     * Deducts the quantity requested from the stock.
     * @param quantity the quantity of the product requested.
     * @throws NotEnoughStockException if the quantity requested is not available in stock
     */
    public void deductFromStock(int quantity) throws NotEnoughStockException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity cannot be zero or negative");
        }

        if (!canQuantityBeDeducted(quantity)) {
            throw new NotEnoughStockException("Requested quantity is not available in stock");
        }

        int newQuantity = this.getCurrentQuantity() - quantity;

        try {
            this.setCurrentQuantity(newQuantity);
        } catch (NegativeStockException e) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
    }

    /**
     * Checks if the quantity requested can be added to the stock.
     * 
     * @param quantity the quantity to be added.
     * @return true if the quantity can be added, false otherwise.
     */
    public boolean canQuantityBeAdded(int quantity) {
        return this.getCurrentQuantity() + quantity <= this.getMaxTreshold();
    }

    /**
     * Adds the quantity requested to the stock.
     * 
     * @param quantity the quantity to be added.
     * @throws TooMuchStockException if the quantity requested exceeds the maximum treshold.
     */
    public void addToStock(int quantity) throws TooMuchStockException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity cannot be zero or negative");
        }

        if (!canQuantityBeAdded(quantity)) {
            throw new TooMuchStockException("Quantity exceeds the maximum treshold");
        } 

        int newQuantity = this.getCurrentQuantity() + quantity;

        try {
            this.setCurrentQuantity(newQuantity);
        } catch (NegativeStockException e) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
    }

    public void setMinTreshold(int minTreshold) throws IllegalArgumentException {
        if (minTreshold < 0) {
            throw new IllegalArgumentException("Minimum treshold cannot be negative");
        }
        this.minTreshold = minTreshold;
    }

    public void setMaxTreshold(int maxTreshold) throws IllegalArgumentException {
        if (maxTreshold < 0) {
            throw new IllegalArgumentException("Maximum treshold cannot be negative");
        }
        this.maxTreshold = maxTreshold;
    }

        
}
