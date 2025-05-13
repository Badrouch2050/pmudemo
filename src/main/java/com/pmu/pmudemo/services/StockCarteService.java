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

import org.springframework.data.domain.PageImpl;

@Service
public class StockCarteService {
    private final StockCarteRepository stockCarteRepository;
    private final RechargeTransactionRepository rechargeRepo;

    public StockCarteService(StockCarteRepository stockCarteRepo, RechargeTransactionRepository rechargeRepo) {
        this.stockCarteRepository = stockCarteRepo;
        this.rechargeRepo = rechargeRepo;
    }

    public Page<StockCarte> getAllStock(Pageable pageable) {
        return stockCarteRepository.findAll(pageable);
    }

    public StockCarte addStock(StockCarte carte) {
        carte.setStatut("DISPONIBLE");
        return stockCarteRepository.save(carte);
    }

    public void deleteStock(Long id) {
        stockCarteRepository.deleteById(id);
    }

    public Optional<StockCarte> getAvailableCard(String operateur, Double montant) {
        return stockCarteRepository.findAll().stream()
                .filter(c -> c.getOperateur().equals(operateur) && c.getMontant().equals(montant) && c.getStatut().equals("DISPONIBLE"))
                .findFirst();
    }

    

    public Page<StockCarte> findAll(Pageable pageable) {
        return stockCarteRepository.findAll(pageable);
    }

    public Optional<StockCarte> findById(Long id) { return stockCarteRepository.findById(id); }

    public void assignCardToTransaction(Long carteId, Long transactionId) {
        StockCarte carte = stockCarteRepository.findById(carteId).orElseThrow();
        RechargeTransaction transaction = rechargeRepo.findById(transactionId).orElseThrow();
        carte.setStatut("UTILISE");
        carte.setUtilisePourTransaction(transaction);
        stockCarteRepository.save(carte);
        transaction.setCodeRecharge(carte);
        transaction.setStatut("TERMINEE");
        rechargeRepo.save(transaction);
    }

    public Page<StockCarte> findByPaysIgnoreCase(String pays, Pageable pageable) {
        List<StockCarte> list = stockCarteRepository.findByPaysIgnoreCase(pays, pageable);
        return new PageImpl<>(list, pageable, list.size());
    }

    public Page<StockCarte> findByPaysAndStatutAndOperateurIgnoreCase(String pays, String statut, String operateur, Pageable pageable) {
        return stockCarteRepository.findByPaysAndStatutAndOperateurIgnoreCase(pays, statut, operateur, pageable);
    }

    public Page<StockCarte> findByPaysAndStatutIgnoreCase(String pays, String statut, Pageable pageable) {
        return stockCarteRepository.findByPaysAndStatutIgnoreCase(pays, statut, pageable);
    }

    public Page<StockCarte> findByPaysAndOperateurIgnoreCase(String pays, String operateur, Pageable pageable) {
        return stockCarteRepository.findByPaysAndOperateurIgnoreCase(pays, operateur, pageable);
    }

    public Page<StockCarte> findByStatutAndOperateurIgnoreCase(String statut, String operateur, Pageable pageable) {
        return stockCarteRepository.findByStatutAndOperateurIgnoreCase(statut, operateur, pageable);
    }

    public Page<StockCarte> findByStatutIgnoreCase(String statut, Pageable pageable) {
        return stockCarteRepository.findByStatutIgnoreCase(statut, pageable);
    }

    public Page<StockCarte> findByOperateurIgnoreCase(String operateur, Pageable pageable) {
        return stockCarteRepository.findByOperateurIgnoreCase(operateur, pageable);
    }
} 