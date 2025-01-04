# Module Payment (Core Domain)

## Structure de fichiers envisagée (Hexagonal Architecture et DDD)

```
payment/
├── domain/
│   ├── model/
│   │   ├── aggregate/
│   │   │   └── Order.java // Agrégat racine
│   │   ├── entity/
│   │   │   ├── OrderItem.java
│   │   │   └── PaymentDetails.java
│   │   ├── valueobject/
│   │   │   ├── Money.java
│   │   │   ├── OrderId.java
│   │   │   ├── OrderStatus.java
│   │   │   └── PaymentStatus.java
│   │   └── event/
│   │       ├── OrderCreatedEvent.java
│   │       ├── PaymentCompletedEvent.java
│   │       └── PaymentFailedEvent.java
│   ├── service/
│   │   └── PaymentDomainService.java // Logique métier complexe
│   └── port/
│       ├── incoming/
│       │   ├── IInitiatePaymentUseCase.java
│       │   ├── IConfirmPaymentUseCase.java
│       │   └── IRefundPaymentUseCase.java
│       └── outgoing/
│           ├── ILoadOrderPort.java
│           ├── ISaveOrderPort.java
│           ├── IPaymentProviderPort.java
│           └── IPaymentEventPublisherPort.java
├── application/
│   ├── usecase/
│   │   ├── InitiatePaymentUseCase.java
│   │   ├── ConfirmPaymentUseCase.java
│   │   └── RefundPaymentUseCase.java
│   ├── command/
│   │   ├── InitiatePaymentCommand.java
│   │   └── RefundPaymentCommand.java
│   └── response/
│       └── PaymentResponse.java
└── infrastructure/
    ├── incoming/
    │   ├── web/
    │   │   ├── PaymentController.java
    │   │   └── dto/
    │   │       ├── PaymentRequest.java
    │   │       └── PaymentResponse.java
    │   └── messaging/
    │       └── OrderCreatedEventListener.java
    └── outgoing/
        ├── persistence/
        │   ├── OrderJpaRepository.java
        │   └── entity/
        │       └── OrderEntity.java
        ├── messaging/
        │   └── RabbitMQEventPublisher.java
        └── paypal/
            ├── PayPalAdapter.java // Implements IPaymentProviderPort
            ├── PayPalOrderTranslator.java // Anti-Corruption Layer
            └── dto/
                ├── PayPalOrderDTO.java
                └── PayPalResponseDTO.java
```