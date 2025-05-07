package com.pmu.pmudemo.domains.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ClientDetailsDTO {
    private Long id;
    private String nom;
    private String email;
    private String role;
    private String statut;
    private String methodeAuthentification;
    private LocalDateTime dateInscription;
    private boolean actif;

    // Statistiques
    private Double montantTotalRecharge;
    private Integer nombreRecharges;
    private Double montantMoyenRecharge;
    private String operateurPrefere;
    private String paysPrefere;

    // Contacts fréquents
    private List<ContactDTO> contactsFrequents;

    // Dernières transactions
    private List<TransactionDTO> dernieresTransactions;

    // Informations de parrainage
    private ReferralInfoDTO referralInfo;

    // Litiges en cours
    private List<DisputeDTO> litigesEnCours;
} 