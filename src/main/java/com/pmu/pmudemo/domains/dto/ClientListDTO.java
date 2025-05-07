package com.pmu.pmudemo.domains.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ClientListDTO {
    private Long id;
    private String nom;
    private String email;
    private String statut;
    private LocalDateTime dateInscription;
    private boolean actif;
} 