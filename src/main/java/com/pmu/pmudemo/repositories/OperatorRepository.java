package com.pmu.pmudemo.repositories;

import com.pmu.pmudemo.domains.Operator;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface OperatorRepository extends JpaRepository<Operator, Long> {
    Optional<Operator> findByNomIgnoreCase(String nom);
    List<Operator> findByActifTrue();
    List<Operator> findByActifTrueAndPaysIgnoreCase(String pays);
} 