# Documentation - Gestion des Taux de Change

## Vue d'ensemble
Le module de gestion des taux de change permet aux administrateurs de gérer les taux de conversion entre la devise principale (TND) et les devises supportées. Ce module est accessible uniquement aux utilisateurs ayant le rôle ADMIN.

## Gestion des Devises Supportées

### Endpoints de Gestion des Devises
```http
GET /api/backoffice/currencies
```
Retourne la liste de toutes les devises supportées actives, triées par priorité.

**Réponse :**
```json
[
  {
    "id": 1,
    "code": "EUR",
    "name": "Euro",
    "symbol": "€",
    "active": true,
    "region": "Europe",
    "priority": 1
  }
]
```

```http
GET /api/backoffice/currencies/{id}
```
Récupère une devise spécifique par son ID.

**Paramètres :**
- `id` : ID de la devise

**Réponse :**
```json
{
  "id": 1,
  "code": "EUR",
  "name": "Euro",
  "symbol": "€",
  "active": true,
  "region": "Europe",
  "priority": 1
}
```

```http
POST /api/backoffice/currencies
```
Crée une nouvelle devise.

**Corps de la requête :**
```json
{
  "code": "EUR",
  "name": "Euro",
  "symbol": "€",
  "active": true,
  "region": "Europe",
  "priority": 1
}
```

**Validation :**
- Le code de la devise doit être unique
- Le code doit être composé de 3 lettres majuscules
- Le symbole ne doit pas être vide
- La priorité doit être un nombre positif

```http
PUT /api/backoffice/currencies/{id}
```
Met à jour une devise existante.

**Paramètres :**
- `id` : ID de la devise

**Corps de la requête :**
```json
{
  "code": "EUR",
  "name": "Euro",
  "symbol": "€",
  "active": true,
  "region": "Europe",
  "priority": 1
}
```

```http
PATCH /api/backoffice/currencies/{id}/toggle
```
Active ou désactive une devise.

**Paramètres :**
- `id` : ID de la devise

**Réponse :**
```json
{
  "id": 1,
  "code": "EUR",
  "name": "Euro",
  "symbol": "€",
  "active": false,
  "region": "Europe",
  "priority": 1
}
```

### Codes d'Erreur
- `404 Not Found` : Devise non trouvée
- `400 Bad Request` : Données invalides
- `409 Conflict` : Code de devise déjà existant
- `403 Forbidden` : Accès non autorisé

### Notes Importantes
1. La désactivation d'une devise affecte tous les taux de change associés
2. La priorité détermine l'ordre d'affichage dans l'interface utilisateur
3. Les devises désactivées ne peuvent pas être utilisées pour les conversions
4. Les modifications sont journalisées pour audit

## Gestion des Devises Principales

### Endpoints de Gestion des Devises Principales
```http
GET /api/backoffice/main-currencies
```
Retourne la liste des devises principales actives.

**Réponse :**
```json
[
  {
    "id": 1,
    "code": "TND",
    "name": "Dinar Tunisien",
    "symbol": "د.ت",
    "active": true
  }
]
```

```http
GET /api/backoffice/main-currencies/{id}
```
Récupère une devise principale spécifique.

```http
POST /api/backoffice/main-currencies
```
Crée une nouvelle devise principale.

```http
PUT /api/backoffice/main-currencies/{id}
```
Met à jour une devise principale existante.

```http
PATCH /api/backoffice/main-currencies/{id}/toggle
```
Active ou désactive une devise principale.

## Endpoints de Taux de Change

### 1. Liste des Taux de Change
```http
GET /api/backoffice/taux-de-change
```
Retourne la liste de tous les taux de change enregistrés.

**Réponse :**
```json
[
  {
    "id": 1,
    "deviseSource": "TND",
    "deviseCible": "EUR",
    "taux": 0.30,
    "dateObtention": "2024-03-20T10:00:00",
    "actif": true
  }
]
```

### 2. Obtenir un Taux de Change Spécifique
```http
GET /api/backoffice/taux-de-change/{id}
```
Retourne les détails d'un taux de change spécifique.

**Paramètres :**
- `id` : ID du taux de change

**Réponse :**
```json
{
  "id": 1,
  "deviseSource": "TND",
  "deviseCible": "EUR",
  "taux": 0.30,
  "dateObtention": "2024-03-20T10:00:00",
  "actif": true
}
```

### 3. Créer un Nouveau Taux de Change
```http
POST /api/backoffice/taux-de-change
```
Crée un nouveau taux de change.

**Corps de la requête :**
```json
{
  "deviseSource": "TND",
  "deviseCible": "EUR",
  "taux": 0.30
}
```

**Validation :**
- La devise source doit être une devise principale (TND)
- La devise cible doit être une devise supportée valide
- La devise source ne peut pas être la même que la devise cible

### 4. Mettre à Jour un Taux de Change
```http
PUT /api/backoffice/taux-de-change/{id}
```
Met à jour un taux de change existant.

**Paramètres :**
- `id` : ID du taux de change

**Corps de la requête :**
```json
{
  "deviseSource": "TND",
  "deviseCible": "EUR",
  "taux": 0.31
}
```

### 5. Activer/Désactiver un Taux de Change
```http
PATCH /api/backoffice/taux-de-change/{id}/toggle
```
Active ou désactive un taux de change.

**Paramètres :**
- `id` : ID du taux de change

### 6. Historique des Modifications
```http
GET /api/backoffice/taux-de-change/{id}/historique
```
Retourne l'historique des modifications d'un taux de change.

**Paramètres :**
- `id` : ID du taux de change

**Réponse :**
```json
[
  {
    "id": 1,
    "taux": 0.30,
    "dateModification": "2024-03-20T10:00:00"
  }
]
```

### 7. Calcul de Conversion
```http
GET /api/backoffice/taux-de-change/calcul
```
Calcule le montant converti entre deux devises.

**Paramètres de requête :**
- `montant` : Montant à convertir
- `deviseSource` : Code de la devise source
- `deviseCible` : Code de la devise cible

**Réponse :**
```json
{
  "montantSource": 100.00,
  "deviseSource": "TND",
  "montantCible": 30.00,
  "deviseCible": "EUR",
  "taux": 0.30,
  "dateCalcul": "2024-03-20T10:00:00"
}
```

## Bonnes Pratiques
1. Toujours vérifier que la devise source est TND
2. S'assurer que la devise cible est une devise supportée active
3. Maintenir un historique des modifications pour audit
4. Utiliser des taux de change précis (4 décimales)
5. Mettre à jour régulièrement les taux pour refléter les conditions du marché

## Gestion des Erreurs
Les erreurs courantes incluent :
- Devise source invalide
- Devise cible invalide
- Taux de change non trouvé
- Tentative de conversion avec une devise désactivée

Toutes les erreurs retournent un message explicatif et un code HTTP approprié. 