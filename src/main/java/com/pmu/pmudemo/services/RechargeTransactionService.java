package com.pmu.pmudemo.services;

import com.pmu.pmudemo.domains.RechargeTransaction;
import com.pmu.pmudemo.repositories.RechargeTransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.pmu.pmudemo.domains.User;

@Service
public class RechargeTransactionService {

    @Autowired
    private RechargeTransactionRepository rechargeTransactionRepository;

    @Autowired
    private RechargeConfigurationService configurationService;

    @Autowired
    private RechargeStockService stockService;

    @Transactional
    public RechargeTransaction createDirectRecharge(User user, String pays, String operateur, 
            String numeroCible, BigDecimal montant, String devisePaiement) {
        
        // Vérifier les limites de montant
        if (!configurationService.validateMontant(pays, operateur, montant)) {
            throw new RuntimeException("Montant hors limites");
        }

        // Vérifier la disponibilité du stock
        if (!stockService.checkStockAvailability(pays, operateur, montant)) {
            throw new RuntimeException("Stock insuffisant");
        }

        RechargeTransaction transaction = new RechargeTransaction();
        transaction.setUser(user);
        transaction.setPays(pays);
        transaction.setOperateur(operateur);
        transaction.setNumeroCible(numeroCible);
        transaction.setMontant(montant.doubleValue());
        transaction.setDevisePaiement(devisePaiement);
        transaction.setStatut("EN_ATTENTE");
        transaction.setTypeRecharge(RechargeTransaction.TypeRecharge.DIRECTE);
        transaction.setDateDemande(LocalDateTime.now());

        // Consommer le stock
        stockService.consumeFromStock(pays, operateur, montant);

        return rechargeTransactionRepository.save(transaction);
    }
} 