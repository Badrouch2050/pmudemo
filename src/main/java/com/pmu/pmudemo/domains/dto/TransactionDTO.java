package com.pmu.pmudemo.domains.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    private Long id;
    private String operateur;
    private String numeroCible;
    private Double montant;
    private String devisePaiement;
    private String statut;
    private LocalDateTime dateDemande;
    private LocalDateTime dateTraitement;
    private Double montantCarte;
    private String deviseCarte;
    private Double tauxDeChange;
    private Double fraisConversion;
    private String pays;
    private Double commission;
    private String typeCommission;
} 