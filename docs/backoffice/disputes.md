# Documentation - Gestion des Litiges (Backoffice)

## Vue d'ensemble
Le module de gestion des litiges permet aux administrateurs de gérer les réclamations des utilisateurs concernant leurs transactions. Ce module est accessible uniquement aux utilisateurs ayant le rôle ADMIN ou AGENT.

## Endpoints API

### 1. Liste des Litiges
```http
GET /api/backoffice/disputes
```
Retourne la liste de tous les litiges.

**Réponse :**
```json
[
  {
    "id": 1,
    "transaction": {
      "id": 123,
      "montant": 50.00,
      "devisePaiement": "EUR",
      "statut": "TERMINE"
    },
    "user": {
      "id": 456,
      "nom": "John Doe",
      "email": "john@example.com"
    },
    "motif": "Recharge non reçue",
    "statut": "OUVERT",
    "commentaire": "Le client n'a pas reçu sa recharge",
    "dateCreation": "2024-03-20T10:30:00",
    "dateResolution": null
  }
]
```

### 2. Liste des Litiges par Transaction
```http
GET /api/backoffice/disputes/transaction/{transactionId}
```
Retourne la liste des litiges liés à une transaction spécifique.

**Paramètres :**
- `transactionId` : ID de la transaction

**Réponse :**
```json
[
  {
    "id": 1,
    "motif": "Recharge non reçue",
    "statut": "OUVERT",
    "commentaire": "Le client n'a pas reçu sa recharge",
    "dateCreation": "2024-03-20T10:30:00",
    "dateResolution": null
  }
]
```

### 3. Création d'un Litige
```http
POST /api/backoffice/disputes
```
Crée un nouveau litige.

**Paramètres :**
- `transactionId` : ID de la transaction concernée
- `motif` : Motif du litige
- `commentaire` : Commentaire détaillé (optionnel)

**Réponse :**
```json
{
  "id": 1,
  "transaction": {
    "id": 123,
    "montant": 50.00,
    "devisePaiement": "EUR",
    "statut": "TERMINE"
  },
  "user": {
    "id": 456,
    "nom": "John Doe",
    "email": "john@example.com"
  },
  "motif": "Recharge non reçue",
  "statut": "OUVERT",
  "commentaire": "Le client n'a pas reçu sa recharge",
  "dateCreation": "2024-03-20T10:30:00",
  "dateResolution": null
}
```

### 4. Mise à Jour du Statut d'un Litige
```http
PATCH /api/backoffice/disputes/{id}/status
```
Met à jour le statut d'un litige et ajoute un commentaire.

**Paramètres :**
- `id` : ID du litige
- `statut` : Nouveau statut (EN_COURS, RESOLU, REMBOURSE, REJETE)
- `commentaire` : Commentaire sur la mise à jour (optionnel)

**Réponse :**
```json
{
  "id": 1,
  "transaction": {
    "id": 123,
    "montant": 50.00,
    "devisePaiement": "EUR",
    "statut": "TERMINE"
  },
  "user": {
    "id": 456,
    "nom": "John Doe",
    "email": "john@example.com"
  },
  "motif": "Recharge non reçue",
  "statut": "RESOLU",
  "commentaire": "La recharge a été effectuée avec succès",
  "dateCreation": "2024-03-20T10:30:00",
  "dateResolution": "2024-03-20T11:00:00"
}
```

## Statuts des Litiges
- `OUVERT` : Litige nouvellement créé
- `EN_COURS` : Litige en cours de traitement
- `RESOLU` : Litige résolu favorablement
- `REMBOURSE` : Litige résolu avec remboursement
- `REJETE` : Litige rejeté

## Codes d'Erreur
- `400 Bad Request` : Données invalides
- `401 Unauthorized` : Non authentifié
- `403 Forbidden` : Non autorisé (pas le rôle ADMIN ou AGENT)
- `404 Not Found` : Litige non trouvé

## Bonnes Pratiques
1. Traiter les litiges dans un délai raisonnable
2. Documenter toutes les actions entreprises
3. Maintenir une communication claire avec l'utilisateur
4. Vérifier l'historique des transactions avant de prendre une décision
5. Conserver toutes les preuves et justificatifs

## Notes Importantes
1. Les litiges sont liés à une transaction spécifique
2. La date de résolution est automatiquement mise à jour lors de la résolution
3. Les litiges peuvent être filtrés par statut
4. L'historique complet des litiges est conservé
5. Les notifications sont envoyées aux utilisateurs lors des mises à jour

## Workflow Recommandé
1. Nouveau litige reçu → Statut : OUVERT
2. Agent assigné → Statut : EN_COURS
3. Résolution trouvée → Statut : RESOLU ou REMBOURSE
4. Litige rejeté → Statut : REJETE

## Notifications
- L'utilisateur est notifié par email lors de :
  - Création du litige
  - Changement de statut
  - Ajout d'un commentaire
  - Résolution du litige

## Filtres Disponibles
- Par statut
- Par transaction
- Par utilisateur
- Par date de création
- Par date de résolution
- Par motif (recherche textuelle)

## Processus de Remboursement
1. Vérifier la validité de la réclamation
2. Confirmer le statut de la transaction
3. Vérifier les politiques de remboursement
4. Effectuer le remboursement si nécessaire
5. Mettre à jour le statut du litige
6. Notifier l'utilisateur 