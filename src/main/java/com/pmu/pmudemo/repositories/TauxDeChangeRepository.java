package com.pmu.pmudemo.repositories;

import com.pmu.pmudemo.domains.TauxDeChange;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TauxDeChangeRepository extends JpaRepository<TauxDeChange, Long> {
    Optional<TauxDeChange> findTopByDeviseSourceAndDeviseCibleOrderByDateObtentionDesc(String deviseSource, String deviseCible);
} 