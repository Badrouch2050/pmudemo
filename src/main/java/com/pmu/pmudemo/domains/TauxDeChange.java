package com.pmu.pmudemo.domains;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "taux_de_change")
public class TauxDeChange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String deviseSource;
    private String deviseCible;
    private double taux;
    private LocalDateTime dateObtention;
    private boolean actif = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDeviseSource() { return deviseSource; }
    public void setDeviseSource(String deviseSource) { this.deviseSource = deviseSource; }
    public String getDeviseCible() { return deviseCible; }
    public void setDeviseCible(String deviseCible) { this.deviseCible = deviseCible; }
    public double getTaux() { return taux; }
    public void setTaux(double taux) { this.taux = taux; }
    public LocalDateTime getDateObtention() { return dateObtention; }
    public void setDateObtention(LocalDateTime dateObtention) { this.dateObtention = dateObtention; }
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
} 