package com.trinity.stock.exception;

public class NegativeStockException extends Exception {
    public NegativeStockException(String message) {
        super(message);
    }
    
}
