# Domaine Product

## Diagramme de classes (UML)

### Core Domain (Product) 

```mermaid
classDiagram
    class Product {
        +UUID id
        +String name
        +String barcode
        +String brand
        +Double price
        +String category
        +NutritionalInfo nutritionalInfo
        +createProduct()
        +updateProduct()
        +enrichWithOpenFoodData()
    }

    class NutritionalInfo {
        +Double energy
        +Double proteins
        +Double carbohydrates
        +Double fat
        +Double fiber
        +String ingredients
    }

    class ProductBatch {
        +UUID id
        +UUID productId
        +Integer quantity
        +DateTime expirationDate
        +BatchStatus status
        +DateTime createdAt
        +DateTime updatedAt
        +addBatch()
        +updateStatus()
        +delete()
    }

    class BatchStatus {
        <<enumeration>>
        IN_STOCK
        LOW_STOCK
        EXPIRING_SOON
        EXPIRED
        DELETED
    }

    class ProductEvent {
        <<interface>>
        +UUID productId
        +DateTime timestamp
    }

    class ProductScanned {
        +String barcode
        +DateTime scannedAt
    }

    class ProductCreated {
        +String barcode
        +String name
        +NutritionalInfo nutritionalInfo
    }

    class BatchAdded {
        +UUID batchId
        +Integer quantity
        +DateTime expirationDate
    }

    class BatchStatusChanged {
        +UUID batchId
        +BatchStatus oldStatus
        +BatchStatus newStatus
        +String reason
    }

    class OpenFoodAPIAdapter {
        +fetchProductData(String barcode)
        +enrichProductInfo(Product product)
        -validateOpenFoodData()
    }

    Product "1" -- "*" ProductBatch
    Product --> NutritionalInfo
    ProductEvent <|-- ProductScanned
    ProductEvent <|-- ProductCreated
    ProductEvent <|-- BatchAdded
    ProductEvent <|-- BatchStatusChanged
    Product --> OpenFoodAPIAdapter : utilise

    note for Product "Agrégat racine pour les informations produit"
    note for ProductBatch "Gestion des lots et péremption"
    note for OpenFoodAPIAdapter "ACL pour Open Food Facts API"
```

### Sub-domain (Stock)

```mermaid
classDiagram
    class Product {
        +UUID id
        +String name
        +String barcode
        +String brand
        +Double price
        +String category
        +NutritionalInfo nutritionalInfo
    }

    class Stock {
        +UUID id
        +UUID productId
        +Integer currentQuantity
        +Integer minThreshold
        +Integer maxThreshold
        +checkAvailability(Integer quantity)
        +removeFromStock(Integer quantity)
        +addToStock(Integer quantity)
        +updateThresholds(Integer min, Integer max)
    }

    class StockEvent {
        <<interface>>
        +UUID stockId
        +UUID productId
        +DateTime timestamp
    }

    class StockChecked {
        +Integer requestedQuantity
        +Boolean isAvailable
    }

    class StockUpdated {
        +Integer oldQuantity
        +Integer newQuantity
        +UpdateType type
    }

    class StockThresholdReached {
        +ThresholdType type
        +Integer currentQuantity
        +Integer threshold
    }

    class UpdateType {
        <<enumeration>>
        ADDITION
        REMOVAL
        ADJUSTMENT
    }

    class ThresholdType {
        <<enumeration>>
        MINIMUM
        MAXIMUM
    }

    class StockMovement {
        +UUID id
        +DateTime timestamp
        +Integer quantity
        +MovementType type
        +String reason
    }

    class MovementType {
        <<enumeration>>
        IN
        OUT
    }

    Product "1" -- "1" Stock
    Stock "1" -- "*" StockMovement
    StockEvent <|-- StockChecked
    StockEvent <|-- StockUpdated
    StockEvent <|-- StockThresholdReached

    note for Stock "Gère la quantité et les limites"
    note for StockMovement "Historique des mouvements"
    note for StockThresholdReached "Notifie quand un seuil est atteint"
```


## Description du _Domain Model_

- Agrégat Racine : `Product`
  - Attributs principaux : id, name, barcode, brand, price, category, nutritionalInfo
  - Méthodes clés : createProduct(), updateProduct(), enrichWithOpenFoodData()
  - Gestion des lots via `ProductBatch`

- Value Objects :
  - `NutritionalInfo` : Informations nutritionnelles
    - Attributs : energy, proteins, carbohydrates, fat, fiber, ingredients
  - `ProductBatch` : Gestion des lots
    - Attributs : id, productId, quantity, expirationDate, status, createdAt, updatedAt
    - Méthodes : addBatch(), updateStatus(), delete()

- Énumérations :
  - `BatchStatus` : États des lots
    - IN_STOCK : Disponible en stock
    - LOW_STOCK : Stock faible
    - EXPIRING_SOON : Proche de la péremption
    - EXPIRED : Périmé
    - DELETED : Supprimé

- Événements de Domaine :
  - `ProductEvent` (interface) : Base pour tous les événements produit
  - `ProductScanned` : Scan d'un produit (barcode, scannedAt)
  - `ProductCreated` : Création d'un nouveau produit
  - `BatchAdded` : Ajout d'un nouveau lot
  - `BatchStatusChanged` : Changement de statut d'un lot

- Anti-Corruption Layer :
  - `OpenFoodAPIAdapter` : Interface avec l'API Open Food Facts
    - Méthodes : fetchProductData(), enrichProductInfo()
    - Validation des données externes

- Règles Métier Principales :
  1. Chaque produit doit avoir un code-barres unique
  2. Les informations nutritionnelles sont enrichies via Open Food Facts
  3. La gestion des lots doit tenir compte des dates de péremption
  4. Les changements de statut des lots doivent être tracés
  5. Les produits périmés ne doivent pas être disponibles à la vente

- Comportements Spécifiques :
  - Enrichissement automatique des données via Open Food Facts
  - Surveillance des dates de péremption
  - Gestion des alertes de stock faible
  - Traçabilité des mouvements de stock
