package com.pmu.pmudemo.services;

import com.pmu.pmudemo.domains.RechargeTransaction;
import com.pmu.pmudemo.domains.User;
import com.pmu.pmudemo.repositories.RechargeTransactionRepository;
import com.pmu.pmudemo.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.pmu.pmudemo.services.CommissionConfigService;
import com.pmu.pmudemo.domains.CommissionConfig;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RechargeService {
    private final RechargeTransactionRepository rechargeRepo;
    private final UserRepository userRepo;
    private final NotificationService notificationService;
    private final TransactionAdminService transactionAdminService;
    private final TauxDeChangeService tauxDeChangeService;
    private final CommissionConfigService commissionConfigService;
    @Value("${conversion.margin.percent:0}")
    private double marginPercent;
    @Value("${commission.type:POURCENTAGE}")
    private String commissionType;
    @Value("${commission.value:0}")
    private static final Logger logger = LoggerFactory.getLogger(RechargeService.class);

    public RechargeService(RechargeTransactionRepository rechargeRepo, UserRepository userRepo, NotificationService notificationService, TransactionAdminService transactionAdminService, TauxDeChangeService tauxDeChangeService, CommissionConfigService commissionConfigService) {
        this.rechargeRepo = rechargeRepo;
        this.userRepo = userRepo;
        this.notificationService = notificationService;
        this.transactionAdminService = transactionAdminService;
        this.tauxDeChangeService = tauxDeChangeService;
        this.commissionConfigService = commissionConfigService;
    }

    public RechargeTransaction createRecharge(Long userId, String operateur, String numeroCible, Double montantCarte, String deviseCarte, String devisePaiement, String pays) {
        User user = userRepo.findById(userId).orElseThrow();
        // 1. Récupérer le taux de change
        TauxDeChange taux = tauxDeChangeService.getOrFetchRate(devisePaiement, deviseCarte);
        double tauxUtilise = taux.getTaux();
        // 2. Appliquer la marge
        double tauxAvecMarge = tauxUtilise * (1 + marginPercent / 100.0);
        // 3. Calculer le montant à payer
        double montantAPayer = montantCarte / tauxAvecMarge;
        double fraisConversion = montantAPayer * (marginPercent / 100.0);
        // 4. Déterminer la commission dynamique
        String typeCommission = commissionType;
        double commissionValue = 0.0;
        double commission = 0.0;
        CommissionConfig config = commissionConfigService.getCommission(pays, operateur).orElse(null);
        if (config != null) {
            typeCommission = config.getTypeCommission();
            commissionValue = config.getValeur();
        } else {
            commissionValue = this.commissionValue;
        }
        if ("POURCENTAGE".equalsIgnoreCase(typeCommission)) {
            commission = montantAPayer * (commissionValue / 100.0);
        } else if ("FIXE".equalsIgnoreCase(typeCommission)) {
            commission = commissionValue;
        }
        // 5. Créer la transaction
        RechargeTransaction recharge = new RechargeTransaction();
        recharge.setUser(user);
        recharge.setOperateur(operateur);
        recharge.setNumeroCible(numeroCible);
        recharge.setMontant(montantAPayer);
        recharge.setDevisePaiement(devisePaiement);
        recharge.setStatut("EN_ATTENTE");
        recharge.setDateDemande(LocalDateTime.now());
        recharge.setMontantCarte(montantCarte);
        recharge.setDeviseCarte(deviseCarte);
        recharge.setTauxDeChange(tauxAvecMarge);
        recharge.setFraisConversion(fraisConversion);
        recharge.setPays(pays);
        recharge.setCommission(commission);
        recharge.setTypeCommission(typeCommission);
        recharge.setCommissionBase(commissionValue);
        RechargeTransaction saved = rechargeRepo.save(recharge);
        logger.info("Transaction créée : {} {} (carte {} {}), taux {}, marge {}%, pays {}, commission {} {} ({}), frais {}", montantAPayer, devisePaiement, montantCarte, deviseCarte, tauxAvecMarge, marginPercent, pays, commission, typeCommission, commissionValue, fraisConversion);
        notificationService.sendNotification(userId, "IN_APP", "Votre demande de recharge a été prise en compte.");
        String emailContent = notificationService.buildRechargeEmail(user.getNom(), montantAPayer, operateur, numeroCible);
        notificationService.sendEmailSendinblue(user.getEmail(), "Confirmation de recharge", emailContent);
        String smsContent = notificationService.buildRechargeSms(String.valueOf(montantAPayer), operateur);
        notificationService.sendSmsTwilio(recharge.getNumeroCible(), smsContent);
        // Exemple d'échec métier : pas de carte disponible
        boolean carteDisponible = true; // Remplacer par la vraie vérification
        if (!carteDisponible) {
            transactionAdminService.notifyFailure(recharge);
            throw new RuntimeException("Aucune carte de recharge disponible pour ce montant/opérateur.");
        }
        return saved;
    }

    public List<RechargeTransaction> getRechargesByUser(Long userId) {
        return rechargeRepo.findAll().stream().filter(r -> r.getUser().getId().equals(userId)).toList();
    }
} 