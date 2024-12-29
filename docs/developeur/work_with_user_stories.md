# Guide du Développeur : Travailler avec les User Stories et les Épiques

## 1. Comprendre la Hiérarchie

### Structure de Base
```
Épique
└── User Story
    └── Tâche Technique
```

### Exemple Concret
```
Épique : Gestion des Stocks
├── US : Mise à jour stocks temps réel
│   ├── Tâche : Créer API endpoints
│   └── Tâche : Développer interface temps réel
└── US : Alertes stocks bas
    ├── Tâche : Configurer système notifications
    └── Tâche : Implémenter logique d'alerte
```

## 2. Travailler avec les Épiques

### Définition
Une épique est un ensemble de user stories liées qui forment une fonctionnalité majeure.

### Bonnes Pratiques
- Identifiez clairement l'objectif métier
- Gardez une portée gérable (3-6 mois maximum)
- Utilisez des préfixes cohérents (ex: EPIC-001)
- Documentez les dépendances entre épiques

### Format Standard
```markdown
# EPIC-[Numéro] : [Titre]
## Objectif
[Description courte de l'objectif métier]
## User Stories Associées
- US-001 : [Titre]
- US-002 : [Titre]
## Critères de Succès
[Liste des critères généraux]
```

## 3. Travailler avec les User Stories

### Format Recommandé
```markdown
# US-[Numéro] : [Titre]
En tant que [persona]
Je veux [action/besoin]
Afin de [bénéfice]

## Critères d'Acceptation
- [ ] Critère 1
- [ ] Critère 2

## Notes Techniques
- Point technique 1
- Point technique 2
```

### Règles de Développement
1. **Validation des Critères**
   - Vérifier tous les critères d'acceptation
   - Obtenir validation fonctionnelle
   - Documenter les tests

2. **Gestion des Branches**
   ```bash
   # Création branche
   git checkout -b feature/US-001-nom-court
   
   # Commit
   git commit -m "US-001: Description claire"
   ```

3. **Documentation**
   - Mettre à jour la documentation technique
   - Ajouter des commentaires pertinents
   - Documenter les choix techniques

## 4. Processus de Développement

### Étapes à Suivre
1. **Analyse**
   - Lire et comprendre l'épique parent
   - Étudier les user stories associées
   - Identifier les dépendances

2. **Planification**
   - Décomposer en tâches techniques
   - Estimer la complexité
   - Identifier les risques potentiels

3. **Développement**
   - Suivre les standards de code
   - Créer des tests unitaires
   - Documenter le code

4. **Review**
   - Faire des PR claires et concises
   - Référencer les US dans les commits
   - Demander des reviews pertinentes

## 5. Tests et Validation

### Niveaux de Test
1. **Tests Unitaires**
   - Couvrir les cas critiques
   - Maintenir >80% de couverture

2. **Tests d'Intégration**
   - Vérifier les interactions
   - Tester les flux complets

3. **Tests Fonctionnels**
   - Valider les critères d'acceptation
   - Tester les cas limites

## 6. Best Practices

### Do's
- ✅ Commencer par comprendre l'épique complète
- ✅ Communiquer les blocages rapidement
- ✅ Documenter les décisions techniques
- ✅ Maintenir la traçabilité (US → code)

### Don'ts
- ❌ Ignorer les critères d'acceptation
- ❌ Développer sans tests
- ❌ Oublier de mettre à jour la documentation
- ❌ Négliger la revue de code

## 7. Outils Recommandés

### Gestion de Projet
- Jira / Trello pour le suivi
- Confluence pour la documentation
- Git pour le versioning

### Développement
- IDE avec intégration Git
- Outils de test automatisés
- Outils de qualité de code

## 8. Ressources Utiles

- [Guide Agile](lien)
- [Documentation Git](lien)
- [Standards de Code](lien)
