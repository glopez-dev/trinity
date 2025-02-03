package com.trinity.stock.controller;

import com.trinity.stock.dto.AddStockRequest;
import com.trinity.stock.service.StockService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/stock")
@AllArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping
    public ResponseEntity<Void> addToStock(@Valid @RequestBody AddStockRequest request) {
        stockService.addToStock(request);
        return ResponseEntity.ok().build();
    }

}