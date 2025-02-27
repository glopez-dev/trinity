import {InputValueTypes} from "@/components/ui/input/types";
import {z} from "zod";

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

type NutrientLevels = null | 'low' | 'moderate' | 'high';
interface Nutrient {
    fat: NutrientLevels;
    saturatedFat: NutrientLevels;
    sugars: NutrientLevels;
    salt: NutrientLevels;
}

interface Nutriments {
    energyKcal100g: number;
    proteins100g: number;
    carbohydrates100g: number;
    fat100g: number;
    fiber100g: number;
    salt100g: number;
    sugars100g: number;
}

interface ImageSizes {
    en: string;
    fr: string;
}

interface SelectedImages {
    display: ImageSizes;
    small: ImageSizes;
    thumb: ImageSizes;
}

export interface ProductOFF {
    id: string | null;
    barcode: string;
    category: string | null;
    brand: string;
    name: string;
    ingredients: string;
    price: InputValueTypes | null;
    nutrientLevels: Nutrient;
    nutriments: Nutriments;
    nutriscoreGrade: 'a' | 'b' | 'c' | 'd' | 'e';
    selectedImages: SelectedImages;
    lastUpdate: string | null;
    stock: {
        currentQuantity: InputValueTypes;
        minThreshold: InputValueTypes;
        maxThreshold: InputValueTypes;
    };
}

export interface ProductResponse {
    id: string;
    barcode: string;
    category: string | null;
    brand: string;
    name: string;
    ingredients: string;
    price: InputValueTypes;
    nutrientLevels: Nutrient;
    nutriments: Nutriments;
    nutriscoreGrade: 'a' | 'b' | 'c' | 'd' | 'e';
    selectedImages: SelectedImages;
    lastUpdate: string | null;
    stock: {
        quantity: InputValueTypes;
        minThreshold: InputValueTypes;
        maxThreshold: InputValueTypes;
    };
}

export const updateProductSchema = z.object({
    name: z.string(),
    price: z.number(),
    quantity: z.number()
});

export type UpdateProductSchemaType = z.infer<typeof updateProductSchema>;



