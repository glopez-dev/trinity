# Structure des Issues Agile

## Vue d'ensemble

Dans GitLab, la gestion Agile des projets s'organise en trois niveaux hiérarchiques :

1. **Epics** : Grands objectifs ou initiatives
2. **User Stories** : Fonctionnalités du point de vue utilisateur
3. **Tasks** : Sous-tâches techniques

## Epics

### Description
- Un Epic représente une initiative majeure ou un objectif important du projet
- Il peut s'étendre sur plusieurs sprints ou itérations
- Il regroupe plusieurs User Stories liées

### Format
```markdown
Title: [EPIC] Titre de l'Epic

**Objectif**
Description claire de l'objectif global

**User Stories Liées**
* US 1
* US 2
* US 3

**Critères d'Acceptation**
* Critère 1
* Critère 2

**Documentation Attendue**
1. Doc 1
2. Doc 2

**Points d'Attention**
* Point 1
* Point 2
```

## User Stories

### Description
- Décrit une fonctionnalité du point de vue de l'utilisateur
- Doit être réalisable dans un sprint
- Est liée à un Epic

### Format
```markdown
Title: [US] Titre de la User Story

/epic #NUMERO_EPIC
/label ~"User Story" ~Labels-Pertinents
/estimate Xd
/milestone "Nom du Sprint/Milestone"

En tant que [rôle]
Je veux [objectif]
Afin de [bénéfice]

Critères d'acceptation:
- Critère 1
- Critère 2
- Critère 3

Tasks:
- [ ] [TASK] Nom de la tâche 1
  - Sous-tâche 1.1
  - Sous-tâche 1.2
- [ ] [TASK] Nom de la tâche 2
  - Sous-tâche 2.1
  - Sous-tâche 2.2
```

## Tasks

### Description
- Les tasks sont intégrées directement dans la User Story
- Elles utilisent les cases à cocher GitLab pour le suivi
- Chaque task peut avoir des sous-tâches détaillées

### Format dans la User Story
```markdown
Tasks:
- [ ] [TASK] Nom de la tâche
  - Description détaillée de la sous-tâche 1
  - Description détaillée de la sous-tâche 2
```

## Commandes GitLab Utiles

### Commandes de Base
- `/label ~"Label"` : Ajoute un label
- `/estimate Xd` : Estime la durée (en jours)
- `/milestone %"Nom"` : Assigne à un milestone

### Labels Courants
- ~"Epic"
- ~"User Story"
- ~"Bug"
- ~"Documentation"
- ~"Feature"

### Pour lier à un Epic
- Ajouter dans la description : `Related epic: #NUMERO_EPIC`
- Ou utiliser l'interface graphique dans le panneau de droite

## Bonnes Pratiques

### Nommage
- Epics : `[EPIC] Nom de l'Epic`
- User Stories : `[US] En tant que... je veux...`
- Tasks : `[TASK] Verbe d'action + objet`

### Estimation
- User Stories : Estimation en story points ou jours
- Tasks : Estimation en heures ou jours

### Documentation
- Toujours inclure des critères d'acceptation clairs
- Détailler suffisamment les tasks pour qu'elles soient actionnables
- Utiliser des listes à puces pour la clarté

### Suivi
- Utiliser les cases à cocher pour suivre l'avancement
- Mettre à jour régulièrement le statut
- Commenter les blocages ou questions

## Exemple Complet

```markdown
Title: [US] En tant que développeur, je veux un langage ubiquitaire documenté

/label ~"User Story" ~Documentation
/estimate 2d
/milestone "Technical Design"

En tant que développeur
Je veux avoir accès à un langage ubiquitaire bien documenté
Afin de pouvoir communiquer efficacement avec l'équipe métier

Critères d'acceptation:
- Glossaire créé et validé
- Termes définis et exemplifiés
- Documentation accessible

Tasks:
- [ ] [TASK] Organiser des sessions avec experts métier
  - Planifier les workshops
  - Préparer les supports
- [ ] [TASK] Documenter les termes
  - Créer le glossaire
  - Valider les définitions
```
