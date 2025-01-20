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


