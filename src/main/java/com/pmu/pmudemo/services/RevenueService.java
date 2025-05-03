package com.pmu.pmudemo.services;

import com.pmu.pmudemo.repositories.RechargeTransactionRepository;
import com.pmu.pmudemo.services.dto.MonthRevenueDTO;
import com.pmu.pmudemo.services.dto.OperatorRevenueDTO;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDate;

@Service
public class RevenueService {
    private final RechargeTransactionRepository transactionRepository;
    private static final String SUCCESS_STATUS = "TERMINEE";

    public RevenueService(RechargeTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public BigDecimal getTotalRevenue() {
        return transactionRepository.sumAmountByStatus(SUCCESS_STATUS);
    }

    public List<MonthRevenueDTO> getMonthlyRevenue() {
        return transactionRepository.sumAmountByMonth(SUCCESS_STATUS);
    }

    public List<OperatorRevenueDTO> getRevenueByOperator() {
        return transactionRepository.sumAmountByOperator(SUCCESS_STATUS);
    }

    public long getTransactionCount() {
        return transactionRepository.countByStatus(SUCCESS_STATUS);
    }

    public BigDecimal getAverageBasket() {
        BigDecimal total = getTotalRevenue();
        long count = getTransactionCount();
        return count > 0 ? total.divide(BigDecimal.valueOf(count), 2, java.math.RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }

    public List<MonthRevenueDTO> getMonthlyRevenueFiltered(LocalDate start, LocalDate end, String operator) {
        return transactionRepository.sumAmountByMonthFiltered(SUCCESS_STATUS, start.atStartOfDay(), end.plusDays(1).atStartOfDay(), operator);
    }

    public List<OperatorRevenueDTO> getRevenueByOperatorFiltered(LocalDate start, LocalDate end) {
        return transactionRepository.sumAmountByOperatorFiltered(SUCCESS_STATUS, start.atStartOfDay(), end.plusDays(1).atStartOfDay());
    }
} 