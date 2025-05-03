package com.pmu.pmudemo.services;

import com.pmu.pmudemo.domains.CommissionConfig;
import com.pmu.pmudemo.repositories.CommissionConfigRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CommissionConfigService {
    private final CommissionConfigRepository repository;

    public CommissionConfigService(CommissionConfigRepository repository) {
        this.repository = repository;
    }

    public Optional<CommissionConfig> getCommission(String pays, String operateur) {
        // 1. Cherche commission spécifique pays+opérateur
        Optional<CommissionConfig> specific = repository.findTopByPaysAndOperateurAndActifTrueOrderByIdDesc(pays, operateur);
        if (specific.isPresent()) return specific;
        // 2. Cherche commission globale pour le pays
        Optional<CommissionConfig> global = repository.findTopByPaysAndOperateurIsNullAndActifTrueOrderByIdDesc(pays);
        return global;
    }
} 