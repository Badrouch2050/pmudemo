package com.pmu.pmudemo.domains;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import lombok.Data;

@Embeddable
@Data
public class Contact {
    @Column(nullable = false)
    private String numero;
    
    @Column(nullable = false)
    private String nom;
} 