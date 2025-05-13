# Documentation - Gestion des Opérateurs (Backoffice)

## Vue d'ensemble
Le module de gestion des opérateurs permet aux administrateurs de gérer les opérateurs téléphoniques par pays. Ce module est accessible uniquement aux utilisateurs ayant le rôle ADMIN.

## Endpoints API

### 1. Liste des Opérateurs
```http
GET /api/backoffice/operators
```
Retourne la liste des opérateurs actifs, avec possibilité de filtrer par pays.

**Paramètres de requête :**
- `pays` (optionnel) : Filtre les opérateurs par pays

**Réponse :**
```json
[
  {
    "id": 1,
    "nom": "Orange",
    "pays": "France",
    "codeDetection": "06,07",
    "statut": "ACTIF",
    "logoUrl": "https://example.com/orange.png",
    "actif": true
  }
]
```

### 2. Liste de Tous les Opérateurs
```http
GET /api/backoffice/operators/all
```
Retourne la liste complète des opérateurs, y compris les inactifs.

**Réponse :**
```json
[
  {
    "id": 1,
    "nom": "Orange",
    "pays": "France",
    "codeDetection": "06,07",
    "statut": "ACTIF",
    "logoUrl": "https://example.com/orange.png",
    "actif": true
  }
]
```

### 3. Détails d'un Opérateur
```http
GET /api/backoffice/operators/{id}
```
Retourne les détails d'un opérateur spécifique.

**Paramètres :**
- `id` : ID de l'opérateur

**Réponse :**
```json
{
  "id": 1,
  "nom": "Orange",
  "pays": "France",
  "codeDetection": "06,07",
  "statut": "ACTIF",
  "logoUrl": "https://example.com/orange.png",
  "actif": true
}
```

### 4. Création d'un Opérateur
```http
POST /api/backoffice/operators
```
Crée un nouvel opérateur.

**Corps de la requête :**
```json
{
  "nom": "Orange",
  "pays": "France",
  "codeDetection": "06,07",
  "statut": "ACTIF",
  "logoUrl": "https://example.com/orange.png",
  "actif": true
}
```

**Validation :**
- Le nom doit être unique
- Le pays est obligatoire
- Le statut doit être valide (ACTIF, INACTIF)
- L'URL du logo doit être valide

### 5. Mise à Jour d'un Opérateur
```http
PUT /api/backoffice/operators/{id}
```
Met à jour un opérateur existant.

**Paramètres :**
- `id` : ID de l'opérateur

**Corps de la requête :**
```json
{
  "nom": "Orange",
  "pays": "France",
  "codeDetection": "06,07",
  "statut": "ACTIF",
  "logoUrl": "https://example.com/orange.png"
}
```

### 6. Activation/Désactivation d'un Opérateur
```http
PUT /api/backoffice/operators/{id}/activation
```
Active ou désactive un opérateur.

**Paramètres :**
- `id` : ID de l'opérateur
- `actif` : true pour activer, false pour désactiver

**Réponse :**
```json
{
  "id": 1,
  "nom": "Orange",
  "pays": "France",
  "codeDetection": "06,07",
  "statut": "ACTIF",
  "logoUrl": "https://example.com/orange.png",
  "actif": false
}
```

### 7. Suppression d'un Opérateur
```http
DELETE /api/backoffice/operators/{id}
```
Désactive un opérateur (soft delete).

**Paramètres :**
- `id` : ID de l'opérateur

## Codes d'Erreur
- `400 Bad Request` : Données invalides
- `401 Unauthorized` : Non authentifié
- `403 Forbidden` : Non autorisé (pas le rôle ADMIN)
- `404 Not Found` : Opérateur non trouvé
- `409 Conflict` : Nom d'opérateur déjà existant

## Statuts des Opérateurs
- `ACTIF` : Opérateur en service
- `INACTIF` : Opérateur temporairement indisponible

## Bonnes Pratiques
1. Toujours vérifier l'unicité du nom d'opérateur par pays
2. Maintenir les codes de détection à jour
3. Utiliser des URLs HTTPS pour les logos
4. Documenter les changements de statut
5. Vérifier l'impact sur les transactions existantes avant la désactivation

## Notes Importantes
1. La désactivation d'un opérateur n'affecte pas les transactions en cours
2. Les opérateurs inactifs ne sont pas visibles dans l'interface utilisateur
3. Les modifications sont journalisées pour audit
4. Les codes de détection sont utilisés pour identifier automatiquement l'opérateur
5. Les logos doivent être optimisés pour le web (format recommandé : PNG, max 200x200px) 