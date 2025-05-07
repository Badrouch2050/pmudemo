package com.pmu.pmudemo.repositories;

import com.pmu.pmudemo.domains.Dispute;
import com.pmu.pmudemo.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisputeRepository extends JpaRepository<Dispute, Long> {
    List<Dispute> findByUserAndStatutNot(User user, String statut);
    List<Dispute> findByTransactionId(Long transactionId);
} 