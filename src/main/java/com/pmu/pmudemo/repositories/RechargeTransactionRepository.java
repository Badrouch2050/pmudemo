package com.pmu.pmudemo.repositories;

import com.pmu.pmudemo.domains.RechargeTransaction;
import com.pmu.pmudemo.domains.User;
import com.pmu.pmudemo.services.dto.MonthRevenueDTO;
import com.pmu.pmudemo.services.dto.OperatorRevenueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RechargeTransactionRepository extends JpaRepository<RechargeTransaction, Long>, JpaSpecificationExecutor<RechargeTransaction> {
    @Query("SELECT SUM(r.montant) FROM RechargeTransaction r WHERE r.statut = :statut")
    BigDecimal sumAmountByStatus(@Param("statut") String statut);

    @Query("SELECT new com.pmu.pmudemo.services.dto.MonthRevenueDTO(YEAR(r.dateTraitement), MONTH(r.dateTraitement), COALESCE(SUM(r.montant),0)) FROM RechargeTransaction r WHERE r.statut = :statut GROUP BY YEAR(r.dateTraitement), MONTH(r.dateTraitement) ORDER BY YEAR(r.dateTraitement), MONTH(r.dateTraitement)")
    List<MonthRevenueDTO> sumAmountByMonth(@Param("statut") String statut);

    @Query("SELECT new com.pmu.pmudemo.services.dto.OperatorRevenueDTO(r.operateur, COALESCE(SUM(r.montant),0)) FROM RechargeTransaction r WHERE r.statut = :statut GROUP BY r.operateur")
    List<OperatorRevenueDTO> sumAmountByOperator(@Param("statut") String statut);

    @Query("SELECT new com.pmu.pmudemo.services.dto.MonthRevenueDTO(YEAR(r.dateTraitement), MONTH(r.dateTraitement), COALESCE(SUM(r.montant),0)) FROM RechargeTransaction r WHERE r.statut = :statut AND r.dateTraitement >= :start AND r.dateTraitement < :end AND (:operator IS NULL OR r.operateur = :operator) GROUP BY YEAR(r.dateTraitement), MONTH(r.dateTraitement) ORDER BY YEAR(r.dateTraitement), MONTH(r.dateTraitement)")
    List<MonthRevenueDTO> sumAmountByMonthFiltered(@Param("statut") String statut, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("operator") String operator);

    @Query("SELECT new com.pmu.pmudemo.services.dto.OperatorRevenueDTO(r.operateur, COALESCE(SUM(r.montant),0)) FROM RechargeTransaction r WHERE r.statut = :statut AND r.dateTraitement >= :start AND r.dateTraitement < :end GROUP BY r.operateur")
    List<OperatorRevenueDTO> sumAmountByOperatorFiltered(@Param("statut") String statut, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    Long countByStatut(String statut);
    
    Page<RechargeTransaction> findByStatut(String statut, Pageable pageable);

    List<RechargeTransaction> findByUser(User user);

    List<RechargeTransaction> findByUserOrderByDateDemandeDesc(User user);

    List<RechargeTransaction> findByAgent(User agent);

    List<RechargeTransaction> findByTypeTraitement(RechargeTransaction.TypeTraitement typeTraitement);
} 