package com.pmu.pmudemo.domains.dto;

import lombok.Data;

@Data
public class UpdateTransactionStatusDTO {
    private String statut;
    private String commentaire; // Optionnel, pour expliquer le changement de statut
} 