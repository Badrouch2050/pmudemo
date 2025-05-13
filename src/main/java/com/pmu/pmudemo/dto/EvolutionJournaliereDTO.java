package com.pmu.pmudemo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EvolutionJournaliereDTO {
    private LocalDate date;
    private int nombreOperations;
    private BigDecimal montantTotal;

    // Getters and Setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public int getNombreOperations() { return nombreOperations; }
    public void setNombreOperations(int nombreOperations) { this.nombreOperations = nombreOperations; }

    public BigDecimal getMontantTotal() { return montantTotal; }
    public void setMontantTotal(BigDecimal montantTotal) { this.montantTotal = montantTotal; }
} 