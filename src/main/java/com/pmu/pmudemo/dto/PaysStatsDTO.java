package com.pmu.pmudemo.dto;

import java.math.BigDecimal;

public class PaysStatsDTO {
    private String pays;
    private int nombreStocks;
    private BigDecimal montantTotal;
    private BigDecimal montantDisponible;

    // Getters and Setters
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }

    public int getNombreStocks() { return nombreStocks; }
    public void setNombreStocks(int nombreStocks) { this.nombreStocks = nombreStocks; }

    public BigDecimal getMontantTotal() { return montantTotal; }
    public void setMontantTotal(BigDecimal montantTotal) { this.montantTotal = montantTotal; }

    public BigDecimal getMontantDisponible() { return montantDisponible; }
    public void setMontantDisponible(BigDecimal montantDisponible) { this.montantDisponible = montantDisponible; }
} 