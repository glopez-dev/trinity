export interface Product {
    id: string;
    name: string;
    barcode: string;
    brand: string;
    price: number;
    category: string;
    lastUpdate: Date;
    stock: {
        currentQuantity: number;
        minThreshold: number;
        maxThreshold: number;
    };
    nutritionalInfo: {
        energy: number;
        proteins: number;
        carbohydrates: number;
        fat: number;
        fiber: number;
        ingredients: string;
    };
}