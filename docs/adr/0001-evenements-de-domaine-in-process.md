# ADR-0001 — Communication inter-modules par événements de domaine in-process

- **Statut** : acceptée (juin 2026)
- **Décideurs** : équipe backend Trinity

## Contexte

Le backend est un monolithe modulaire Spring Boot découpé en bounded contexts
(`authentication`, `cart`, `payment`, `product`, `user`) avec un noyau partagé
(`common`). Le design initial (diagrammes C4) prévoyait un broker RabbitMQ pour
la communication inter-modules, mais celui-ci n'a jamais été implémenté : aucun
producteur, aucun consommateur, aucune dépendance AMQP n'existait dans le code.

L'analyse du couplage (juin 2026) a montré que la seule interaction métier
asynchronisable est le flux de validation du panier (cart → product pour le
stock, cart → payment pour le paiement). Le déploiement est mono-instance
(`replicas: 1`), la volumétrie cible est faible (~10 000 transactions/jour) et
l'équipe (5 personnes) n'opère qu'un PostgreSQL.

## Décision

La communication inter-modules se fait par **événements de domaine in-process** :

- L'événement est un record Java **pur** (aucun import framework), **possédé par
  le module producteur** dans son package `domain/event`
  (ex. `cart/domain/event/CartValidatedEvent`). Il est **autoportant** :
  identifiants + données dénormalisées, jamais de référence vivante vers les
  agrégats du producteur.
- La **couche application** du producteur le publie via
  `ApplicationEventPublisher`, à l'intérieur de la transaction du cas d'usage.
- Les consommateurs utilisent `@TransactionalEventListener(phase = AFTER_COMMIT)`
  avec `@Transactional(propagation = REQUIRES_NEW)` : ils ne s'exécutent que si
  la transaction du producteur a réellement committé, et leurs écritures vivent
  dans leur propre transaction.
- Les consommateurs importent l'événement du producteur, **jamais l'inverse** ;
  chaque dépendance est inscrite dans la whitelist du test d'architecture
  (`ArchitectureTest.CROSS_MODULE_WHITELIST`), dans le commit qui l'exige.

Le backend reste un service RESTful **stateless** : les événements sont
consommés de façon synchrone dans le cycle de vie de la requête HTTP, aucun
état applicatif ne vit en mémoire entre deux requêtes.

## Conséquences

- **Positives** : découplage logique des modules sans nouvelle infrastructure ;
  cohérence transactionnelle native (pas de dual-write/outbox) ; types Java
  refactor-safe ; stack traces complètes ; déboguage local trivial.
- **Négatives / assumées** : sémantique **at-most-once après commit** — si le
  processus meurt entre le commit du producteur et l'exécution du listener,
  l'événement est perdu (acceptable pour le périmètre actuel ; l'Event
  Publication Registry de Spring Modulith fournirait du at-least-once persisté
  le jour où ce besoin devient réel). Les exceptions levées par un listener
  AFTER_COMMIT sont avalées par Spring : chaque listener loggue et isole ses
  échecs ligne par ligne.

## Critères de réouverture (vers un broker externe type RabbitMQ)

Réexaminer cette décision si **l'un** de ces critères devient vrai :

1. Le backend passe à **plus d'un réplica** avec des traitements à distribuer
   (competing consumers) ou à diffuser à toutes les instances.
2. Un module est **extrait en service séparé** : l'événement doit traverser le
   réseau.
3. Un **consommateur hors process** apparaît (webhooks entrants à traiter en
   différé, service analytics externe).
4. Un besoin réel de **durabilité/rejeu ou de lissage de pics** est démontré.

Chemin de migration sans regret : `ApplicationEventPublisher` (aujourd'hui) →
Spring Modulith Event Publication Registry (durabilité) → externalisation
`@Externalized` vers AMQP/Kafka (distribution), sans réécrire le code métier —
les événements sont déjà sérialisables et autoportants.
