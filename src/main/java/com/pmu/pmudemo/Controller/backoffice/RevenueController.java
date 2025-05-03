package com.pmu.pmudemo.Controller.backoffice;

import com.pmu.pmudemo.services.RevenueService;
import com.pmu.pmudemo.services.dto.MonthRevenueDTO;
import com.pmu.pmudemo.services.dto.OperatorRevenueDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/backoffice/revenue")
public class RevenueController {
    private final RevenueService revenueService;

    public RevenueController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getTotalRevenue() {
        return ResponseEntity.ok(revenueService.getTotalRevenue());
    }

    @GetMapping("/by-month")
    public ResponseEntity<List<MonthRevenueDTO>> getMonthlyRevenue() {
        return ResponseEntity.ok(revenueService.getMonthlyRevenue());
    }

    @GetMapping("/by-operator")
    public ResponseEntity<List<OperatorRevenueDTO>> getRevenueByOperator() {
        return ResponseEntity.ok(revenueService.getRevenueByOperator());
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = Map.of(
            "totalRevenue", revenueService.getTotalRevenue(),
            "transactionCount", revenueService.getTransactionCount(),
            "averageBasket", revenueService.getAverageBasket()
        );
        return ResponseEntity.ok(stats);
    }
} 