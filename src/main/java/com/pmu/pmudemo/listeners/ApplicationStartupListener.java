package com.pmu.pmudemo.listeners;

import com.pmu.pmudemo.domains.*;
import com.pmu.pmudemo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private StockCarteRepository stockCarteRepository;

    @Autowired
    private CommissionConfigRepository commissionConfigRepository;

    @Autowired
    private TauxDeChangeRepository tauxDeChangeRepository;

    @Autowired
    private FeatureFlagRepository featureFlagRepository;

    @Autowired
    private RechargeTransactionRepository rechargeTransactionRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (userRepository.count() == 0) {
            initializeData();
        }
    }

    private void initializeData() {
        // Initialisation des utilisateurs
        User admin = new User();
        admin.setNom("Admin Chargili");
        admin.setEmail("admin@chargili.com");
        admin.setMotDePasse(passwordEncoder.encode("admin12345"));
        admin.setRole(Role.ADMIN);
        admin.setStatut("ACTIF");
        admin.setMethodeAuthentification("EMAIL");
        admin.setDateInscription(LocalDateTime.now());
        userRepository.save(admin);

        User agent = new User();
        agent.setNom("Agent Chargili");
        agent.setEmail("agent@chargili.com");
        agent.setMotDePasse("$2a$10$X7G3Y5Z9B7D1F3H5J7L9N1P3R5T7V9W1Y3A5C7E9G1I3K5M7O9Q1S3U5W7Y9");
        agent.setRole(Role.AGENT);
        agent.setStatut("ACTIF");
        agent.setMethodeAuthentification("EMAIL");
        agent.setDateInscription(LocalDateTime.now());
        userRepository.save(agent);

        // Initialisation des opérateurs
        List<Operator> operators = Arrays.asList(
            createOperator("Orange", "France", "06,07", "ACTIF", "https://example.com/orange.png", true),
            createOperator("Free", "France", "06,07", "ACTIF", "https://example.com/free.png", true),
            createOperator("SFR", "France", "06,07", "ACTIF", "https://example.com/sfr.png", true),
            createOperator("Bouygues", "France", "06,07", "ACTIF", "https://example.com/bouygues.png", true)
        );
        operatorRepository.saveAll(operators);

        // Initialisation des cartes de recharge
        List<StockCarte> cartes = Arrays.asList(
            createStockCarte(operators.get(0).getNom(), 10.00, "ORANGE-123456", "DISPONIBLE", "France"),
            createStockCarte(operators.get(1).getNom(), 20.00, "FREE-789012", "DISPONIBLE", "France"),
            createStockCarte(operators.get(2).getNom(), 15.00, "SFR-345678", "DISPONIBLE", "France"),
            createStockCarte(operators.get(3).getNom(), 25.00, "BOUYGUES-901234", "DISPONIBLE", "France")
        );
        stockCarteRepository.saveAll(cartes);

        // Initialisation des configurations de commission
        List<CommissionConfig> commissions = Arrays.asList(
            createCommissionConfig("France", operators.get(0).getNom(), "POURCENTAGE", 2.5, true),
            createCommissionConfig("France", operators.get(1).getNom(), "POURCENTAGE", 2.0, true),
            createCommissionConfig("France", operators.get(2).getNom(), "POURCENTAGE", 2.3, true),
            createCommissionConfig("France", operators.get(3).getNom(), "POURCENTAGE", 2.1, true)
        );
        commissionConfigRepository.saveAll(commissions);

        // Initialisation des taux de change
        List<TauxDeChange> taux = Arrays.asList(
            createTauxDeChange("EUR", "USD", 1.08),
            createTauxDeChange("USD", "EUR", 0.92),
            createTauxDeChange("EUR", "GBP", 0.86),
            createTauxDeChange("GBP", "EUR", 1.16)
        );
        tauxDeChangeRepository.saveAll(taux);

        // Initialisation des feature flags
        List<FeatureFlag> flags = Arrays.asList(
            createFeatureFlag("PAIEMENT_STRIPE", true, "Activer le paiement via Stripe"),
            createFeatureFlag("NOTIFICATIONS_EMAIL", true, "Activer les notifications par email"),
            createFeatureFlag("SUPPORT_CHAT", false, "Activer le chat de support"),
            createFeatureFlag("MULTI_DEVISE", true, "Activer le support multi-devises")
        );
        featureFlagRepository.saveAll(flags);

        // Initialisation des transactions de recharge
     // ... création admin et agent comme avant ...

    // Création de 10 utilisateurs USER
    List<User> users = new ArrayList();
    for (int i = 1; i <= 10; i++) {
        User user = new User();
        user.setNom("User " + i);
        user.setEmail("user" + i + "@chargili.com");
        user.setMotDePasse("$2a$10$X7G3Y5Z9B7D1F3H5J7L9N1P3R5T7V9W1Y3A5C7E9G1I3K5M7O9Q1S3U5W7Y9"); // hash de "password"
        user.setRole(Role.USER);
        user.setStatut("ACTIF");
        user.setMethodeAuthentification("EMAIL");
        user.setDateInscription(LocalDateTime.now().minusDays(i));
        users.add(user);
    }
    userRepository.saveAll(users);

    // Création de 5 transactions par utilisateur
    List<RechargeTransaction> transactions = new ArrayList();
    String[] statuts = {"COMPLETEE", "EN_ATTENTE", "ECHOUEE"};

    for (int i = 0; i < users.size(); i++) {
        User user = users.get(i);
        for (int j = 0; j < 5; j++) {
            Operator op = operators.get((i + j) % operators.size());
            String statut = statuts[(i + j) % statuts.length];
            double montant = 10.0 * (j + 1);
            transactions.add(
                createTransaction(
                    user,
                    op.getNom(),
                    "06" + String.format("%08d", i * 5 + j),
                    montant,
                    "EUR",
                    statut,
                    "sess_" + i + "_" + j,
                    LocalDateTime.now().minusDays(j),
                    montant,
                    "EUR",
                    1.0,
                    0.0,
                    op.getPays(),
                    montant * 0.025,
                    "POURCENTAGE",
                    2.5,
                    RechargeTransaction.TypeTraitement.AUTOMATIQUE,
                    null
                )
            );
        }
    }
    rechargeTransactionRepository.saveAll(transactions);
    }
    
    private Operator createOperator(String nom, String pays, String codeDetection, String statut, String logoUrl, boolean actif) {
        Operator operator = new Operator();
        operator.setNom(nom);
        operator.setPays(pays);
        operator.setCodeDetection(codeDetection);
        operator.setStatut(statut);
        operator.setLogoUrl(logoUrl);
        operator.setActif(actif);
        return operator;
    }

    private StockCarte createStockCarte(String operateur, double montant, String code, String statut, String pays) {
        StockCarte carte = new StockCarte();
        carte.setOperateur(operateur);
        carte.setMontant(montant);
        carte.setCode(code);
        carte.setStatut(statut);
        carte.setDateAjout(LocalDateTime.now());
        carte.setPays(pays);
        return carte;
    }

    private CommissionConfig createCommissionConfig(String pays, String operateur, String typeCommission, double valeur, boolean actif) {
        CommissionConfig config = new CommissionConfig();
        config.setPays(pays);
        config.setOperateur(operateur);
        config.setTypeCommission(typeCommission);
        config.setValeur(valeur);
        config.setActif(actif);
        return config;
    }

    private TauxDeChange createTauxDeChange(String deviseSource, String deviseCible, double taux) {
        TauxDeChange tauxDeChange = new TauxDeChange();
        tauxDeChange.setDeviseSource(deviseSource);
        tauxDeChange.setDeviseCible(deviseCible);
        tauxDeChange.setTaux(taux);
        tauxDeChange.setDateObtention(LocalDateTime.now());
        return tauxDeChange;
    }

    private FeatureFlag createFeatureFlag(String nom, boolean actif, String description) {
        FeatureFlag flag = new FeatureFlag();
        flag.setNom(nom);
        flag.setActif(actif);
        flag.setDescription(description);
        return flag;
    }

    private RechargeTransaction createTransaction(User user, String operateur, String numeroCible, double montant, 
            String devisePaiement, String statut, String stripeSessionId, LocalDateTime dateDemande, 
            double montantCarte, String deviseCarte, double tauxDeChange, double fraisConversion, 
            String pays, double commission, String typeCommission, double commissionBase,
            RechargeTransaction.TypeTraitement typeTraitement, User agent) {
        RechargeTransaction transaction = new RechargeTransaction();
        transaction.setUser(user);
        transaction.setOperateur(operateur);
        transaction.setNumeroCible(numeroCible);
        transaction.setMontant(montant);
        transaction.setDevisePaiement(devisePaiement);
        transaction.setStatut(statut);
        transaction.setStripeSessionId(stripeSessionId);
        transaction.setDateDemande(dateDemande);
        transaction.setMontantCarte(montantCarte);
        transaction.setDeviseCarte(deviseCarte);
        transaction.setTauxDeChange(tauxDeChange);
        transaction.setFraisConversion(fraisConversion);
        transaction.setPays(pays);
        transaction.setCommission(commission);
        transaction.setTypeCommission(typeCommission);
        transaction.setCommissionBase(commissionBase);
        transaction.setTypeTraitement(typeTraitement);
        transaction.setAgent(agent);
        if (statut.equals("COMPLETEE") || statut.equals("ECHOUEE")) {
            transaction.setDateTraitement(dateDemande.plusMinutes(30));
        }
        return transaction;
    }
} 