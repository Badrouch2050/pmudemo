package com.pmu.pmudemo.domains;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_carte")
public class StockCarte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String operateur;
    private Double montant;
    @Column(unique = true)
    private String code;
    private String statut; // DISPONIBLE, UTILISE, EXPIRE
    private LocalDateTime dateAjout;
    private LocalDateTime dateUtilisation;
    @OneToOne
    private RechargeTransaction utilisePourTransaction; // nullable
    private String pays;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOperateur() { return operateur; }
    public void setOperateur(String operateur) { this.operateur = operateur; }
    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public LocalDateTime getDateAjout() { return dateAjout; }
    public void setDateAjout(LocalDateTime dateAjout) { this.dateAjout = dateAjout; }
    public LocalDateTime getDateUtilisation() { return dateUtilisation; }
    public void setDateUtilisation(LocalDateTime dateUtilisation) { this.dateUtilisation = dateUtilisation; }
    public RechargeTransaction getUtilisePourTransaction() { return utilisePourTransaction; }
    public void setUtilisePourTransaction(RechargeTransaction utilisePourTransaction) { this.utilisePourTransaction = utilisePourTransaction; }
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
} 