package com.pmu.pmudemo.dto;

import java.math.BigDecimal;

public class OperateurStatsDTO {
    private String operateur;
    private int nombreStocks;
    private BigDecimal montantTotal;
    private BigDecimal montantDisponible;

    // Getters and Setters
    public String getOperateur() { return operateur; }
    public void setOperateur(String operateur) { this.operateur = operateur; }

    public int getNombreStocks() { return nombreStocks; }
    public void setNombreStocks(int nombreStocks) { this.nombreStocks = nombreStocks; }

    public BigDecimal getMontantTotal() { return montantTotal; }
    public void setMontantTotal(BigDecimal montantTotal) { this.montantTotal = montantTotal; }

    public BigDecimal getMontantDisponible() { return montantDisponible; }
    public void setMontantDisponible(BigDecimal montantDisponible) { this.montantDisponible = montantDisponible; }
} 