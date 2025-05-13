package com.pmu.pmudemo.services;

import com.pmu.pmudemo.domains.RechargeStock;
import com.pmu.pmudemo.repositories.RechargeStockRepository;
import com.pmu.pmudemo.dto.RechargeStockStatsDTO;
import com.pmu.pmudemo.dto.PaysStatsDTO;
import com.pmu.pmudemo.dto.OperateurStatsDTO;
import com.pmu.pmudemo.dto.EvolutionJournaliereDTO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class RechargeStockService {
    private final RechargeStockRepository stockRepository;

    public RechargeStockService(RechargeStockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public RechargeStock createStock(String pays, String operateur, BigDecimal montantTotal) {
        RechargeStock stock = new RechargeStock();
        stock.setPays(pays);
        stock.setOperateur(operateur);
        stock.setMontantTotal(montantTotal);
        stock.setMontantDisponible(montantTotal);
        return stockRepository.save(stock);
    }

    @Transactional
    public RechargeStock addToStock(String pays, String operateur, BigDecimal montant) {
        RechargeStock stock = stockRepository.findByPaysAndOperateurWithLock(pays, operateur)
            .orElseGet(() -> createStock(pays, operateur, BigDecimal.ZERO));

        stock.setMontantTotal(stock.getMontantTotal().add(montant));
        stock.setMontantDisponible(stock.getMontantDisponible().add(montant));
        return stockRepository.save(stock);
    }

    @Transactional
    public RechargeStock consumeFromStock(String pays, String operateur, BigDecimal montant) {
        RechargeStock stock = stockRepository.findByPaysAndOperateurWithLock(pays, operateur)
            .orElseThrow(() -> new RuntimeException("Stock non trouvé"));

        if (stock.getMontantDisponible().compareTo(montant) < 0) {
            throw new RuntimeException("Stock insuffisant");
        }

        stock.setMontantDisponible(stock.getMontantDisponible().subtract(montant));
        return stockRepository.save(stock);
    }

    public Optional<RechargeStock> getStock(String pays, String operateur) {
        return stockRepository.findByPaysAndOperateur(pays, operateur);
    }

    public List<RechargeStock> getAllStocks() {
        return stockRepository.findAll();
    }

    public boolean checkStockAvailability(String pays, String operateur, BigDecimal montant) {
        return stockRepository.findByPaysAndOperateur(pays, operateur)
            .map(stock -> stock.getMontantDisponible().compareTo(montant) >= 0)
            .orElse(false);
    }

    public Page<RechargeStock> findAll(Pageable pageable) {
        return stockRepository.findAll(pageable);
    }

    public RechargeStockStatsDTO getStatistiques(LocalDate dateDebut, LocalDate dateFin) {
        // Validation des dates
        if (dateDebut != null && dateFin != null && dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("La date de début doit être antérieure à la date de fin");
        }

        RechargeStockStatsDTO stats = new RechargeStockStatsDTO();

        // Récupération des stocks avec filtrage par date si nécessaire
        List<RechargeStock> stocks = dateDebut != null && dateFin != null ?
            stockRepository.findByDateCreationBetween(dateDebut.atStartOfDay(), dateFin.atTime(23, 59, 59)) :
            stockRepository.findAll();

        // Calcul des totaux globaux
        stats.setTotalStocks(stocks.size());
        stats.setTotalMontant(stocks.stream()
            .map(RechargeStock::getMontantTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add));
        stats.setTotalDisponible(stocks.stream()
            .map(RechargeStock::getMontantDisponible)
            .reduce(BigDecimal.ZERO, BigDecimal::add));

        // Statistiques par pays
        Map<String, List<RechargeStock>> stocksParPays = stocks.stream()
            .collect(Collectors.groupingBy(RechargeStock::getPays));
        
        List<PaysStatsDTO> paysStats = stocksParPays.entrySet().stream()
            .map(entry -> {
                PaysStatsDTO paysStatsDTO = new PaysStatsDTO();
                paysStatsDTO.setPays(entry.getKey());
                paysStatsDTO.setNombreStocks(entry.getValue().size());
                paysStatsDTO.setMontantTotal(entry.getValue().stream()
                    .map(RechargeStock::getMontantTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
                paysStatsDTO.setMontantDisponible(entry.getValue().stream()
                    .map(RechargeStock::getMontantDisponible)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
                return paysStatsDTO;
            })
            .collect(Collectors.toList());
        stats.setStatistiquesParPays(paysStats);

        // Statistiques par opérateur
        Map<String, List<RechargeStock>> stocksParOperateur = stocks.stream()
            .collect(Collectors.groupingBy(RechargeStock::getOperateur));
        
        List<OperateurStatsDTO> operateurStats = stocksParOperateur.entrySet().stream()
            .map(entry -> {
                OperateurStatsDTO operateurStatsDTO = new OperateurStatsDTO();
                operateurStatsDTO.setOperateur(entry.getKey());
                operateurStatsDTO.setNombreStocks(entry.getValue().size());
                operateurStatsDTO.setMontantTotal(entry.getValue().stream()
                    .map(RechargeStock::getMontantTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
                operateurStatsDTO.setMontantDisponible(entry.getValue().stream()
                    .map(RechargeStock::getMontantDisponible)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
                return operateurStatsDTO;
            })
            .collect(Collectors.toList());
        stats.setStatistiquesParOperateur(operateurStats);

        // Évolution journalière
        if (dateDebut != null && dateFin != null) {
            Map<LocalDate, List<RechargeStock>> stocksParJour = stocks.stream()
                .collect(Collectors.groupingBy(stock -> 
                    stock.getDateCreation().toLocalDate()));

            List<EvolutionJournaliereDTO> evolutionJournaliere = stocksParJour.entrySet().stream()
                .map(entry -> {
                    EvolutionJournaliereDTO evolution = new EvolutionJournaliereDTO();
                    evolution.setDate(entry.getKey());
                    evolution.setNombreOperations(entry.getValue().size());
                    evolution.setMontantTotal(entry.getValue().stream()
                        .map(RechargeStock::getMontantTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
                    return evolution;
                })
                .sorted(Comparator.comparing(EvolutionJournaliereDTO::getDate))
                .collect(Collectors.toList());
            stats.setEvolutionJournaliere(evolutionJournaliere);
        }

        return stats;
    }
} 