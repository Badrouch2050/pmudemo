package com.pmu.pmudemo.Controller.backoffice;

import com.pmu.pmudemo.domains.Dispute;
import com.pmu.pmudemo.services.DisputeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/backoffice/disputes")
public class DisputeController {
    private final DisputeService disputeService;

    public DisputeController(DisputeService disputeService) {
        this.disputeService = disputeService;
    }

    @GetMapping
    public ResponseEntity<List<Dispute>> getAllDisputes() {
        return ResponseEntity.ok(disputeService.getAllDisputes());
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<List<Dispute>> getDisputesByTransaction(@PathVariable Long transactionId) {
        return ResponseEntity.ok(disputeService.getDisputesByTransaction(transactionId));
    }

    @PostMapping
    public ResponseEntity<Dispute> createDispute(@RequestParam Long transactionId, @RequestParam String motif, @RequestParam(required = false) String commentaire) {
        return ResponseEntity.ok(disputeService.createDispute(transactionId, motif, commentaire));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Dispute> updateDisputeStatus(@PathVariable Long id, @RequestParam String statut, @RequestParam(required = false) String commentaire) {
        return ResponseEntity.ok(disputeService.updateDisputeStatus(id, statut, commentaire));
    }
} 