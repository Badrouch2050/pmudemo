package com.pmu.pmudemo.services;

import com.pmu.pmudemo.domains.FeatureFlag;
import com.pmu.pmudemo.repositories.FeatureFlagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeatureFlagService {
    private final FeatureFlagRepository featureFlagRepo;

    public FeatureFlagService(FeatureFlagRepository featureFlagRepo) {
        this.featureFlagRepo = featureFlagRepo;
    }

    public List<FeatureFlag> getAllFeatures() {
        return featureFlagRepo.findAll();
    }

    public void activateFeature(Long id) {
        featureFlagRepo.findById(id).ifPresent(f -> {
            f.setActif(true);
            featureFlagRepo.save(f);
        });
    }

    public void deactivateFeature(Long id) {
        featureFlagRepo.findById(id).ifPresent(f -> {
            f.setActif(false);
            featureFlagRepo.save(f);
        });
    }

    public List<FeatureFlag> getActiveFeatures() {
        return featureFlagRepo.findAll().stream().filter(FeatureFlag::getActif).toList();
    }

    public boolean isAutoCodeDeliveryEnabled() {
        return featureFlagRepo.findAll().stream()
                .anyMatch(f -> "auto_code_delivery".equals(f.getNom()) && Boolean.TRUE.equals(f.getActif()));
    }
} 