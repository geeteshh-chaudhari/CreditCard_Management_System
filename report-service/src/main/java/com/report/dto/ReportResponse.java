package com.report.dto;

import java.math.BigDecimal;

public class ReportResponse {

    private Long id;
    private String name;
    private String type;
    private BigDecimal amount;
    private Long count;

    public ReportResponse() {
    }

    public ReportResponse(
            Long id,
            String name,
            String type,
            BigDecimal amount,
            Long count) {

        this.id = id;
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}