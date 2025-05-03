package com.pmu.pmudemo.repositories;

import com.pmu.pmudemo.domains.Dispute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisputeRepository extends JpaRepository<Dispute, Long> {
    List<Dispute> findByTransactionId(Long transactionId);
} 