# Domaine Payment

## Diagramme de classes (UML)

### Core Domain (Payment)

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


## Description du _Domain Model_
- Agrégat Racine : `Order`
  - Attributs clés : id, customerId, items, totalAmount, status, createdAt, paymentDetails
  - Méthodes principales : validate(), archive()
  - Énumérations associées : OrderStatus (CREATED, PAYMENT_PENDING, PAYMENT_COMPLETED, etc.)

- Objets-Valeurs :
  - `OrderItem` : Représente un item dans la commande (productId, productName, quantity, unitPrice, totalPrice)
  - `PaymentDetails` : Détails du paiement (id, paypalTransactionId, status, initiatedAt, completedAt, amount)
  - `Money` : Représentation des montants monétaires


- Événements de Domaine :
  - `PaymentEvent` (interface)
  - `PaymentInitiated` : Déclenché lors de l'initiation du paiement
  - `PaymentCompleted` : Déclenché lors de la completion du paiement
  - `PaymentFailed` : Déclenché en cas d'échec du paiement

- Entités Associées :
  - `PurchaseHistory` : Gestion de l'historique des achats par client
    - Méthodes : addOrder(), getCustomerOrders()

- Règles Métier Principales :
  1. Une commande doit être validée avant le processus de paiement
  2. Le statut de paiement doit suivre une progression logique (INITIATED -> COMPLETED/FAILED)
  3. Les détails de paiement doivent être conservés pour audit
  4. L'historique des achats doit être maintenu par client

#### Domaine Catalog
- Agrégat Racine : `Catalog`
- Objets-Valeurs : `CatalogItem`, `Promotion`
- Événements de Domaine : `ProductAddedToCatalog`, `ProductRemovedFromCatalog`, `PromotionApplied`

### Mises à jour de la Documentation
- Ajout des guides d'implémentation par contexte borné
- Documentation des invariants d'agrégats et règles métier
- Création des spécifications techniques pour les objets-valeurs
- Ajout des patterns d'intégration entre domaines
