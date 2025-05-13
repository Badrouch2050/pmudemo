package com.pmu.pmudemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class RechargeConfigurationDTO {
    @NotBlank(message = "Le pays est obligatoire")
    private String pays;

    @NotBlank(message = "L'opérateur est obligatoire")
    private String operateur;

    @NotNull(message = "Le montant minimum est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant minimum doit être supérieur à 0")
    private BigDecimal montantMin;

    @NotNull(message = "Le montant maximum est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant maximum doit être supérieur à 0")
    private BigDecimal montantMax;

    // Getters and Setters
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
    public String getOperateur() { return operateur; }
    public void setOperateur(String operateur) { this.operateur = operateur; }
    public BigDecimal getMontantMin() { return montantMin; }
    public void setMontantMin(BigDecimal montantMin) { this.montantMin = montantMin; }
    public BigDecimal getMontantMax() { return montantMax; }
    public void setMontantMax(BigDecimal montantMax) { this.montantMax = montantMax; }
} 