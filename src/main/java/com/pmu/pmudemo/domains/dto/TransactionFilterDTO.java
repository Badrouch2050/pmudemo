package com.pmu.pmudemo.domains.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TransactionFilterDTO {
    private String statut;
    private String typeTraitement;
    private String operateur;
    private String pays;
    private Long agentId;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private Double montantMin;
    private Double montantMax;
} 