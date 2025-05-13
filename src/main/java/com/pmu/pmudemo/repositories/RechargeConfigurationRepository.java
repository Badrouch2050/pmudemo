package com.pmu.pmudemo.repositories;

import com.pmu.pmudemo.domains.RechargeConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RechargeConfigurationRepository extends JpaRepository<RechargeConfiguration, Long> {
    Optional<RechargeConfiguration> findByPaysAndOperateur(String pays, String operateur);
} 