package com.pmu.pmudemo.Controller.backoffice;

import com.pmu.pmudemo.domains.RechargeTransaction;
import com.pmu.pmudemo.services.TransactionAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/backoffice/transactions")
public class TransactionAdminController {
    private final TransactionAdminService transactionAdminService;

    public TransactionAdminController(TransactionAdminService transactionAdminService) {
        this.transactionAdminService = transactionAdminService;
    }

    @GetMapping("/pending")
    public ResponseEntity<Page<RechargeTransaction>> getPendingTransactions(Pageable pageable) {
        return ResponseEntity.ok(transactionAdminService.getPendingTransactions(pageable));
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<Void> validateTransaction(@PathVariable Long id) {
        transactionAdminService.validateTransaction(id);
        return ResponseEntity.ok().build();
    }
} 