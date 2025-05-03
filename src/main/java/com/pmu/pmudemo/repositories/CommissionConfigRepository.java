package com.pmu.pmudemo.repositories;

import com.pmu.pmudemo.domains.CommissionConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CommissionConfigRepository extends JpaRepository<CommissionConfig, Long> {
    List<CommissionConfig> findByPaysAndActifTrue(String pays);
    List<CommissionConfig> findByPaysAndOperateurAndActifTrue(String pays, String operateur);
    Optional<CommissionConfig> findTopByPaysAndOperateurAndActifTrueOrderByIdDesc(String pays, String operateur);
    Optional<CommissionConfig> findTopByPaysAndOperateurIsNullAndActifTrueOrderByIdDesc(String pays);
} 