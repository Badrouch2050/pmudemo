package com.pmu.pmudemo.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "currencies")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String code;        // EUR, USD, etc.
    private String name;        // Euro, US Dollar, etc.
    private String symbol;      // €, $, etc.
    private boolean isActive;   // Pour activer/désactiver
    private String region;      // Europe, USA, Gulf, etc.
    private int priority;       // Pour l'ordre d'affichage

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
    
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
} 