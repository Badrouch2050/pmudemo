package com.pmu.pmudemo.domains.dto;

import com.pmu.pmudemo.domains.RechargeTransaction;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TransactionListDTO {
    private Long id;
    private String operateur;
    private String numeroCible;
    private Double montant;
    private String devisePaiement;
    private String statut;
    private LocalDateTime dateDemande;
    private LocalDateTime dateTraitement;
    private String typeTraitement;
    private String agentNom; // Nom de l'agent si traitement manuel
    private String pays;
    private Double commission;
    private String clientNom; // Nom de l'utilisateur qui a effectué la transaction

    public static TransactionListDTO fromEntity(RechargeTransaction transaction) {
        TransactionListDTO dto = new TransactionListDTO();
        dto.setId(transaction.getId());
        dto.setOperateur(transaction.getOperateur());
        dto.setNumeroCible(transaction.getNumeroCible());
        dto.setMontant(transaction.getMontant());
        dto.setDevisePaiement(transaction.getDevisePaiement());
        dto.setStatut(transaction.getStatut());
        dto.setDateDemande(transaction.getDateDemande());
        dto.setDateTraitement(transaction.getDateTraitement());
        dto.setTypeTraitement(transaction.getTypeTraitement().name());
        if (transaction.getAgent() != null) {
            dto.setAgentNom(transaction.getAgent().getNom());
        }
        dto.setPays(transaction.getPays());
        dto.setCommission(transaction.getCommission());
        if (transaction.getUser() != null) {
            dto.setClientNom(transaction.getUser().getNom());
        }
        return dto;
    }
} 