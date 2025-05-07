-- Initialisation des utilisateurs
INSERT INTO "user" (id, nom, email, mot_de_passe, roles, statut, methode_authentification, date_inscription)
VALUES (1, 'Admin Chargili', 'admin@chargili.com', '$2a$10$X7G3Y5Z9B7D1F3H5J7L9N1P3R5T7V9W1Y3A5C7E9G1I3K5M7O9Q1S3U5W7Y9', 'ADMIN', 'ACTIF', 'EMAIL', CURRENT_TIMESTAMP);

INSERT INTO "user" (id, nom, email, mot_de_passe, roles, statut, methode_authentification, date_inscription)
VALUES (2, 'User Chargili', 'user@chargili.com', '$2a$10$X7G3Y5Z9B7D1F3H5J7L9N1P3R5T7V9W1Y3A5C7E9G1I3K5M7O9Q1S3U5W7Y9', 'USER', 'ACTIF', 'EMAIL', CURRENT_TIMESTAMP);

-- Initialisation des opérateurs
INSERT INTO operator (id, nom, pays, code_detection, statut, logo_url, actif)
VALUES 
(1, 'Orange', 'France', '06,07', 'ACTIF', 'https://example.com/orange.png', true),
(2, 'Free', 'France', '06,07', 'ACTIF', 'https://example.com/free.png', true),
(3, 'SFR', 'France', '06,07', 'ACTIF', 'https://example.com/sfr.png', true),
(4, 'Bouygues', 'France', '06,07', 'ACTIF', 'https://example.com/bouygues.png', true);

-- Initialisation des cartes de recharge
INSERT INTO stock_carte (id, operateur_id, montant, code, statut, date_ajout, pays)
VALUES 
(1, 1, 10.00, 'ORANGE-123456', 'DISPONIBLE', CURRENT_TIMESTAMP, 'France'),
(2, 2, 20.00, 'FREE-789012', 'DISPONIBLE', CURRENT_TIMESTAMP, 'France'),
(3, 3, 15.00, 'SFR-345678', 'DISPONIBLE', CURRENT_TIMESTAMP, 'France'),
(4, 4, 25.00, 'BOUYGUES-901234', 'DISPONIBLE', CURRENT_TIMESTAMP, 'France');

-- Initialisation des configurations de commission
INSERT INTO commission_config (id, pays, operateur_id, type_commission, valeur, actif)
VALUES 
(1, 'France', 1, 'POURCENTAGE', 2.5, true),
(2, 'France', 2, 'POURCENTAGE', 2.0, true),
(3, 'France', 3, 'POURCENTAGE', 2.3, true),
(4, 'France', 4, 'POURCENTAGE', 2.1, true);

-- Initialisation des taux de change
INSERT INTO taux_de_change (id, devise_source, devise_cible, taux, date_obtention)
VALUES 
(1, 'EUR', 'USD', 1.08, CURRENT_TIMESTAMP),
(2, 'USD', 'EUR', 0.92, CURRENT_TIMESTAMP),
(3, 'EUR', 'GBP', 0.86, CURRENT_TIMESTAMP),
(4, 'GBP', 'EUR', 1.16, CURRENT_TIMESTAMP);

-- Initialisation des feature flags
INSERT INTO feature_flag (id, nom, actif, description)
VALUES 
(1, 'PAIEMENT_STRIPE', true, 'Activer le paiement via Stripe'),
(2, 'NOTIFICATIONS_EMAIL', true, 'Activer les notifications par email'),
(3, 'SUPPORT_CHAT', false, 'Activer le chat de support'),
(4, 'MULTI_DEVISE', true, 'Activer le support multi-devises');

-- Initialisation des transactions de recharge
INSERT INTO recharge_transaction (id, user_id, operateur_id, numero_cible, montant, devise_paiement, statut, stripe_session_id, date_demande, montant_carte, devise_carte, taux_de_change, frais_conversion, pays, commission, type_commission, commission_base)
VALUES 
(1, 1, 1, '0612345678', 10.00, 'EUR', 'COMPLETEE', 'sess_123', CURRENT_TIMESTAMP, 10.00, 'EUR', 1.0, 0.0, 'France', 0.25, 'POURCENTAGE', 2.5),
(2, 1, 2, '0623456789', 20.00, 'EUR', 'EN_COURS', 'sess_456', CURRENT_TIMESTAMP, 20.00, 'EUR', 1.0, 0.0, 'France', 0.40, 'POURCENTAGE', 2.0),
(3, 1, 3, '0634567890', 15.00, 'EUR', 'EN_ATTENTE', 'sess_789', CURRENT_TIMESTAMP, 15.00, 'EUR', 1.0, 0.0, 'France', 0.35, 'POURCENTAGE', 2.3),
(4, 1, 4, '0645678901', 25.00, 'EUR', 'ECHOUEE', 'sess_012', CURRENT_TIMESTAMP, 25.00, 'EUR', 1.0, 0.0, 'France', 0.53, 'POURCENTAGE', 2.1);

-- Mise à jour des cartes utilisées
UPDATE stock_carte SET utilise_pour_tx_id = 1, statut = 'UTILISE', date_utilisation = CURRENT_TIMESTAMP WHERE id = 1; 