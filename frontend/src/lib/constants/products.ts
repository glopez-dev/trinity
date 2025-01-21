export const LOW_STOCK= 'Bas';
export const NORMAL_STOCK= 'Normal';
export const VERY_LOW_STOCK= 'Insuffisant';

export const STOCK_STATUS = {
    LOW_STOCK,
    NORMAL_STOCK,
    VERY_LOW_STOCK
}

export const getStockStatus = (currentQuantity: number, minThreshold: number): string => {
    if (currentQuantity < minThreshold) {
        return VERY_LOW_STOCK;
    }
    if (currentQuantity < minThreshold * 2) {
        return LOW_STOCK;
    }
    return NORMAL_STOCK;
}