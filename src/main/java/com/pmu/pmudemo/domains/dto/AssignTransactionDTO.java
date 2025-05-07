package com.pmu.pmudemo.domains.dto;

import lombok.Data;

@Data
public class AssignTransactionDTO {
    private Long agentId;
    private String commentaire; // Optionnel, pour expliquer l'assignation
} 