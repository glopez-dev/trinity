import {describe, expect, it} from "vitest";
import {render, screen} from "@testing-library/react";
import {ProductResponse} from "@/lib/types/product/product";
import {ProductCard} from "@/components/ui/cards/product/ProductCard";
import {STOCK_STATUS} from "@/lib/constants/products";

const productLow: ProductResponse = {
    "id": "8f7e9d2c-5a4b-4c3d-b1e6-f9a8b7c6d5e4",
    "name": "Bio Organic Quinoa",
    "barcode": "3760020507350",
    "brand": "Nature Bio",
    "price": 4.99,
    "category": "Céréales",
    "ingredients": "Quinoa biologique",
    "lastUpdate": "2025-01-20T10:00:00Z",
    "stock": {
        "quantity": 35,
        "minThreshold": 20,
        "maxThreshold": 200
    },
    "nutrientLevels": {
        "fat": "low",
        "saturatedFat": "low",
        "sugars": "low",
        "salt": "low"
    },
    "nutriments": {
        "energyKcal100g": 368,
        "proteins100g": 14.1,
        "carbohydrates100g": 64.2,
        "fat100g": 6.1,
        "fiber100g": 7,
        "salt100g": 0.01,
        "sugars100g": 0.8
    },
    "nutriscoreGrade": "a",
    "selectedImages": {
        "display": {
            "en": "",
            "fr": ""
        },
        "small": {
            "en": "",
            "fr": ""
        },
        "thumb": {
            "en": "",
            "fr": ""
        }
    }
};

const productVeryLow: ProductResponse = {
    "id": "4a3b5f9e-1a0b-6c9d-71e2-b6c5d4a3e2f1",
    "name": "Huile d'Olive Extra Vierge",
    "barcode": "3760020507398",
    "brand": "Oliviers & Co",
    "price": 8.99,
    "category": "Huiles",
    "ingredients": "100% huile d'olive extra vierge première pression à froid",
    "lastUpdate": "2025-01-20T10:00:00Z",
    "stock": {
        "quantity": 5,
        "minThreshold": 10,
        "maxThreshold": 100
    },
    "nutrientLevels": {
        "fat": "high",
        "saturatedFat": "moderate",
        "sugars": "low",
        "salt": "low"
    },
    "nutriments": {
        "energyKcal100g": 824,
        "proteins100g": 0,
        "carbohydrates100g": 0,
        "fat100g": 91.6,
        "fiber100g": 0,
        "salt100g": 0,
        "sugars100g": 0
    },
    "nutriscoreGrade": "c",
    "selectedImages": {
        "display": {
            "en": "",
            "fr": ""
        },
        "small": {
            "en": "",
            "fr": ""
        },
        "thumb": {
            "en": "",
            "fr": ""
        }
    }
};

const productNormal: ProductResponse = {
    "id": "3f2e4d8c-0a9b-5c8d-61e1-a5b4c3d2e1f0",
    "name": "Pommes Gala Bio",
    "barcode": "3760020507404",
    "brand": "Vergers Bio",
    "price": 2.99,
    "category": "Fruits",
    "ingredients": "Pommes Gala issues de l'agriculture biologique",
    "lastUpdate": "2025-01-20T10:00:00Z",
    "stock": {
        "quantity": 180,
        "minThreshold": 20,
        "maxThreshold": 200
    },
    "nutrientLevels": {
        "fat": "low",
        "saturatedFat": "low",
        "sugars": "moderate",
        "salt": "low"
    },
    "nutriments": {
        "energyKcal100g": 52,
        "proteins100g": 0.3,
        "carbohydrates100g": 11.4,
        "fat100g": 0.2,
        "fiber100g": 2.4,
        "salt100g": 0.01,
        "sugars100g": 10.8
    },
    "nutriscoreGrade": "a",
    "selectedImages": {
        "display": {
            "en": "",
            "fr": ""
        },
        "small": {
            "en": "",
            "fr": ""
        },
        "thumb": {
            "en": "",
            "fr": ""
        }
    }
};

describe("ProductCard", () => {

    it('should render correctly the product card', () => {
        const {container} = render(<ProductCard product={productLow} key={productLow.id}/>);
        expect(container).toBeDefined();

        expect(screen.getByText(productLow.name)).toBeDefined();

        expect(screen.getByText('Catégorie')).toBeDefined();

        expect(screen.getByText('Prix')).toBeDefined();
        expect(screen.getByText(`${productLow.price} €`)).toBeDefined();

        expect(screen.getByText('Seuil Minimal')).toBeDefined();
        expect(screen.getByText(productLow.stock.minThreshold)).toBeDefined();

        expect(screen.getByText('Quantité')).toBeDefined();
        expect(screen.getByText(productLow.stock.quantity)).toBeDefined();
    });

    it('should display the correct badge depend on product stock status', () => {
        const {rerender, container} = render(<ProductCard product={productLow} key={productLow.id}/>);
        expect(container).toBeDefined();

        const badgeLow = screen.getByRole('badge');
        expect(badgeLow).toBeDefined();
        expect(badgeLow.textContent).toBe(STOCK_STATUS.LOW_STOCK);
        expect(badgeLow.getAttribute('class')).toContain('warning');

        rerender(<ProductCard product={productVeryLow} key={productVeryLow.id}/>);

        const badgeVeryLow = screen.getByRole('badge');
        expect(badgeVeryLow).toBeDefined();
        expect(badgeVeryLow.textContent).toBe(STOCK_STATUS.VERY_LOW_STOCK);
        expect(badgeVeryLow.getAttribute('class')).toContain('error');

        rerender(<ProductCard product={productNormal} key={productNormal.id}/>);

        const badgeNormal = screen.getByRole('badge');
        expect(badgeNormal).toBeDefined();
        expect(badgeNormal.textContent).toBe(STOCK_STATUS.NORMAL_STOCK);
        expect(badgeNormal.getAttribute('class')).toContain('success');
    });
});