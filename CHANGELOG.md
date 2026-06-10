# Changelog

Tous les changements notables de ce projet sont documentés dans ce fichier.

Le format suit [Keep a Changelog](https://keepachangelog.com/fr/1.1.0/) et le
projet adhère au [Versionnage Sémantique](https://semver.org/lang/fr/).

## [Non publié] — 2026-06-10

Chantier « monolithe modulaire » (branche `gabriel/ddd-refactoring`) : câblage
du flux d'achat par événements de domaine in-process, verrouillage des
frontières de modules et correction des défauts relevés par l'analyse
d'architecture. Décision actée dans
[ADR-0001](docs/adr/0001-evenements-de-domaine-in-process.md).

### ⚠️ Changements cassants (API mobile/web)

- `PUT/DELETE /api/v1/carts/{customerId}/items` : le corps devient
  `{productId, quantity}` — le nom et le prix ne sont **plus acceptés du
  client**, ils sont résolus côté serveur depuis le catalogue produit.
- `POST /api/v1/stripe/checkout` : les lignes deviennent
  `{productId, quantity}` (mêmes raisons) ; la devise vient de la
  configuration serveur.
- Route panier : `/api/V1/carts` → `/api/v1/carts` (casse normalisée ; les
  chemins HTTP sont sensibles à la casse).
- Inscription : les champs `role` (employé) et `type` (client) sont retirés
  des corps de requête — ils n'étaient jamais lus par le serveur.
- `GET /api/v1/hello` est supprimé (endpoint de test redondant avec
  `/actuator/health`).
- `CustomerResponse` ne contient plus `tokenExpiresAt` ni `tokenExpired`.

### Ajouté

- **Flux d'achat serveur** : la validation du panier publie l'événement de
  domaine `CartValidatedEvent` ; un écouteur transactionnel dans `product`
  décrémente le stock vendu après commit (`CartValidationStockIT` prouve le
  flux de bout en bout). Le backend reste stateless : événements in-process,
  aucun broker.
- **Persistance des paiements** : agrégat `Payment` persisté dans la nouvelle
  table `payments` (migration Flyway V2) — trace `PENDING` avant l'appel
  Stripe, résultat ou échec persisté après ; base de la réconciliation.
- **Résolution des prix côté serveur** : ports `ProductInfoPort` (cart) et
  `ProductPricingPort` (payment) adossés au module produit.
- Endpoint `POST /api/v1/carts/{customerId}/cancel` : vide le panier sans le
  supprimer.
- **Règles d'architecture inter-modules** (ArchUnit) : un module n'importe
  d'un autre module que sa surface publique whitelistée ; le noyau partagé
  `common` n'importe aucun module métier ; la couche `application` ne dépend
  jamais de la couche `interfaces`.
- `ExternalServiceException` (→ HTTP 502) pour les pannes de services tiers.
- Timeouts de connexion/réponse sur le client HTTP OpenFoodFacts.
- `docs/adr/0001-evenements-de-domaine-in-process.md` : décision « événements
  in-process, pas de RabbitMQ » avec critères de réouverture explicites.
- Test de non-régression `OpenApiDocIT` sur le document OpenAPI généré.
- **Spring Modulith** : chaque module déclare ses dépendances autorisées
  (`package-info.java`, named interfaces `user::model`, `user::ports`,
  `cart::events`, `cart::ports`, `product::api`, `product::model` ; `common`
  en module ouvert). `ModularityTest.verify()` rejette tout cycle et toute
  dépendance non déclarée, et `Documenter` génère les diagrammes C4 réels du
  graphe de modules dans `target/spring-modulith-docs`.

### Corrigé

- **Filtre JWT** : la chaîne de filtres continue toujours sur le chemin
  nominal ; un token expiré/malformé ou un utilisateur inconnu renvoie un
  **401** propre (auparavant : requête avalée ou erreur 500).
- **Codes HTTP du module produit** : produit introuvable → **404**, données
  invalides → **409**, panne OpenFoodFacts → **502** (auparavant : 500 pour
  tout, les exceptions n'étant pas rattachées à la hiérarchie commune).
- Plus aucune transaction base de données maintenue pendant un appel HTTP
  externe (Stripe, OpenFoodFacts).
- Le contexte PayPal (`APIContext`) est un singleton : fin de la
  ré-authentification OAuth à chaque opération de facturation.
- Les origines CORS sont entièrement externalisées dans la configuration
  (`app.cors.allowed-origins`) — plus de `localhost` codé en dur fusionné en
  production.
- L'API panier apparaît désormais dans Swagger (l'ancienne route `/api/V1`
  échappait au filtre `pathsToMatch` de springdoc, sensible à la casse) et
  ses endpoints sont documentés (`@Tag`, `@Operation`, schémas).

### Modifié

- **Module `authentication` hexagonalisé** (interfaces/rest, application,
  infrastructure/security) ; il absorbe `AppUserDetails`, dont il était
  l'unique consommateur — plus aucune dépendance vers l'infrastructure du
  module `user`.
- Le bean `PasswordEncoder` appartient au module `user` (son consommateur)
  au lieu de la configuration de sécurité d'`authentication`.
- **Couche application sans DTO REST** dans les cinq modules : les services
  applicatifs parlent domaine (ou commands applicatifs), le mapping DTO se
  fait dans les controllers via les mappers MapStruct.
- `scanProduct` réécrit en domaine pur : les valeurs par défaut d'import
  vivent dans `Product.applyImportDefaults()` (plus d'aller-retour
  DTO→DTO→domaine).
- Devise par défaut centralisée dans `Money.DEFAULT_CURRENCY`.
- Documentation DDD réalignée sur le code : les diagrammes C4 ne montrent
  plus de broker RabbitMQ (jamais implémenté) mais les événements in-process
  réels.
- L'adaptateur `ProductInfoAdapter` (port produit du panier) est hébergé par
  le module `product` : le graphe de modules devient acyclique
  (product → cart, payment → product, authentication → user), condition de la
  vérification Modulith.

### Supprimé

- Champs Stripe OAuth de `Customer` (`stripeUserId`, tokens, expiration) :
  préoccupation paiement jamais lue par aucun code — colonnes supprimées par
  la migration Flyway V3.
- `CartService.getTotalAmount` (aucun appelant, redondant avec `getCart`).
- `CartRequest` (DTO de réponse mal nommé), remplacé par
  `CartResponse`/`CartItemResponse` à JSON identique.
