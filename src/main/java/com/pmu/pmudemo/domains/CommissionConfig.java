package com.pmu.pmudemo.domains;

import jakarta.persistence.*;

@Entity
@Table(name = "commission_config")
public class CommissionConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pays;
    private String operateur; // null ou vide = global pour le pays
    private String typeCommission; // POURCENTAGE ou FIXE
    private Double valeur;
    private boolean actif = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
    public String getOperateur() { return operateur; }
    public void setOperateur(String operateur) { this.operateur = operateur; }
    public String getTypeCommission() { return typeCommission; }
    public void setTypeCommission(String typeCommission) { this.typeCommission = typeCommission; }
    public Double getValeur() { return valeur; }
    public void setValeur(Double valeur) { this.valeur = valeur; }
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
} 