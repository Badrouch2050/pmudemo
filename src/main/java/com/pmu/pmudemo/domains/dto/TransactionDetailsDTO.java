package com.pmu.pmudemo.domains.dto;

import com.pmu.pmudemo.domains.RechargeTransaction;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TransactionDetailsDTO {
    private Long id;
    private String operateur;
    private String numeroCible;
    private Double montant;
    private String devisePaiement;
    private String statut;
    private LocalDateTime dateDemande;
    private LocalDateTime dateTraitement;
    private String typeTraitement;
    private String agentNom;
    private String agentEmail;
    private Double montantCarte;
    private String deviseCarte;
    private Double tauxDeChange;
    private Double fraisConversion;
    private String pays;
    private Double commission;
    private String typeCommission;
    private Double commissionBase;
    private String clientNom;
    private String clientEmail;
    private String typeRecharge;
    private String codeRecharge;

    public static TransactionDetailsDTO fromEntity(RechargeTransaction transaction) {
        TransactionDetailsDTO dto = new TransactionDetailsDTO();
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
            dto.setAgentEmail(transaction.getAgent().getEmail());
        }
        
        dto.setMontantCarte(transaction.getMontantCarte());
        dto.setDeviseCarte(transaction.getDeviseCarte());
        dto.setTauxDeChange(transaction.getTauxDeChange());
        dto.setFraisConversion(transaction.getFraisConversion());
        dto.setPays(transaction.getPays());
        dto.setCommission(transaction.getCommission());
        dto.setTypeCommission(transaction.getTypeCommission());
        dto.setCommissionBase(transaction.getCommissionBase());
        
        if (transaction.getUser() != null) {
            dto.setClientNom(transaction.getUser().getNom());
            dto.setClientEmail(transaction.getUser().getEmail());
        }
        
        dto.setTypeRecharge(transaction.getTypeRecharge().name());
        if (transaction.getCodeRecharge() != null) {
            dto.setCodeRecharge(transaction.getCodeRecharge().getCode());
        }
        
        return dto;
    }
} 