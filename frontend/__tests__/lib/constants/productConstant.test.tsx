import {describe, expect, it} from "vitest";
import {Product} from "@/lib/types/product/product";
import {getStockStatus, STOCK_STATUS} from "@/lib/constants/products";

const product: Product = {
    "id": "8f7e9d2c-5a4b-4c3d-b1e6-f9a8b7c6d5e4",
    "name": "Bio Organic Quinoa",
    "barcode": "3760020507350",
    "brand": "Nature Bio",
    "price": 4.99,
    "category": "Céréales",
    "lastUpdate": new Date("2025-01-20T10:00:00Z"),
    "stock": {
        "currentQuantity": 35,
        "minThreshold": 20,
        "maxThreshold": 200
    },
    "nutritionalInfo": {
        "energy": 368,
        "proteins": 14.1,
        "carbohydrates": 64.2,
        "fat": 6.1,
        "fiber": 7,
        "ingredients": "Quinoa biologique"
    }
};

describe('prodcut constants', () => {

    it('should get the correct stock status for low stock', () => {
        const stockStatus = getStockStatus(product.stock.currentQuantity, product.stock.minThreshold);
        expect(stockStatus).toEqual(STOCK_STATUS.LOW_STOCK);
    });

    it('should get the correct stock status for very low stock', () => {
        const stockStatus = getStockStatus(10, 20);
        expect(stockStatus).toEqual(STOCK_STATUS.VERY_LOW_STOCK);
    });

    it('should get the correct stock status for normal stock', () => {
        const stockStatus = getStockStatus(50, 20);
        expect(stockStatus).toEqual(STOCK_STATUS.NORMAL_STOCK);
    });
});