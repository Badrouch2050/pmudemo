package com.pmu.pmudemo.services;

import com.pmu.pmudemo.domains.RechargeTransaction;
import com.pmu.pmudemo.repositories.RechargeTransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionAdminService {
    private final RechargeTransactionRepository rechargeRepo;
    private final NotificationService notificationService;
    private final FeatureFlagService featureFlagService;
    private final StockCarteService stockCarteService;

    public TransactionAdminService(RechargeTransactionRepository rechargeRepo, NotificationService notificationService, FeatureFlagService featureFlagService, StockCarteService stockCarteService) {
        this.rechargeRepo = rechargeRepo;
        this.notificationService = notificationService;
        this.featureFlagService = featureFlagService;
        this.stockCarteService = stockCarteService;
    }
    
    public List<RechargeTransaction> getPendingTransactions() {
        return rechargeRepo.findAll().stream()
                .filter(t -> t.getStatut().equals("EN_ATTENTE"))
                .toList();
    }

    public void validateTransaction(Long id) {
        rechargeRepo.findById(id).ifPresent(t -> {
            if (featureFlagService.isAutoCodeDeliveryEnabled()) {
                // Automatisation : affecter un code dispo et notifier
                stockCarteService.getAvailableCard(t.getOperateur(), t.getMontant()).ifPresentOrElse(carte -> {
                    stockCarteService.assignCardToTransaction(carte.getId(), t.getId());
                    notificationService.sendNotification(t.getUser().getId(), "IN_APP", "Votre recharge a été validée automatiquement.");
                    String emailContent = notificationService.buildRechargeSuccessEmail(
                            t.getUser().getNom(), t.getMontant(), t.getOperateur(), t.getNumeroCible(), carte.getCode());
                    notificationService.sendEmailSendinblue(t.getUser().getEmail(), "Recharge validée", emailContent);
                    String smsContent = notificationService.buildRechargeSuccessSms(
                            String.valueOf(t.getMontant()), t.getOperateur(), carte.getCode());
                    notificationService.sendSmsTwilio(t.getNumeroCible(), smsContent);
                }, () -> {
                    // Aucun code dispo, repasser en manuel
                    t.setStatut("EN_ATTENTE_AGENT");
                    rechargeRepo.save(t);
                    notificationService.sendNotification(t.getUser().getId(), "IN_APP", "Votre recharge sera traitée manuellement.");
                });
            } else {
                // Flux manuel classique
                t.setStatut("EN_ATTENTE_AGENT");
                rechargeRepo.save(t);
            }
        });
    }

     public Page<RechargeTransaction> getPendingTransactions(Pageable pageable) {
        return rechargeRepo.findByStatut("EN_ATTENTE", pageable);
    }

    public void notifyFailure(RechargeTransaction t) {
        if (t.getUser() != null) {
            String emailContent = notificationService.buildRechargeFailureEmail(
                    t.getUser().getNom(), t.getMontant(), t.getOperateur(), t.getNumeroCible());
            notificationService.sendEmailSendinblue(t.getUser().getEmail(), "Echec de la recharge", emailContent);
            String smsContent = notificationService.buildRechargeFailureSms(
                    String.valueOf(t.getMontant()), t.getOperateur());
            notificationService.sendSmsTwilio(t.getNumeroCible(), smsContent);
        }
    }
} 