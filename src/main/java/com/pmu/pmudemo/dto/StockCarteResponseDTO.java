package com.pmu.pmudemo.dto;

import java.time.LocalDateTime;

public class StockCarteResponseDTO {
    private Long id;
    private String operateur;
    private Double montant;
    private String code;
    private String statut;
    private LocalDateTime dateAjout;
    private LocalDateTime dateUtilisation;
    private String pays;

    // Getters and Setters
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
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
} 