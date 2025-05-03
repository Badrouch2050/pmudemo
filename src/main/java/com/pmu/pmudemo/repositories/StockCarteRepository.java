package com.pmu.pmudemo.repositories;

import com.pmu.pmudemo.domains.StockCarte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockCarteRepository extends JpaRepository<StockCarte, Long> {
    // méthodes personnalisées si besoin
    List<StockCarte> findByPaysIgnoreCase(String pays);
} 