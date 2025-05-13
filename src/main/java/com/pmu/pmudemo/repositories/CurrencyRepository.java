package com.pmu.pmudemo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pmu.pmudemo.domains.Currency;
import java.util.List;
import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    List<Currency> findByIsActiveTrueOrderByPriorityAsc();
    Optional<Currency> findByCode(String code);
} 