package com.pmu.pmudemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

public class DirectRechargeDTO {
    @NotNull(message = "L'ID de l'utilisateur est obligatoire")
    private Long userId;

    @NotBlank(message = "Le pays est obligatoire")
    private String pays;

    @NotBlank(message = "L'opérateur est obligatoire")
    private String operateur;

    @NotBlank(message = "Le numéro cible est obligatoire")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Format de numéro de téléphone invalide")
    private String numeroCible;

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant doit être supérieur à 0")
    private BigDecimal montant;

    @NotBlank(message = "La devise de paiement est obligatoire")
    private String devisePaiement;

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
    public String getOperateur() { return operateur; }
    public void setOperateur(String operateur) { this.operateur = operateur; }
    public String getNumeroCible() { return numeroCible; }
    public void setNumeroCible(String numeroCible) { this.numeroCible = numeroCible; }
    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }
    public String getDevisePaiement() { return devisePaiement; }
    public void setDevisePaiement(String devisePaiement) { this.devisePaiement = devisePaiement; }
} 