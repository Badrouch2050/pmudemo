package com.pmu.pmudemo.domains.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DisputeDTO {
    private Long id;
    private Long transactionId;
    private String motif;
    private String statut;
    private String commentaire;
    private LocalDateTime dateCreation;
    private LocalDateTime dateResolution;
} 