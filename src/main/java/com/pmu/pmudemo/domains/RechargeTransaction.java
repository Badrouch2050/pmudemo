package com.pmu.pmudemo.domains;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.Data;

@Entity
@Table(name = "recharge_transaction")
public class RechargeTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_recharge_transaction_user"))
    private User user;
    private String operateur;
    private String numeroCible;
    private Double montant;
    private String devisePaiement;
    private String statut; // EN_ATTENTE, EN_COURS, TERMINEE, ECHOUEE
    private String stripeSessionId;
    private LocalDateTime dateDemande;
    private LocalDateTime dateTraitement;
    @ManyToOne
    private StockCarte codeRecharge; // nullable
    @ManyToOne
    @JoinColumn(name = "agent_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_recharge_transaction_agent"))
    private User agent; // nullable
    private Double montantCarte; // valeur de la carte, ex : 5
    private String deviseCarte; // ex : TND
    private Double tauxDeChange; // taux utilisé pour la conversion
    private Double fraisConversion; // montant des frais/marge appliqués
    private String pays;
    private Double commission; // montant de la commission prélevée
    private String typeCommission; // POURCENTAGE ou FIXE
    private Double commissionBase; // valeur de base (pourcentage ou montant)

    @Enumerated(EnumType.STRING)
    @Column(name = "type_traitement", nullable = false)
    private TypeTraitement typeTraitement;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_recharge", nullable = false)
    private TypeRecharge typeRecharge = TypeRecharge.CARTE;

    public enum TypeTraitement {
        AUTOMATIQUE,  // Traité par le système
        MANUELLE      // Traité par un agent
    }

    public enum TypeRecharge {
        CARTE,    // Recharge via une carte
        DIRECTE   // Recharge directe
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getOperateur() { return operateur; }
    public void setOperateur(String operateur) { this.operateur = operateur; }
    public String getNumeroCible() { return numeroCible; }
    public void setNumeroCible(String numeroCible) { this.numeroCible = numeroCible; }
    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }
    public String getDevisePaiement() { return devisePaiement; }
    public void setDevisePaiement(String devisePaiement) { this.devisePaiement = devisePaiement; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public String getStripeSessionId() { return stripeSessionId; }
    public void setStripeSessionId(String stripeSessionId) { this.stripeSessionId = stripeSessionId; }
    public LocalDateTime getDateDemande() { return dateDemande; }
    public void setDateDemande(LocalDateTime dateDemande) { this.dateDemande = dateDemande; }
    public LocalDateTime getDateTraitement() { return dateTraitement; }
    public void setDateTraitement(LocalDateTime dateTraitement) { this.dateTraitement = dateTraitement; }
    public StockCarte getCodeRecharge() { return codeRecharge; }
    public void setCodeRecharge(StockCarte codeRecharge) { this.codeRecharge = codeRecharge; }
    public User getAgent() { return agent; }
    public void setAgent(User agent) { this.agent = agent; }
    public Double getMontantCarte() { return montantCarte; }
    public void setMontantCarte(Double montantCarte) { this.montantCarte = montantCarte; }
    public String getDeviseCarte() { return deviseCarte; }
    public void setDeviseCarte(String deviseCarte) { this.deviseCarte = deviseCarte; }
    public Double getTauxDeChange() { return tauxDeChange; }
    public void setTauxDeChange(Double tauxDeChange) { this.tauxDeChange = tauxDeChange; }
    public Double getFraisConversion() { return fraisConversion; }
    public void setFraisConversion(Double fraisConversion) { this.fraisConversion = fraisConversion; }
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
    public Double getCommission() { return commission; }
    public void setCommission(Double commission) { this.commission = commission; }
    public String getTypeCommission() { return typeCommission; }
    public void setTypeCommission(String typeCommission) { this.typeCommission = typeCommission; }
    public Double getCommissionBase() { return commissionBase; }
    public void setCommissionBase(Double commissionBase) { this.commissionBase = commissionBase; }
    public TypeTraitement getTypeTraitement() {
        return typeTraitement;
    }
    public void setTypeTraitement(TypeTraitement typeTraitement) {
        this.typeTraitement = typeTraitement;
    }
    public TypeRecharge getTypeRecharge() {
        return typeRecharge;
    }
    public void setTypeRecharge(TypeRecharge typeRecharge) {
        this.typeRecharge = typeRecharge;
    }
} 