package com.pmu.pmudemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class RechargeStockDTO {
    @NotBlank(message = "Le pays est obligatoire")
    private String pays;

    @NotBlank(message = "L'opérateur est obligatoire")
    private String operateur;

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant doit être supérieur à 0")
    private BigDecimal montant;

    // Getters and Setters
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
    public String getOperateur() { return operateur; }
    public void setOperateur(String operateur) { this.operateur = operateur; }
    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }
} 