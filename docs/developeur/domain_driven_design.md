# Architecture Modulaire - Structure DDD

## 1. Structure Générale des Modules

```
com.trinity
├── shared
│   ├── kernel            // Composants partagés entre domaines
│   ├── infrastructure    // Configuration commune, sécurité, etc.
│   └── utils            
├── product
│   ├── domain
│   │   ├── model
│   │   ├── repository
│   │   └── service
│   ├── application
│   │   ├── dto
│   │   ├── mapper
│   │   └── service
│   ├── infrastructure
│   │   ├── persistence
│   │   └── integration
│   └── api
│       └── controller
├── inventory
│   ├── [même structure]
├── user
│   ├── [même structure]
├── order
│   ├── [même structure]
└── reporting
    ├── [même structure]
```

## 2. Bounded Contexts et Domaines

### Product Context
**Responsabilités**:
- Gestion des informations produits
- Intégration avec Open Food Facts
- Catégorisation et caractéristiques produits

**Agrégats Principaux**:
- Product (Agrégat Root)
- Category
- Nutritional Information

### Inventory Context
**Responsabilités**:
- Gestion des stocks
- Alertes de niveau de stock
- Mouvements de stock

**Agrégats Principaux**:
- Stock (Agrégat Root)
- StockMovement
- StockAlert

### User Context
**Responsabilités**:
- Gestion des utilisateurs
- Authentification et autorisation
- Profils clients

**Agrégats Principaux**:
- User (Agrégat Root)
- Role
- UserProfile

### Order Context
**Responsabilités**:
- Gestion des commandes
- Paiements
- Historique des achats

**Agrégats Principaux**:
- Order (Agrégat Root)
- Payment
- OrderLine

### Reporting Context
**Responsabilités**:
- KPIs
- Analyses des ventes
- Rapports personnalisés

**Agrégats Principaux**:
- Report (Agrégat Root)
- KPI
- ReportTemplate

## 3. Communication Inter-Modules

### Events Domain
```java
// Exemple d'événement domain
public class StockLevelChangedEvent {
    private final String productId;
    private final int newQuantity;
    private final int previousQuantity;
    private final LocalDateTime timestamp;
}
```

### Events Flow
```
Inventory -> Product
- StockLevelChangedEvent
- StockAlertEvent

Order -> Inventory
- OrderCreatedEvent
- OrderConfirmedEvent

Product -> Inventory
- ProductCreatedEvent
- ProductUpdatedEvent
```

## 4. Shared Kernel

### Components Partagés
```java
// Value Objects partagés
public class Money {
    private BigDecimal amount;
    private Currency currency;
}

public class Quantity {
    private int value;
    private Unit unit;
}
```

### Interfaces Communes
```java
// Interface générique pour les repositories
public interface BaseRepository<T, ID> {
    Optional<T> findById(ID id);
    T save(T entity);
    void delete(T entity);
}
```

## 5. Module Boundaries

### Règles d'Interaction
1. Les modules ne communiquent que via :
   - Events (RabbitMQ)
   - APIs exposées (interfaces publiques)

2. Chaque module maintient :
   - Sa propre persistance
   - Ses propres validations
   - Sa propre logique métier

### Exemple d'API Module
```java
// API publique du module Product
public interface ProductManagementApi {
    ProductDTO getProduct(String productId);
    List<ProductDTO> searchProducts(SearchCriteria criteria);
    void updateProduct(ProductUpdateCommand command);
}
```

## 6. Patterns d'Implémentation

### CQRS Léger
```java
// Commande
public class UpdateStockCommand {
    private final String productId;
    private final int quantity;
}

// Query
public class StockQuery {
    private final String productId;
}
```

### Event Sourcing Pattern
```java
public interface EventStore {
    void store(DomainEvent event);
    List<DomainEvent> getEvents(String aggregateId);
}
```

## 7. Configuration Technique

```yaml
# Exemple de configuration module
trinity:
  modules:
    product:
      enabled: true
      database:
        schema: product
    inventory:
      enabled: true
      database:
        schema: inventory
```
