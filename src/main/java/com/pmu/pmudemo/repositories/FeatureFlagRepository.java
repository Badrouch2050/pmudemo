package com.pmu.pmudemo.repositories;

import com.pmu.pmudemo.domains.FeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {
    // méthodes personnalisées si besoin
} 