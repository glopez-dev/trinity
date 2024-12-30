# User Domain Model

## Diagramme de classes (UML)

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
### Anti-Corruption Layer (Paypal Identity)

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




## Description du _Domain Model_

- Agrégat Racine : `User` (classe abstraite)
  - Attributs de base : id, email, password, personalInfo, status, createdAt, updatedAt, lastLoginAt
  - Méthodes communes : connect(), delete()
  - Types d'utilisateurs :
    - `Employee` : Utilisateur interne avec rôle et dates d'emploi
    - `Customer` : Client avec gestion d'adresses

- Value Objects :
  - `PersonalInfo` : Informations personnelles (firstName, lastName, phoneNumber)
  - `Address` : Adresse postale (street, zipCode, city, country, isDefault)

- Énumérations :
  - `EmployeeRole` : Définit les rôles des employés (STORE_MANAGER, CASHIER)
  - `UserStatus` : États possibles d'un utilisateur (ACTIVE, INACTIVE, DELETED)

- Anti-Corruption Layer :
  - `PayPalAdapter` : Gère l'intégration avec PayPal
    - Méthodes : initiatePayment(), handlePayPalCallback(), validatePayment()
  - `PayPalIdentityAdapter` : Gère la traduction des identités utilisateurs
    - Méthodes : toPayPalIdentity(), updateFromPayPalIdentity()
  - `PayPalIdentityTranslator` : Traduit les données d'identité entre notre domaine et PayPal

- Événements de Domaine :
  - `UserEvent` (interface) : Base pour tous les événements utilisateur
  - `UserConnected` : Trace les connexions (ipAddress, userAgent, connectionTime)
  - `AccountDeleted` : Suivi des suppressions de compte (reason, deletionDate, deletedById)
  - `CustomerCreated` : Création d'un nouveau client
  - `EmployeeCreated` : Création d'un nouvel employé (initialRole)
  - `EmployeePromoted` : Promotion d'un employé (newRole)

- Règles Métier Principales :
  1. Un utilisateur doit avoir une adresse email unique
  2. Les mots de passe doivent être stockés de manière sécurisée
  3. Les employés doivent avoir un rôle défini
  4. Un client peut avoir plusieurs adresses mais une seule par défaut
  5. La traçabilité des connexions et modifications doit être maintenue
  6. La suppression d'un compte doit être tracée avec la raison et l'auteur

- Comportements Spécifiques :
  - Employee :
    - `promote()` : Gestion des promotions
    - `deactivate()` : Désactivation d'un compte employé
    - `update()` : Mise à jour des informations employé
  - Customer :
    - `updateProfile()` : Mise à jour du profil client

## Organisation du package Java
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

