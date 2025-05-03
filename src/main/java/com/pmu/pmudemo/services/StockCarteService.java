package com.pmu.pmudemo.services;

import com.pmu.pmudemo.domains.StockCarte;
import com.pmu.pmudemo.domains.RechargeTransaction;
import com.pmu.pmudemo.repositories.StockCarteRepository;
import com.pmu.pmudemo.repositories.RechargeTransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockCarteService {
    private final StockCarteRepository stockCarteRepo;
    private final RechargeTransactionRepository rechargeRepo;

    public StockCarteService(StockCarteRepository stockCarteRepo, RechargeTransactionRepository rechargeRepo) {
        this.stockCarteRepo = stockCarteRepo;
        this.rechargeRepo = rechargeRepo;
    }

    public Page<StockCarte> getAllStock(Pageable pageable) {
        return stockCarteRepo.findAll(pageable);
    }

    public StockCarte addStock(StockCarte carte) {
        carte.setStatut("DISPONIBLE");
        return stockCarteRepo.save(carte);
    }

    public void deleteStock(Long id) {
        stockCarteRepo.deleteById(id);
    }

    public Optional<StockCarte> getAvailableCard(String operateur, Double montant) {
        return stockCarteRepo.findAll().stream()
                .filter(c -> c.getOperateur().equals(operateur) && c.getMontant().equals(montant) && c.getStatut().equals("DISPONIBLE"))
                .findFirst();
    }

    public void assignCardToTransaction(Long carteId, Long transactionId) {
        StockCarte carte = stockCarteRepo.findById(carteId).orElseThrow();
        RechargeTransaction transaction = rechargeRepo.findById(transactionId).orElseThrow();
        carte.setStatut("UTILISE");
        carte.setUtilisePourTransaction(transaction);
        stockCarteRepo.save(carte);
        transaction.setCodeRecharge(carte);
        transaction.setStatut("TERMINEE");
        rechargeRepo.save(transaction);
    }
} 