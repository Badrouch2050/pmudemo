package com.pmu.pmudemo.services;

import com.pmu.pmudemo.domains.RechargeConfiguration;
import com.pmu.pmudemo.repositories.RechargeConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class RechargeConfigurationService {

    @Autowired
    private RechargeConfigurationRepository configurationRepository;

    @Transactional
    public RechargeConfiguration createConfiguration(String pays, String operateur, 
            BigDecimal montantMin, BigDecimal montantMax) {
        RechargeConfiguration config = new RechargeConfiguration();
        config.setPays(pays);
        config.setOperateur(operateur);
        config.setMontantMin(montantMin);
        config.setMontantMax(montantMax);
        return configurationRepository.save(config);
    }

    @Transactional
    public RechargeConfiguration updateConfiguration(Long id, BigDecimal montantMin, BigDecimal montantMax) {
        RechargeConfiguration config = configurationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Configuration non trouvée"));
        
        config.setMontantMin(montantMin);
        config.setMontantMax(montantMax);
        return configurationRepository.save(config);
    }

    public Optional<RechargeConfiguration> getConfiguration(String pays, String operateur) {
        return configurationRepository.findByPaysAndOperateur(pays, operateur);
    }

    public List<RechargeConfiguration> getAllConfigurations() {
        return configurationRepository.findAll();
    }

    @Transactional
    public void deleteConfiguration(Long id) {
        configurationRepository.deleteById(id);
    }

    public boolean validateMontant(String pays, String operateur, BigDecimal montant) {
        return configurationRepository.findByPaysAndOperateur(pays, operateur)
            .map(config -> montant.compareTo(config.getMontantMin()) >= 0 
                && montant.compareTo(config.getMontantMax()) <= 0)
            .orElse(false);
    }
} 