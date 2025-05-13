package com.pmu.pmudemo.repositories;

import com.pmu.pmudemo.domains.RechargeStock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RechargeStockRepository extends JpaRepository<RechargeStock, Long> {
    Optional<RechargeStock> findByPaysAndOperateur(String pays, String operateur);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT rs FROM RechargeStock rs WHERE rs.pays = :pays AND rs.operateur = :operateur")
    Optional<RechargeStock> findByPaysAndOperateurWithLock(
        @Param("pays") String pays,
        @Param("operateur") String operateur
    );

    List<RechargeStock> findByDateCreationBetween(LocalDateTime dateDebut, LocalDateTime dateFin);
} 