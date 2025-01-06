# Diagrammes C4

## Ressources
https://www.alexandrevandekerkhove.fr/2020/02/10/c4model-pour-les-diags-darchi.html
https://www.alexandrevandekerkhove.fr/2019/03/20/architecture-modulaire-microservices-on-en-est-ou.html

## C4
C4 est l’acronyme de **C**ontext, **C**ontainer, **C**omponent et **C**ode.

L’élément central du C4 model est l’approche en **différents niveaux de zoom** de la représentation du système à modéliser. Ces vues permettent d’avoir des détails précis ou au contraire une vue globale.

### Niveau 1 : Contexte

Le 1er niveau est un diagramme extrêmement simple, qui permet de visualiser le(s) application(s) à modéliser dans leur écosystème

```mermaid
graph TB
    Client[Client Mobile<br>Personne faisant ses courses<br>dans le magasin]
    Manager[Manager Magasin<br>Personne gérant le<br>magasin au quotidien]
    
    Trinity[Système Trinity<br><br>Permet aux clients de scanner<br>et payer leurs produits, et aux<br>managers de gérer les stocks<br>et suivre les ventes]
    
    PaymentSystem[Système de Paiement<br><br>Système externe gérant<br>les transactions bancaires]
    
    ProductDB[Base Produits<br><br>Base de données externe<br>des informations produits]

    Client -->|Scanne les produits<br>et effectue ses achats| Trinity
    Manager -->|Gère les stocks et<br>consulte les rapports| Trinity
    Trinity -->|Vérifie les paiements| PaymentSystem
    Trinity -->|Obtient les informations<br>produits| ProductDB

    classDef person fill:#08427b,stroke:#052e56,color:#ffffff
    classDef system fill:#1168bd,stroke:#0b4884,color:#ffffff
    classDef external fill:#666666,stroke:#0b4884,color:#ffffff

    class Client,Manager person
    class Trinity system
    class PaymentSystem,ProductDB external
```

### Niveau 2 : Conteneur

 Cette vue permet de visualiser les différentes briques logicielles qui composent le système modélisé.

```mermaid
graph TB
    MobileClient["Mobile App\nContainer: React Native\nFournit l'interface mobile\npour les clients"]
    WebClient["Web Application\nContainer: React.js\nFournit l'interface web\npour les managers"]
    
    API["API Application\nContainer: Spring Boot\nFournit les fonctionnalités\nvia API JSON/HTTPS"]
    
    Database[("Base de données\nContainer: PostgreSQL\nStocke toutes les\ndonnées du système")]
    
    MessageBroker["Message Broker\nContainer: RabbitMQ\nGère la communication\nasynchrone"]
    
    PayPal["PayPal API\nSystème externe\nGestion des paiements"]
    OpenFood["Open Food Facts API\nSystème externe\nDonnées produits"]

    MobileClient -->|"JSON/HTTPS"| API
    WebClient -->|"JSON/HTTPS"| API
    
    API -->|"JDBC"| Database
    API -->|"AMQP"| MessageBroker
    API -->|"JSON/HTTPS"| PayPal
    API -->|"JSON/HTTPS"| OpenFood

    classDef client fill:#1168bd,stroke:#0b4884,color:#ffffff
    classDef api fill:#1168bd,stroke:#0b4884,color:#ffffff
    classDef db fill:#2b4c8c,stroke:#0b4884,color:#ffffff
    classDef external fill:#666666,stroke:#0b4884,color:#ffffff
    classDef broker fill:#1168bd,stroke:#0b4884,color:#ffffff

    class MobileClient,WebClient client
    class API api
    class Database db
    class PayPal,OpenFood external
    class MessageBroker broker
```
 
Les technologies utilisées sont écrites, les interactions sont également plus précises en terme de protocole et format

Attention : Un conteneur est une unité d'exécution (un processus séparé) notre monolithe modulaire est donc représenté comme un seul conteneur.

### Niveau 3 : Composants
Le 3ème niveau, “composant”, décrit l’architecture locale d’une des briques logicielles. Le _conteneur_ (voir niveau 2) est découpé sous la forme de multiples composants. Chaque composant représente une fonctionnalité du conteneur.

```mermaid
graph TB
    subgraph Clients ["Applications Clientes"]
        WebApp["Application Web\n[Conteneur: React.js]"]
        MobileApp["Application Mobile\n[Conteneur: React Native]"]
    end

    subgraph API Application ["Application API\n[Conteneur: Spring Boot]"]
        SalesController["Contrôleur des Ventes\n[Composant: RestController]\nGère les opérations de vente\net les paniers"]
        
        ProductController["Contrôleur des Produits\n[Composant: RestController]\nGère le catalogue et\nles stocks"]
        
        PaymentController["Contrôleur des Paiements\n[Composant: RestController]\nGère les transactions\net paiements"]

        UserController["Contrôleur Utilisateurs\n[Composant: RestController]\nGère les comptes et\nles profils"]

        AnalyticsController["Contrôleur Analytics\n[Composant: RestController]\nGère les rapports et\nles indicateurs"]
        
        SecurityComponent["Composant Sécurité\n[Composant: Spring Security]\nGère l'authentification\net les autorisations"]
        
        RabbitConfig["Configuration RabbitMQ\n[Composant: Configuration]\nConfigure les échanges\net les files d'attente"]

        EventListeners["Écouteurs d'Événements\n[Composant: Component]\nTraite les événements\nasynchrones"]
    end

    subgraph External ["Systèmes Externes"]
        Queue[("File de Messages\n[Conteneur: RabbitMQ]")]
        DB[("Base de données\n[Conteneur: PostgreSQL]")]
        PayPalAPI["API PayPal\n[Système Externe]"]
        OpenFoodAPI["API Open Food Facts\n[Système Externe]"]
    end

    WebApp & MobileApp -->|"REST/HTTPS"| SalesController
    WebApp & MobileApp -->|"REST/HTTPS"| ProductController
    WebApp & MobileApp -->|"REST/HTTPS"| PaymentController
    WebApp & MobileApp -->|"REST/HTTPS"| UserController
    WebApp -->|"REST/HTTPS"| AnalyticsController

    SalesController --> SecurityComponent
    ProductController --> SecurityComponent
    PaymentController --> SecurityComponent
    UserController --> SecurityComponent
    AnalyticsController --> SecurityComponent

    SalesController & ProductController & PaymentController & UserController & AnalyticsController -->|"Publie des événements"| RabbitConfig

    RabbitConfig -->|"Configure"| Queue
    Queue -->|"Consomme"| EventListeners
    
    SecurityComponent -->|"Lit/Écrit"| DB
    PaymentController -->|"Appelle l'API"| PayPalAPI
    ProductController -->|"Récupère les données produits"| OpenFoodAPI

    classDef controller fill:#d44242,stroke:#333,color:#ffffff
    classDef component fill:#2a9d2a,stroke:#333,color:#ffffff
    classDef external fill:#666666,stroke:#333,color:#ffffff
    classDef client fill:#1168bd,stroke:#333,color:#ffffff

    class SalesController,ProductController,PaymentController,UserController,AnalyticsController controller
    class SecurityComponent,RabbitConfig,EventListeners component
    class Queue,DB,PayPalAPI,OpenFoodAPI external
    class WebApp,MobileApp client
```
