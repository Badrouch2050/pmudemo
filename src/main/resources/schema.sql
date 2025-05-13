-- Suppression des tables si elles existent (dans l'ordre inverse des dépendances)
DROP TABLE IF EXISTS notification;
DROP TABLE IF EXISTS dispute;
DROP TABLE IF EXISTS support_ticket;
DROP TABLE IF EXISTS feature_flag;
DROP TABLE IF EXISTS recharge_transaction;
DROP TABLE IF EXISTS stock_carte;
DROP TABLE IF EXISTS commission_config;
DROP TABLE IF EXISTS taux_de_change;
DROP TABLE IF EXISTS operator;
DROP TABLE IF EXISTS "user";
DROP TABLE IF EXISTS currencies;
DROP TABLE IF EXISTS main_currencies;

-- Création de la table utilisateur
CREATE TABLE "user" (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    roles VARCHAR(50) NOT NULL,
    statut VARCHAR(50) NOT NULL,
    methode_authentification VARCHAR(50) NOT NULL,
    date_inscription TIMESTAMP NOT NULL
);

-- Création de la table opérateur
CREATE TABLE operator (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL UNIQUE,
    pays VARCHAR(100) NOT NULL,
    code_detection VARCHAR(50),
    statut VARCHAR(50) NOT NULL,
    logo_url VARCHAR(255),
    actif BOOLEAN DEFAULT TRUE
);

-- Création de la table taux de change
CREATE TABLE taux_de_change (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    devise_source VARCHAR(3) NOT NULL,
    devise_cible VARCHAR(3) NOT NULL,
    taux DECIMAL(10,4) NOT NULL,
    date_obtention TIMESTAMP NOT NULL
);

-- Création de la table configuration de commission
CREATE TABLE commission_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pays VARCHAR(100) NOT NULL,
    operateur_id BIGINT,
    type_commission VARCHAR(50) NOT NULL,
    valeur DECIMAL(10,2) NOT NULL,
    actif BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (operateur_id) REFERENCES operator(id)
);

-- Création de la table stock de cartes
CREATE TABLE stock_carte (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    operateur_id BIGINT NOT NULL,
    montant DECIMAL(10,2) NOT NULL,
    code VARCHAR(255) NOT NULL UNIQUE,
    statut VARCHAR(50) NOT NULL,
    date_ajout TIMESTAMP NOT NULL,
    date_utilisation TIMESTAMP,
    utilise_pour_tx_id BIGINT,
    pays VARCHAR(100) NOT NULL,
    FOREIGN KEY (operateur_id) REFERENCES operator(id)
);

-- Création de la table transaction de recharge
CREATE TABLE recharge_transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    operateur_id BIGINT NOT NULL,
    numero_cible VARCHAR(50) NOT NULL,
    montant DECIMAL(10,2) NOT NULL,
    devise_paiement VARCHAR(3) NOT NULL,
    statut VARCHAR(50) NOT NULL,
    stripe_session_id VARCHAR(255),
    date_demande TIMESTAMP NOT NULL,
    date_traitement TIMESTAMP,
    code_recharge_id BIGINT,
    agent_id BIGINT,
    montant_carte DECIMAL(10,2) NOT NULL,
    devise_carte VARCHAR(3) NOT NULL,
    taux_de_change DECIMAL(10,4) NOT NULL,
    frais_conversion DECIMAL(10,2) NOT NULL,
    pays VARCHAR(100) NOT NULL,
    commission DECIMAL(10,2) NOT NULL,
    type_commission VARCHAR(50) NOT NULL,
    commission_base DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "user"(id),
    FOREIGN KEY (operateur_id) REFERENCES operator(id),
    FOREIGN KEY (code_recharge_id) REFERENCES stock_carte(id),
    FOREIGN KEY (agent_id) REFERENCES "user"(id)
);

-- Création de la table feature flag
CREATE TABLE feature_flag (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL UNIQUE,
    actif BOOLEAN NOT NULL,
    description VARCHAR(255)
);

-- Création de la table ticket de support
CREATE TABLE support_ticket (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    sujet VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    statut VARCHAR(50) NOT NULL,
    reponse TEXT,
    date_creation TIMESTAMP NOT NULL,
    date_resolution TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES "user"(id)
);

-- Création de la table litige
CREATE TABLE dispute (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    motif VARCHAR(255) NOT NULL,
    statut VARCHAR(50) NOT NULL,
    date_creation TIMESTAMP NOT NULL,
    date_resolution TIMESTAMP,
    FOREIGN KEY (transaction_id) REFERENCES recharge_transaction(id),
    FOREIGN KEY (user_id) REFERENCES "user"(id)
);

-- Création de la table notification
CREATE TABLE notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    contenu TEXT NOT NULL,
    statut VARCHAR(50) NOT NULL,
    date_envoi TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "user"(id)
);

-- Table des devises principales
CREATE TABLE main_currencies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(3) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    symbol VARCHAR(5) NOT NULL,
    active BOOLEAN DEFAULT true
);

-- Table des devises supportées
CREATE TABLE currencies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(3) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    symbol VARCHAR(5) NOT NULL,
    is_active BOOLEAN DEFAULT true,
    region VARCHAR(50) NOT NULL,
    priority INT NOT NULL
);

-- Insertion des devises principales
INSERT INTO main_currencies (code, name, symbol, active) VALUES
('TND', 'Tunisian Dinar', 'د.ت', true);

-- Insertion des devises supportées
INSERT INTO currencies (code, name, symbol, is_active, region, priority) VALUES
('EUR', 'Euro', '€', true, 'Europe', 1),
('USD', 'US Dollar', '$', true, 'USA', 2),
('GBP', 'British Pound', '£', true, 'Europe', 3),
('AED', 'UAE Dirham', 'د.إ', true, 'Gulf', 4),
('SAR', 'Saudi Riyal', '﷼', true, 'Gulf', 5),
('QAR', 'Qatari Riyal', '﷼', true, 'Gulf', 6),
('KWD', 'Kuwaiti Dinar', 'د.ك', true, 'Gulf', 7),
('BHD', 'Bahraini Dinar', '.د.ب', true, 'Gulf', 8),
('OMR', 'Omani Rial', '﷼', true, 'Gulf', 9); 