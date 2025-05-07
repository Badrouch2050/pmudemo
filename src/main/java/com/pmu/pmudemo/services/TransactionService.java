package com.pmu.pmudemo.services;

import com.pmu.pmudemo.domains.RechargeTransaction;
import com.pmu.pmudemo.domains.User;
import com.pmu.pmudemo.domains.dto.*;
import com.pmu.pmudemo.repositories.RechargeTransactionRepository;
import com.pmu.pmudemo.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {
    private final RechargeTransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(RechargeTransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public Page<TransactionListDTO> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable)
                .map(TransactionListDTO::fromEntity);
    }

    public Page<TransactionListDTO> getTransactionsByFilter(TransactionFilterDTO filter, Pageable pageable) {
        Specification<RechargeTransaction> spec = (root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            if (filter.getStatut() != null) {
                predicates.add(cb.equal(root.get("statut"), filter.getStatut()));
            }
            if (filter.getTypeTraitement() != null) {
                predicates.add(cb.equal(root.get("typeTraitement"), 
                    RechargeTransaction.TypeTraitement.valueOf(filter.getTypeTraitement())));
            }
            if (filter.getOperateur() != null) {
                predicates.add(cb.equal(root.get("operateur"), filter.getOperateur()));
            }
            if (filter.getPays() != null) {
                predicates.add(cb.equal(root.get("pays"), filter.getPays()));
            }
            if (filter.getAgentId() != null) {
                predicates.add(cb.equal(root.get("agent").get("id"), filter.getAgentId()));
            }
            if (filter.getDateDebut() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dateDemande"), filter.getDateDebut()));
            }
            if (filter.getDateFin() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dateDemande"), filter.getDateFin()));
            }
            if (filter.getMontantMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("montant"), filter.getMontantMin()));
            }
            if (filter.getMontantMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("montant"), filter.getMontantMax()));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        return transactionRepository.findAll(spec, pageable).map(TransactionListDTO::fromEntity);
    }

    public TransactionDetailsDTO getTransactionDetails(Long id) {
        return transactionRepository.findById(id)
                .map(TransactionDetailsDTO::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Transaction non trouvée"));
    }

    public TransactionDetailsDTO updateTransactionStatus(Long id, UpdateTransactionStatusDTO updateDTO) {
        RechargeTransaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction non trouvée"));

        transaction.setStatut(updateDTO.getStatut());
        transaction.setDateTraitement(LocalDateTime.now());

        return TransactionDetailsDTO.fromEntity(transactionRepository.save(transaction));
    }

    public TransactionDetailsDTO assignTransaction(Long id, AssignTransactionDTO assignDTO) {
        RechargeTransaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction non trouvée"));

        User agent = userRepository.findById(assignDTO.getAgentId())
                .orElseThrow(() -> new IllegalArgumentException("Agent non trouvé"));

        transaction.setAgent(agent);
        transaction.setTypeTraitement(RechargeTransaction.TypeTraitement.MANUELLE);
        transaction.setDateTraitement(LocalDateTime.now());

        return TransactionDetailsDTO.fromEntity(transactionRepository.save(transaction));
    }

    public List<TransactionListDTO> getTransactionsByAgent(Long agentId) {
        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> new IllegalArgumentException("Agent non trouvé"));

        return transactionRepository.findByAgent(agent).stream()
                .map(TransactionListDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<TransactionListDTO> getAutomaticTransactions() {
        return transactionRepository.findByTypeTraitement(RechargeTransaction.TypeTraitement.AUTOMATIQUE).stream()
                .map(TransactionListDTO::fromEntity)
                .collect(Collectors.toList());
    }
} 