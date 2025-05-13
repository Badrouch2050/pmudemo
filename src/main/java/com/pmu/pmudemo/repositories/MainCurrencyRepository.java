package com.pmu.pmudemo.repositories;

import com.pmu.pmudemo.domains.MainCurrency;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MainCurrencyRepository extends JpaRepository<MainCurrency, Long> {
    List<MainCurrency> findByActiveTrue();
    Optional<MainCurrency> findByCode(String code);
} 