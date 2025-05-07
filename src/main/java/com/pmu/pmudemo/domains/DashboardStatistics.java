package com.pmu.pmudemo.domains;

import lombok.Data;

@Data
public class DashboardStatistics {
    private Double montantTotalRecharge = 0.0;
    private Integer nombreRecharges = 0;
    private Double montantMoyenRecharge = 0.0;
    private String operateurPrefere;
    private String paysPrefere;
    private Double bonusTotal = 0.0;
    private Integer nombreParrainages = 0;
} 