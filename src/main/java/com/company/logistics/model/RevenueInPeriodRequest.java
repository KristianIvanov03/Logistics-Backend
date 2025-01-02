package com.company.logistics.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RevenueInPeriodRequest {
    private LocalDate startDate;
    private LocalDate endDate;
}
