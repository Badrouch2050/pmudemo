package com.pmu.pmudemo.domains;

import jakarta.persistence.*;

@Entity
@Table(name = "operator")
public class Operator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String codeDetection;
    private String statut;
    private String logoUrl;
    private boolean actif = true;
    private String pays;
    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getCodeDetection() { return codeDetection; }
    public void setCodeDetection(String codeDetection) { this.codeDetection = codeDetection; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
} 