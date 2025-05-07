package com.pmu.pmudemo.domains.dto;

import lombok.Data;

@Data
public class ReferralInfoDTO {
    private String codeParrainage;
    private String parrainEmail;
    private Double montantTotalParrainage;
    private Integer nombreRecharges;
    private Double bonusTotal;
    private String statut;
} 