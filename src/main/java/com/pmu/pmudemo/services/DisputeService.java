package com.pmu.pmudemo.services;

import com.pmu.pmudemo.domains.Dispute;
import com.pmu.pmudemo.domains.RechargeTransaction;
import com.pmu.pmudemo.repositories.DisputeRepository;
import com.pmu.pmudemo.repositories.RechargeTransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DisputeService {
    private final DisputeRepository disputeRepo;
    private final RechargeTransactionRepository transactionRepo;

    public DisputeService(DisputeRepository disputeRepo, RechargeTransactionRepository transactionRepo) {
        this.disputeRepo = disputeRepo;
        this.transactionRepo = transactionRepo;
    }

    public Dispute createDispute(Long transactionId, String motif, String commentaire) {
        RechargeTransaction transaction = transactionRepo.findById(transactionId).orElseThrow();
        Dispute dispute = new Dispute();
        dispute.setTransaction(transaction);
        dispute.setMotif(motif);
        dispute.setCommentaire(commentaire);
        dispute.setStatut("OUVERT");
        dispute.setDateCreation(LocalDateTime.now());
        return disputeRepo.save(dispute);
    }

    public List<Dispute> getAllDisputes() {
        return disputeRepo.findAll();
    }

    public List<Dispute> getDisputesByTransaction(Long transactionId) {
        return disputeRepo.findByTransactionId(transactionId);
    }

    public Optional<Dispute> getDispute(Long id) {
        return disputeRepo.findById(id);
    }

    public Dispute updateDisputeStatus(Long id, String statut, String commentaire) {
        Dispute dispute = disputeRepo.findById(id).orElseThrow();
        dispute.setStatut(statut);
        dispute.setCommentaire(commentaire);
        if ("RESOLU".equals(statut) || "REMBOURSE".equals(statut) || "REJETE".equals(statut)) {
            dispute.setDateResolution(LocalDateTime.now());
        }
        return disputeRepo.save(dispute);
    }
} 