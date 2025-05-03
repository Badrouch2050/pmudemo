package com.pmu.pmudemo.domains;

import jakarta.persistence.*;

@Entity
@Table(name = "feature_flag")
public class FeatureFlag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nom;
    private Boolean actif;
    private String description;
    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
} 