package com.pmu.pmudemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class StockCarteDTO {
    @NotBlank(message = "L'opérateur est obligatoire")
    private String operateur;
    
    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private Double montant;
    
    @NotBlank(message = "Le code est obligatoire")
    private String code;
    
    @NotBlank(message = "Le pays est obligatoire")
    private String pays;

    // Getters and Setters
    public String getOperateur() { return operateur; }
    public void setOperateur(String operateur) { this.operateur = operateur; }
    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
} 