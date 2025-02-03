package com.trinity.stock.model;

import java.util.UUID;

import com.trinity.stock.exception.NotEnoughStockException;
import com.trinity.stock.exception.NegativeStockException;
import com.trinity.stock.exception.TooManyStockException;

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
     * @param desiredQuantity the quantity of the product requested.
     * @return true if the quantity is available, false otherwise.
    */
    public boolean canQuantityBeDeducted(int desiredQuantity) {
        return desiredQuantity <= this.getCurrentQuantity();
    }

    /**
     * Deducts the quantity requested from the stock.
     * @param quantity the quantity of the product requested.
     * @throws NotEnoughStockException if the quantity requested is not available in stock.
     * @throws NegativeStockException if the quantity requested is negative.
     */
    public void deductFromStock(int quantity) throws NotEnoughStockException, NegativeStockException {
        if (!canQuantityBeDeducted(quantity)) {
            throw new NotEnoughStockException("Requested quantity is not available in stock");
        }

        int newQuantity = this.getCurrentQuantity() - quantity;

        this.setCurrentQuantity(newQuantity);
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
     * @throws TooManyStockException if the quantity requested exceeds the maximum treshold.
     * @throws NegativeStockException if the quantity requested is negative.
     */
    public void addToStock(int quantity) throws TooManyStockException, NegativeStockException {
        if (!canQuantityBeAdded(quantity)) {
            throw new TooManyStockException("Quantity exceeds the maximum treshold");
        } 

        int newQuantity = this.getCurrentQuantity() + quantity;

        this.setCurrentQuantity(newQuantity);
    }

    public void setMinTreshold(int minTreshold) throws NegativeStockException {
        if (minTreshold < 0) {
            throw new NegativeStockException("Minimum treshold cannot be negative");
        }
        this.minTreshold = minTreshold;
    }

    public void setMaxTreshold(int maxTreshold) throws NegativeStockException {
        if (maxTreshold < 0) {
            throw new NegativeStockException("Maximum treshold cannot be negative");
        }
        this.maxTreshold = maxTreshold;
    }

        
}
