# Documentation - Gestion des Commissions (Backoffice)

## Vue d'ensemble
Le module de gestion des commissions permet aux administrateurs de configurer les commissions par pays et par opérateur. Les commissions peuvent être de type pourcentage ou fixe, et peuvent être définies globalement pour un pays ou spécifiquement pour un opérateur.

## Endpoints API

### 1. Liste des Commissions
```http
GET /api/backoffice/commissions
```
Retourne la liste de toutes les configurations de commission.

**Réponse :**
```json
[
  {
    "id": 1,
    "pays": "France",
    "operateur": "Orange",
    "typeCommission": "POURCENTAGE",
    "valeur": 2.5,
    "actif": true
  }
]
```

### 2. Détails d'une Commission
```http
GET /api/backoffice/commissions/{id}
```
Retourne les détails d'une configuration de commission spécifique.

**Paramètres :**
- `id` : ID de la configuration

**Réponse :**
```json
{
  "id": 1,
  "pays": "France",
  "operateur": "Orange",
  "typeCommission": "POURCENTAGE",
  "valeur": 2.5,
  "actif": true
}
```

### 3. Création d'une Commission
```http
POST /api/backoffice/commissions
```
Crée une nouvelle configuration de commission.

**Corps de la requête :**
```json
{
  "pays": "France",
  "operateur": "Orange",
  "typeCommission": "POURCENTAGE",
  "valeur": 2.5,
  "actif": true
}
```

**Validation :**
- Le pays est obligatoire
- L'opérateur est optionnel (null = commission globale pour le pays)
- Le type de commission doit être "POURCENTAGE" ou "FIXE"
- La valeur doit être positive
- Pour un type "POURCENTAGE", la valeur doit être ≤ 100

### 4. Mise à Jour d'une Commission
```http
PUT /api/backoffice/commissions/{id}
```
Met à jour une configuration de commission existante.

**Paramètres :**
- `id` : ID de la configuration

**Corps de la requête :**
```json
{
  "pays": "France",
  "operateur": "Orange",
  "typeCommission": "POURCENTAGE",
  "valeur": 3.0,
  "actif": true
}
```

### 5. Suppression d'une Commission
```http
DELETE /api/backoffice/commissions/{id}
```
Supprime une configuration de commission.

**Paramètres :**
- `id` : ID de la configuration

## Types de Commission
- `POURCENTAGE` : La commission est calculée comme un pourcentage du montant de la transaction
- `FIXE` : La commission est un montant fixe

## Hiérarchie des Commissions
1. Commission spécifique opérateur (si existe)
2. Commission globale pays (si pas de commission spécifique)
3. Commission par défaut (3%) si aucune configuration trouvée

## Codes d'Erreur
- `400 Bad Request` : Données invalides
- `401 Unauthorized` : Non authentifié
- `403 Forbidden` : Non autorisé (pas le rôle ADMIN)
- `404 Not Found` : Configuration non trouvée

## Bonnes Pratiques
1. Toujours définir une commission globale par pays
2. Utiliser des commissions spécifiques opérateur uniquement si nécessaire
3. Documenter les changements de commission
4. Vérifier l'impact sur les transactions en cours avant modification
5. Maintenir un historique des modifications

## Notes Importantes
1. Les modifications de commission n'affectent pas les transactions en cours
2. Les commissions sont appliquées lors de la création de la transaction
3. Les informations de commission sont stockées dans chaque transaction
4. Les commissions peuvent être consultées dans les détails des transactions
5. Les commissions sont prises en compte dans le calcul des revenus

## Exemple de Calcul
Pour une transaction de 100€ avec une commission de 2.5% :
- Montant transaction : 100€
- Commission : 2.5€ (2.5% de 100€)
- Montant net : 97.5€

Pour une commission fixe de 5€ :
- Montant transaction : 100€
- Commission : 5€ (fixe)
- Montant net : 95€ 