# Tactical Patterns

## Ressources
- https://en.wikipedia.org/wiki/Domain-driven_design

<iframe width="560" height="315" src="https://www.youtube.com/embed/WZb-FPmiuMY?si=-1tVU4gTm8Kk0t_M" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>

<iframe width="560" height="315" src="https://www.youtube.com/embed/3n3OcAIlXjk?si=-9qI3hd4XujJjA1f" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>

## Model Driven Design

Les développeurs construisent un *domain model* : un ensemble d'abstractions qui décrit une certains aspects du domaine métier et qui peut être utilisé pour résoudre des problèmes liés à ce domaine.

## Types de modèles

Une ***Entity*** est un objet qui n'est pas défini par ses attributs mais par son identité.

Example: Une companie d'avions assigne un numéro unique à chaque siège d'un avion; c'est l'identité du siège.

À contrario un ***value object*** est un objet immutable qui contient des attributs mais n'a pas d'identité propre.

Example: Une carte de visite contient des informations (attributs) mais on ne cherche pas à distinguer chaque carte de visite.

Les modèles peuvent aussi définir des ***events*** (quelque chose qui s'est produit dans le passé). Un ***domain event*** est un événement important pour les experts du domaine métier.

#### Aggregates
Les modèles peuvent être liés entre eux par une ***root entity*** (entité racine) et devenir des ***aggregates*** (aggrégats). 

Les objets externes peuvent conserver une référence à l'objet racine mais pas aux autres objets de l'aggrégat.

La racine de l'aggrégat conserve la consistence des changements dans l'aggrégat.

Example: Un conducteur ne contrôle pas individuellement chaque roue de son véhicule. Une voiture est un aggrégat de plusieurs objets (le moteur, les freins, les phares).

- https://www.baeldung.com/spring-persisting-ddd-aggregates

## Travailler avec les modèles
En DDD, la création d'un objet est souvent séparée de l'objet lui même.

Un ***repository*** par example est un objet avec des méthodes pour récupérer et stocker des *domain objects* (aggrégats) dans un *data store* (une *database*).

Une ***factory*** est un objet avec des méthodes qui créent directement des *domain objects* (aggrégats).

Quand une fonctionnalité d'un programme n'appartient conceptuellement à aucun objet du domaine métier elle est exprimée comme un ***service***.


## User Domain

L'organisation en packages pourrait ressembler à :


```
com.myapp.user/   
├── domain/           # Core Domain  
│   ├── User.java  
│   ├── Customer.java  
│   └── Employee.java  
├── infrastructure/   # Anti-Corruption Layer  
│   └── paypal/  
│       ├── PayPalIdentityAdapter.java  
│       └── PayPalIdentityTranslator.java  
└── application/      # Use Cases      
	└── UserService.java
```

### Core Domain
```mermaid
classDiagram
    class User {
        <<abstract>>
        +UUID id
        +String email
        +String password
        +PersonalInfo personalInfo
        +UserStatus status
        +DateTime createdAt
        +DateTime updatedAt
        +DateTime lastLoginAt
        +connect()
        +delete()
    }

    class Employee {
        +EmployeeRole role
        +DateTime hireDate
        +DateTime terminationDate
        +promote()
        +deactivate()
        +update()
    }

    class Customer {
        +List~Address~ addresses
        +updateProfile()
    }

    class EmployeeRole {
        <<enumeration>>
        STORE_MANAGER
        CASHIER
    }

    class UserStatus {
        <<enumeration>>
        ACTIVE
        INACTIVE
        DELETED
    }

    class PersonalInfo {
        +String firstName
        +String lastName
        +String phoneNumber
    }

    class Address {
        +UUID id
        +String street
        +String zipCode
        +String city
        +String country
        +Boolean isDefault
    }

    class UserEvent {
        <<interface>>
        +UUID userId
        +DateTime timestamp
    }

    class UserConnected {
        +String ipAddress
        +String userAgent
        +DateTime connectionTime
    }

    class AccountDeleted {
        +String reason
        +DateTime deletionDate
        +UUID deletedById
    }

    class CustomerCreated {
        +Address address
    }

    class EmployeeCreated {
        +EmployeeRole initialRole
    }

    class EmployeePromoted {
        +EmployeeRole newRole
    }

    User <|-- Employee
    User <|-- Customer
    User "1" -- "1" PersonalInfo
    Customer "1" -- "*" Address
    UserEvent <|-- UserConnected
    UserEvent <|-- AccountDeleted
    UserEvent <|-- CustomerCreated
    UserEvent <|-- EmployeeCreated
    UserEvent <|-- EmployeePromoted

    note for User "connect() est maintenant une méthode commune\npour tous les types d'utilisateurs"
```

## Product Domain

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

### Stock sub-domain
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


## Catalog Domain

Nous avons décidé de considérer le Catalogue comme un domaine distinct plutôt qu'un sous-domaine de Produit. Voici pourquoi :

1. **Responsabilités distinctes** :
    - Le domaine Produit gère les informations intrinsèques des produits, les lots, les stocks
    - Le domaine Catalogue gère la présentation commerciale, les promotions, la visibilité
2. **Cycles de vie différents** :
    - Un produit peut exister sans être dans le catalogue
    - Les changements de catalogue (promotions, visibilité) n'impactent pas les données du produit
    - Les règles métier sont différentes (ex: gestion des stocks vs gestion des promotions)
3. **Équipes potentiellement différentes** :
    - Produit/Stock : équipe logistique
    - Catalogue : équipe marketing/commerciale

### Core Domain 

```mermaid
classDiagram
    class Catalog {
        +UUID id
        +String name
        +List~CatalogItem~ items
        +addProduct(Product product)
        +removeProduct(UUID productId)
        +updateProductStatus(UUID productId, CatalogStatus status)
        +applyPromotion(UUID productId, Promotion promotion)
    }

    class CatalogItem {
        +UUID id
        +UUID productId
        +Double displayPrice
        +Integer viewCount
        +CatalogStatus status
        +DateTime availableFrom
        +DateTime availableTo
        +Promotion currentPromotion
        +incrementViews()
        +updateStatus(CatalogStatus newStatus)
        +applyPromotion(Promotion promotion)
    }

    class CatalogStatus {
        <<enumeration>>
        AVAILABLE
        COMING_SOON
        LIMITED_QUANTITY
        UNAVAILABLE
        REMOVED
    }

    class Promotion {
        +UUID id
        +Double discountPercentage
        +DateTime startDate
        +DateTime endDate
        +isValid()
    }

    class CatalogEvent {
        <<interface>>
        +UUID catalogId
        +DateTime timestamp
    }

    class ProductAddedToCatalog {
        +UUID productId
        +CatalogStatus initialStatus
    }

    class ProductRemovedFromCatalog {
        +UUID productId
        +String reason
    }

    class PromotionApplied {
        +UUID productId
        +Double oldPrice
        +Double newPrice
        +Double discountPercentage
    }

    class CatalogStatusChanged {
        +UUID productId
        +CatalogStatus oldStatus
        +CatalogStatus newStatus
    }

    Catalog "1" -- "*" CatalogItem
    CatalogItem "1" -- "0..1" Promotion
    CatalogEvent <|-- ProductAddedToCatalog
    CatalogEvent <|-- ProductRemovedFromCatalog
    CatalogEvent <|-- PromotionApplied
    CatalogEvent <|-- CatalogStatusChanged

    note for Catalog "Gère la visibilité des produits"
    note for CatalogItem "Représente un produit dans le catalogue"
    note for Promotion "Gère les réductions temporaires"
```

## Cart Domain
Le Panier (Cart) devrait être un domaine distinct plutôt qu'un sous-domaine du Payment car :
1. **Cycle de vie différent**
    - Le panier existe avant tout paiement
    - Le panier peut être annulé sans paiement
    - Un panier peut être sauvegardé/repris entre sessions
2. **Responsabilités spécifiques**
    - Gestion temporaire des produits sélectionnés
    - Calcul des totaux
    - Vérification des disponibilités
    - États transitoires avant paiement

```mermaid
classDiagram
    class Cart {
        +UUID id
        +UUID customerId
        +List~CartItem~ items
        +CartStatus status
        +DateTime createdAt
        +DateTime updatedAt
        +Money totalAmount
        +addItem(Product product, int quantity)
        +removeItem(UUID productId)
        +updateQuantity(UUID productId, int quantity)
        +validate()
        +cancel()
    }

    class CartItem {
        +UUID id
        +UUID productId
        +String productName
        +Integer quantity
        +Money unitPrice
        +Money totalPrice
        +updateQuantity(int quantity)
    }

    class CartStatus {
        <<enumeration>>
        ACTIVE
        VALIDATED
        CANCELLED
    }

    class CartEvent {
        <<interface>>
        +UUID cartId
        +DateTime timestamp
    }

    class ItemAddedToCart {
        +UUID productId
        +Integer quantity
        +Money price
    }

    class ItemRemovedFromCart {
        +UUID productId
        +Integer quantity
    }

    class CartValidated {
        +Money totalAmount
        +Integer totalItems
    }

    class CartCancelled {
        +String reason
    }

    class Money {
        +BigDecimal amount
        +Currency currency
    }

    Cart "1" -- "*" CartItem
    Cart --> CartStatus
    CartEvent <|-- ItemAddedToCart
    CartEvent <|-- ItemRemovedFromCart
    CartEvent <|-- CartValidated
    CartEvent <|-- CartCancelled
    CartItem --> Money
    Cart --> Money

    note for Cart "Agrégat racine gérant l'état du panier"
    note for CartEvent "Événements émis lors des modifications du panier"
```

## Payment Domain

### Core Domain

```mermaid
classDiagram
    class Order {
        +UUID id
        +UUID customerId
        +List~OrderItem~ items
        +Money totalAmount
        +OrderStatus status
        +DateTime createdAt
        +PaymentDetails paymentDetails
        +validate()
        +archive()
    }

    class OrderStatus {
        <<enumeration>>
        CREATED
        PAYMENT_PENDING
        PAYMENT_COMPLETED
        PAYMENT_FAILED
        PAYMENT_CANCELLED
        VALIDATED
        ARCHIVED
    }

    class OrderItem {
        +UUID productId
        +String productName
        +Integer quantity
        +Money unitPrice
        +Money totalPrice
    }

    class PaymentDetails {
        +UUID id
        +String paypalTransactionId
        +PaymentStatus status
        +DateTime initiatedAt
        +DateTime completedAt
        +Money amount
    }

    class PaymentStatus {
        <<enumeration>>
        INITIATED
        COMPLETED
        FAILED
        CANCELLED
    }

    class PayPalAdapter {
        +initiatePayment(Order order)
        +handlePayPalCallback(PayPalWebhookEvent event)
        +validatePayment(String transactionId)
        -validatePayPalResponse(PayPalResponse response)
    }

    class PaymentEvent {
        <<interface>>
        +UUID orderId
        +DateTime timestamp
    }

    class PaymentInitiated {
        +String paypalTransactionId
        +Money amount
    }

    class PaymentCompleted {
        +String paypalTransactionId
        +DateTime completedAt
    }

    class PaymentFailed {
        +String reason
        +DateTime failedAt
    }

    class PurchaseHistory {
        +UUID customerId
        +List~Order~ orders
        +addOrder(Order order)
        +getCustomerOrders()
    }

    Order "1" -- "*" OrderItem
    Order --> OrderStatus
    Order "1" -- "1" PaymentDetails
    PaymentDetails --> PaymentStatus
    Order ..> PayPalAdapter : utilise
    PurchaseHistory "1" -- "*" Order

    note for Order "Agrégat racine du domaine Payment"
    note for PayPalAdapter "Anti-Corruption Layer pour PayPal"
    note for PurchaseHistory "Gestion de l'historique des achats"
```

### Paypal Identity Anti-Corruption Layer
```mermaid
classDiagram
    %% Notre modèle de domaine (simplifié)
    class User {
        <<abstract>>
        +UUID id
        +String email
        +PersonalInfo personalInfo
        +UserStatus status
    }

    class Customer {
        +List~Address~ addresses
    }

    class PersonalInfo {
        +String firstName
        +String lastName
        +String phoneNumber
    }

    %% Anti-Corruption Layer
    class PayPalIdentityAdapter {
        +PayPalIdentity toPayPalIdentity(User user)
        +void updateFromPayPalIdentity(User user, PayPalIdentity paypalIdentity)
        -validatePayPalData(PayPalIdentity identity)
    }

    class PayPalIdentityTranslator {
        +PayPalName translateName(PersonalInfo info)
        +PayPalAddress translateAddress(Address address)
        +PersonalInfo translateToPersonalInfo(PayPalName name)
        +Address translateToAddress(PayPalAddress address)
    }

    %% Modèle PayPal (DTO)
    class PayPalIdentity {
        <<external>>
        +String id
        +String externalId
        +String userName
        +PayPalName name
        +Boolean active
        +List~PayPalEmail~ emails
        +List~PayPalPhone~ phoneNumbers
        +List~PayPalAddress~ addresses
    }

    class PayPalName {
        <<external>>
        +String familyName
        +String givenName
        +String middleName
        +String honorificPrefix
        +String honorificSuffix
    }

    class PayPalAddress {
        <<external>>
        +String streetAddress
        +String locality
        +String region
        +String postalCode
        +String country
        +String type
    }

    User <|-- Customer
    User "1" -- "1" PersonalInfo
    PayPalIdentityAdapter ..> User : traduit
    PayPalIdentityAdapter ..> PayPalIdentity : traduit
    PayPalIdentityAdapter --> PayPalIdentityTranslator : utilise
    
    note for PayPalIdentityAdapter "Isole notre domaine User\ndu format PayPal Identity"
    note for PayPalIdentityTranslator "Gère les règles de traduction\nentre les deux modèles"
```


## Analytics Domain

```mermaid
classDiagram
    class Report {
        +UUID id
        +ReportType type
        +DateRange period
        +DateTime generatedAt
        +List~KPI~ kpis
        +ReportStatus status
        +generate()
        +archive()
    }

    class ReportType {
        <<enumeration>>
        DAILY
        WEEKLY
        MONTHLY
        YEARLY
    }

    class DateRange {
        +DateTime startDate
        +DateTime endDate
        +boolean includes(DateTime date)
    }

    class KPI {
        +UUID id
        +String name
        +String description
        +KPIType type
        +Value currentValue
        +Value previousValue
        +Double variation
        +calculate()
    }

    class KPIType {
        <<enumeration>>
        SALES_REVENUE
        AVERAGE_BASKET
        TOP_PRODUCTS
        STOCK_TURNOVER
        CUSTOMER_SATISFACTION
        PROFIT_MARGIN
    }

    class Value {
        +Double amount
        +String unit
        +DateTime computedAt
    }

    class MetricsCollector {
        +collectSalesMetrics(DateRange range)
        +collectStockMetrics(DateRange range)
        +collectCustomerMetrics(DateRange range)
        +aggregateMetrics(List~Metric~ metrics)
    }

    class AnalyticsEvent {
        <<interface>>
        +UUID reportId
        +DateTime timestamp
    }

    class ReportGenerated {
        +ReportType type
        +DateRange period
        +List~KPI~ summary
    }

    class PeriodClosed {
        +ReportType type
        +DateRange period
    }

    Report "1" -- "1" DateRange
    Report "1" -- "*" KPI
    KPI "1" -- "1" KPIType
    KPI "1" -- "2" Value
    Report ..> MetricsCollector : utilise
    AnalyticsEvent <|-- ReportGenerated
    AnalyticsEvent <|-- PeriodClosed

    note for Report "Agrégat racine qui représente\nun rapport de KPIs"
    note for MetricsCollector "Collecte les données des\nautres domaines"
    note for KPI "Représente un indicateur\nde performance clé"
```

### Key Performance Indicators

Description des KPIs choisis:

1. **SALES_REVENUE (Chiffre d'affaires)**
    - Calcul : Somme de tous les OrderStatus.VALIDATED sur la période
    - Source : Payment Domain (Orders)
    - Pertinence :
        - Mesure directe de la performance commerciale
        - Permet de suivre la croissance des ventes
        - Base pour le calcul d'autres métriques
2. **AVERAGE_BASKET (Panier moyen)**
    - Calcul : SALES_REVENUE / Nombre de commandes validées
    - Source : Payment Domain (Orders)
    - Pertinence :
        - Indicateur du comportement d'achat
        - Permet d'évaluer l'efficacité des promotions
        - Aide à la stratégie de pricing
3. **STOCK_TURNOVER (Rotation des stocks)**
    - Calcul : (Quantité vendue / Quantité moyenne en stock) sur la période
    - Sources :
        - Product Domain (Stock)
        - Payment Domain (OrderItems)
    - Pertinence :
        - Optimisation de la gestion des stocks
        - Identification des produits à fort/faible roulement
        - Aide à la prévision des réapprovisionnements
4. **TOP_PRODUCTS (Produits les plus vendus)**
    - Calcul : Classement des produits par quantité vendue
    - Sources :
        - Payment Domain (OrderItems)
        - Catalog Domain (consultations)
    - Pertinence :
        - Guide les décisions de réapprovisionnement
        - Aide au placement des produits dans le catalogue
        - Identification des tendances
5. **PROFIT_MARGIN (Marge bénéficiaire)**
    - Calcul : (SALES_REVENUE - Coût total des produits vendus) / SALES_REVENUE × 100
    - Sources :
        - Payment Domain (Orders)
        - Product Domain (coût des produits)
    - Pertinence :
        - Mesure de la rentabilité
        - Aide à la politique de prix
        - Évaluation de l'efficacité des promotions

Ces KPIs forment un ensemble cohérent pour :

- Suivre la performance commerciale (SALES_REVENUE, PROFIT_MARGIN)
- Optimiser les opérations (STOCK_TURNOVER)
- Comprendre le comportement client (AVERAGE_BASKET)
- Guider les décisions produit (TOP_PRODUCTS)

