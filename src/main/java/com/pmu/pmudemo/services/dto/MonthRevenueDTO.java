package com.pmu.pmudemo.services.dto;

import java.math.BigDecimal;

public class MonthRevenueDTO {
    private int year;
    private int month;
    private BigDecimal amount;

    public MonthRevenueDTO(int year, int month, BigDecimal amount) {
        this.year = year;
        this.month = month;
        this.amount = amount;
    }

    public int getYear() { return year; }
    public int getMonth() { return month; }
    public BigDecimal getAmount() { return amount; }

    public void setYear(int year) { this.year = year; }
    public void setMonth(int month) { this.month = month; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
} 