package com.pmu.pmudemo.repositories;

 

import com.pmu.pmudemo.domains.StockCarte;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface StockCarteRepository extends JpaRepository<StockCarte, Long> {
    // méthodes personnalisées si besoin
    List<StockCarte> findByPaysIgnoreCase(String pays, Pageable pageable);
    Page<StockCarte> findByPaysAndStatutAndOperateurIgnoreCase(String pays, String statut, String operateur, Pageable pageable);
    Page<StockCarte> findByPaysAndStatutIgnoreCase(String pays, String statut, Pageable pageable);
    Page<StockCarte> findByPaysAndOperateurIgnoreCase(String pays, String operateur, Pageable pageable);
    Page<StockCarte> findByStatutAndOperateurIgnoreCase(String statut, String operateur, Pageable pageable);
    Page<StockCarte> findByStatutIgnoreCase(String statut, Pageable pageable);
    Page<StockCarte> findByOperateurIgnoreCase(String operateur, Pageable pageable);
} 