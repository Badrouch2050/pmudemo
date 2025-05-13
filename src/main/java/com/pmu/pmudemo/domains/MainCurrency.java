package com.pmu.pmudemo.domains;

import jakarta.persistence.*;

@Entity
@Table(name = "main_currency")
public class MainCurrency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String code;        // TND
    private String name;        // Tunisian Dinar
    private String symbol;      // د.ت
    private boolean active;   // Toujours true
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}