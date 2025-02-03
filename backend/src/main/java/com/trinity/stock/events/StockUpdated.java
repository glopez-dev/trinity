package com.trinity.stock.events;

import com.trinity.stock.constant.UpdateType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StockUpdated extends StockOperationMessage {
    private int oldQuantity; 
    private int newQuantity;
    UpdateType updateType;
}
