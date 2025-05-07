package com.pmu.pmudemo.domains.dto;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String ancienMotDePasse;
    private String nouveauMotDePasse;
} 