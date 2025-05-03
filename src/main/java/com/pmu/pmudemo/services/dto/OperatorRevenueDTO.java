package com.pmu.pmudemo.services.dto;

import java.math.BigDecimal;

public class OperatorRevenueDTO {
    private String operator;
    private BigDecimal amount;

    public OperatorRevenueDTO(String operator, BigDecimal amount) {
        this.operator = operator;
        this.amount = amount;
    }

    public String getOperator() { return operator; }
    public BigDecimal getAmount() { return amount; }

    public void setOperator(String operator) { this.operator = operator; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
} 