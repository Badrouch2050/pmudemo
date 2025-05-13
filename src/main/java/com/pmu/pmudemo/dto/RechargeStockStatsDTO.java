package com.pmu.pmudemo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class RechargeStockStatsDTO {
    private int totalStocks;
    private BigDecimal totalMontant;
    private BigDecimal totalDisponible;
    private int nombreOperations;
    private List<PaysStatsDTO> statistiquesParPays;
    private List<OperateurStatsDTO> statistiquesParOperateur;
    private List<EvolutionJournaliereDTO> evolutionJournaliere;

    // Getters and Setters
    public int getTotalStocks() { return totalStocks; }
    public void setTotalStocks(int totalStocks) { this.totalStocks = totalStocks; }

    public BigDecimal getTotalMontant() { return totalMontant; }
    public void setTotalMontant(BigDecimal totalMontant) { this.totalMontant = totalMontant; }

    public BigDecimal getTotalDisponible() { return totalDisponible; }
    public void setTotalDisponible(BigDecimal totalDisponible) { this.totalDisponible = totalDisponible; }

    public int getNombreOperations() { return nombreOperations; }
    public void setNombreOperations(int nombreOperations) { this.nombreOperations = nombreOperations; }

    public List<PaysStatsDTO> getStatistiquesParPays() { return statistiquesParPays; }
    public void setStatistiquesParPays(List<PaysStatsDTO> statistiquesParPays) { this.statistiquesParPays = statistiquesParPays; }

    public List<OperateurStatsDTO> getStatistiquesParOperateur() { return statistiquesParOperateur; }
    public void setStatistiquesParOperateur(List<OperateurStatsDTO> statistiquesParOperateur) { this.statistiquesParOperateur = statistiquesParOperateur; }

    public List<EvolutionJournaliereDTO> getEvolutionJournaliere() { return evolutionJournaliere; }
    public void setEvolutionJournaliere(List<EvolutionJournaliereDTO> evolutionJournaliere) { this.evolutionJournaliere = evolutionJournaliere; }

} 