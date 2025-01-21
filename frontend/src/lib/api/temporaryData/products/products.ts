import {Product} from "@/lib/types/product/product";

export const products: Product[] = [
    {
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
    },
    {
        "id": "7d6e8c2b-4a3b-9c2d-a1e5-e9f8d7c6b5a4",
        "name": "Lait d'Amande Bio",
        "barcode": "3760020507367",
        "brand": "Bio Délice",
        "price": 2.49,
        "category": "Boissons végétales",
        "lastUpdate": new Date("2025-01-20T10:00:00Z"),
        "stock": {
            "currentQuantity": 25,
            "minThreshold": 15,
            "maxThreshold": 100
        },
        "nutritionalInfo": {
            "energy": 24,
            "proteins": 1.1,
            "carbohydrates": 3,
            "fat": 1.1,
            "fiber": 0.4,
            "ingredients": "Eau, amandes* (7%), carbonate de calcium, sel de mer, stabilisants : gomme guar et carraghénanes. *Ingrédient issu de l'agriculture biologique"
        }
    },
    {
        "id": "6c5d7b1a-3a2b-8c1d-91e4-d8e7f6c5b4a3",
        "name": "Chocolat Noir 70%",
        "barcode": "3760020507374",
        "brand": "Chocolaterie Artisanale",
        "price": 3.29,
        "category": "Confiserie",
        "lastUpdate": new Date("2025-01-20T10:00:00Z"),
        "stock": {
            "currentQuantity": 95,
            "minThreshold": 30,
            "maxThreshold": 250
        },
        "nutritionalInfo": {
            "energy": 545,
            "proteins": 7.8,
            "carbohydrates": 34.2,
            "fat": 42.5,
            "fiber": 10.9,
            "ingredients": "Pâte de cacao*, sucre de canne*, beurre de cacao*, vanille*. *Ingrédients issus du commerce équitable"
        }
    },
    {
        "id": "5b4c6a0f-2a1b-7c0d-81e3-c7d6e5b4a3f2",
        "name": "Yaourt Nature Bio",
        "barcode": "3760020507381",
        "brand": "Ferme Bio",
        "price": 0.99,
        "category": "Produits laitiers",
        "lastUpdate": new Date("2025-01-20T10:00:00Z"),
        "stock": {
            "currentQuantity": 35,
            "minThreshold": 25,
            "maxThreshold": 150
        },
        "nutritionalInfo": {
            "energy": 62,
            "proteins": 4.5,
            "carbohydrates": 5,
            "fat": 3.2,
            "fiber": 0,
            "ingredients": "Lait entier bio pasteurisé, ferments lactiques"
        }
    },
    {
        "id": "4a3b5f9e-1a0b-6c9d-71e2-b6c5d4a3e2f1",
        "name": "Huile d'Olive Extra Vierge",
        "barcode": "3760020507398",
        "brand": "Oliviers & Co",
        "price": 8.99,
        "category": "Huiles",
        "lastUpdate": new Date("2025-01-20T10:00:00Z"),
        "stock": {
            "currentQuantity": 5,
            "minThreshold": 10,
            "maxThreshold": 100
        },
        "nutritionalInfo": {
            "energy": 824,
            "proteins": 0,
            "carbohydrates": 0,
            "fat": 91.6,
            "fiber": 0,
            "ingredients": "100% huile d'olive extra vierge première pression à froid"
        }
    },
    {
        "id": "3f2e4d8c-0a9b-5c8d-61e1-a5b4c3d2e1f0",
        "name": "Pommes Gala Bio",
        "barcode": "3760020507404",
        "brand": "Vergers Bio",
        "price": 2.99,
        "category": "Fruits",
        "lastUpdate": new Date("2025-01-20T10:00:00Z"),
        "stock": {
            "currentQuantity": 180,
            "minThreshold": 20,
            "maxThreshold": 200
        },
        "nutritionalInfo": {
            "energy": 52,
            "proteins": 0.3,
            "carbohydrates": 11.4,
            "fat": 0.2,
            "fiber": 2.4,
            "ingredients": "Pommes Gala issues de l'agriculture biologique"
        }
    },
    {
        "id": "2e1d3c7b-9a8b-4c7d-51e0-94b3c2d1e0f9",
        "name": "Pain Complet Bio",
        "barcode": "3760020507411",
        "brand": "Boulangerie Bio",
        "price": 1.89,
        "category": "Boulangerie",
        "lastUpdate": new Date("2025-01-20T10:00:00Z"),
        "stock": {
            "currentQuantity": 45,
            "minThreshold": 10,
            "maxThreshold": 60
        },
        "nutritionalInfo": {
            "energy": 250,
            "proteins": 8.8,
            "carbohydrates": 45.3,
            "fat": 2.5,
            "fiber": 6.5,
            "ingredients": "Farine complète bio, eau, levain, sel marin"
        }
    },
    {
        "id": "1d0c2b6a-8a7b-3c6d-41e9-83b2c1d0e9f8",
        "name": "Muesli Fruits Rouges",
        "barcode": "3760020507428",
        "brand": "Céréales Bio",
        "price": 4.49,
        "category": "Céréales",
        "lastUpdate": new Date("2025-01-20T10:00:00Z"),
        "stock": {
            "currentQuantity": 90,
            "minThreshold": 15,
            "maxThreshold": 120
        },
        "nutritionalInfo": {
            "energy": 375,
            "proteins": 9.2,
            "carbohydrates": 62.4,
            "fat": 8.3,
            "fiber": 9.1,
            "ingredients": "Flocons d'avoine*, raisins secs*, noisettes*, fraises lyophilisées*, framboises lyophilisées*. *Ingrédients issus de l'agriculture biologique"
        }
    },
    {
        "id": "0c9b1a5f-7a6b-2c5d-31e8-72b1c0d9e8f7",
        "name": "Jus d'Orange Pressé",
        "barcode": "3760020507435",
        "brand": "Pur Jus",
        "price": 2.79,
        "category": "Boissons",
        "lastUpdate": new Date("2025-01-20T10:00:00Z"),
        "stock": {
            "currentQuantity": 110,
            "minThreshold": 20,
            "maxThreshold": 150
        },
        "nutritionalInfo": {
            "energy": 47,
            "proteins": 0.7,
            "carbohydrates": 10.5,
            "fat": 0.2,
            "fiber": 0.4,
            "ingredients": "100% jus d'oranges pressées"
        }
    },
    {
        "id": "9b8a0f4e-6a5b-1c4d-21e7-61a0b9c8d7e6",
        "name": "Tomates Cerises Bio",
        "barcode": "3760020507442",
        "brand": "Potager Bio",
        "price": 2.99,
        "category": "Légumes",
        "lastUpdate": new Date("2025-01-20T10:00:00Z"),
        "stock": {
            "currentQuantity": 100,
            "minThreshold": 15,
            "maxThreshold": 120
        },
        "nutritionalInfo": {
            "energy": 18,
            "proteins": 0.9,
            "carbohydrates": 3.9,
            "fat": 0.2,
            "fiber": 1.2,
            "ingredients": "Tomates cerises issues de l'agriculture biologique"
        }
    }
];