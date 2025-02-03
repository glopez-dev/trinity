package com.trinity.stock.events;

import com.trinity.stock.constant.TresholdType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class StockTresholdReached extends StockOperationMessage {
    private TresholdType tresholdType;
    private int treshold;
    private int currentQuantity;
}
