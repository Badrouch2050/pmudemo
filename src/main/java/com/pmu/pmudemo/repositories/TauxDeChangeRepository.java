package com.pmu.pmudemo.repositories;

import com.pmu.pmudemo.domains.TauxDeChange;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface TauxDeChangeRepository extends JpaRepository<TauxDeChange, Long> {
    Optional<TauxDeChange> findTopByDeviseSourceAndDeviseCibleOrderByDateObtentionDesc(String deviseSource, String deviseCible);
    List<TauxDeChange> findHistoriqueById(Long id);
} 