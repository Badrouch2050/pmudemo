# Documentation - Gestion des Tickets de Support (Backoffice)

## Vue d'ensemble
Le module de gestion des tickets de support permet aux administrateurs de gérer les demandes d'assistance des utilisateurs. Ce module est accessible uniquement aux utilisateurs ayant le rôle ADMIN ou AGENT.

## Endpoints API

### 1. Liste des Tickets
```http
GET /api/backoffice/support-tickets
```
Retourne la liste de tous les tickets de support.

**Réponse :**
```json
[
  {
    "id": 1,
    "user": {
      "id": 123,
      "nom": "John Doe",
      "email": "john@example.com"
    },
    "sujet": "Problème de recharge",
    "message": "Ma recharge n'a pas été effectuée",
    "statut": "OUVERT",
    "reponse": null,
    "dateCreation": "2024-03-20T10:30:00",
    "dateResolution": null
  }
]
```

### 2. Liste des Tickets par Utilisateur
```http
GET /api/backoffice/support-tickets/user/{userId}
```
Retourne la liste des tickets d'un utilisateur spécifique.

**Paramètres :**
- `userId` : ID de l'utilisateur

**Réponse :**
```json
[
  {
    "id": 1,
    "sujet": "Problème de recharge",
    "message": "Ma recharge n'a pas été effectuée",
    "statut": "OUVERT",
    "reponse": null,
    "dateCreation": "2024-03-20T10:30:00",
    "dateResolution": null
  }
]
```

### 3. Création d'un Ticket
```http
POST /api/backoffice/support-tickets
```
Crée un nouveau ticket de support.

**Paramètres :**
- `userId` : ID de l'utilisateur
- `sujet` : Sujet du ticket
- `message` : Message détaillé

**Réponse :**
```json
{
  "id": 1,
  "user": {
    "id": 123,
    "nom": "John Doe",
    "email": "john@example.com"
  },
  "sujet": "Problème de recharge",
  "message": "Ma recharge n'a pas été effectuée",
  "statut": "OUVERT",
  "reponse": null,
  "dateCreation": "2024-03-20T10:30:00",
  "dateResolution": null
}
```

### 4. Répondre à un Ticket
```http
PATCH /api/backoffice/support-tickets/{id}/respond
```
Permet de répondre à un ticket et de mettre à jour son statut.

**Paramètres :**
- `id` : ID du ticket
- `reponse` : Réponse au ticket
- `statut` : Nouveau statut (EN_COURS, RESOLU, FERME)

**Réponse :**
```json
{
  "id": 1,
  "user": {
    "id": 123,
    "nom": "John Doe",
    "email": "john@example.com"
  },
  "sujet": "Problème de recharge",
  "message": "Ma recharge n'a pas été effectuée",
  "statut": "RESOLU",
  "reponse": "Votre recharge a été effectuée avec succès",
  "dateCreation": "2024-03-20T10:30:00",
  "dateResolution": "2024-03-20T11:00:00"
}
```

## Statuts des Tickets
- `OUVERT` : Ticket nouvellement créé
- `EN_COURS` : Ticket en cours de traitement
- `RESOLU` : Ticket résolu
- `FERME` : Ticket fermé

## Codes d'Erreur
- `400 Bad Request` : Données invalides
- `401 Unauthorized` : Non authentifié
- `403 Forbidden` : Non autorisé (pas le rôle ADMIN ou AGENT)
- `404 Not Found` : Ticket non trouvé

## Bonnes Pratiques
1. Répondre aux tickets dans un délai raisonnable
2. Mettre à jour le statut du ticket régulièrement
3. Fournir des réponses claires et précises
4. Documenter les actions entreprises
5. Notifier l'utilisateur des mises à jour importantes

## Notes Importantes
1. Les tickets sont liés à un utilisateur spécifique
2. Les réponses sont stockées dans le ticket
3. La date de résolution est automatiquement mise à jour lors de la résolution
4. Les tickets peuvent être filtrés par statut
5. L'historique complet des tickets est conservé

## Workflow Recommandé
1. Nouveau ticket reçu → Statut : OUVERT
2. Agent assigné → Statut : EN_COURS
3. Réponse fournie → Statut : RESOLU
4. Ticket archivé → Statut : FERME

## Notifications
- L'utilisateur est notifié par email lors de :
  - Création du ticket
  - Réponse au ticket
  - Changement de statut
  - Résolution du ticket

## Filtres Disponibles
- Par statut
- Par utilisateur
- Par date de création
- Par date de résolution
- Par sujet (recherche textuelle) 