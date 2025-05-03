package com.pmu.pmudemo.Controller.backoffice;

import com.pmu.pmudemo.repositories.RechargeTransactionRepository;
import com.pmu.pmudemo.repositories.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/backoffice/dashboard")
public class DashboardController {
    private final RechargeTransactionRepository transactionRepo;
    private final UserRepository userRepo;

    public DashboardController(RechargeTransactionRepository transactionRepo, UserRepository userRepo) {
        this.transactionRepo = transactionRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRecharges", transactionRepo.count());
        stats.put("totalUsers", userRepo.count());
        stats.put("totalCA", transactionRepo.findAll().stream().filter(t -> "TERMINEE".equals(t.getStatut()) || "PAYE".equals(t.getStatut())).mapToDouble(t -> t.getMontant() != null ? t.getMontant() : 0).sum());
        stats.put("pendingTransactions", transactionRepo.findAll().stream().filter(t -> "EN_ATTENTE".equals(t.getStatut())).count());
        stats.put("failedTransactions", transactionRepo.findAll().stream().filter(t -> t.getStatut() != null && t.getStatut().startsWith("ECHEC")).count());
        stats.put("refundedTransactions", transactionRepo.findAll().stream().filter(t -> "REMBOURSE".equals(t.getStatut())).count());
        // Statistiques du jour
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        stats.put("rechargesToday", transactionRepo.findAll().stream().filter(t -> t.getDateDemande() != null && t.getDateDemande().isAfter(startOfDay)).count());
        return ResponseEntity.ok(stats);
    }
} 