package com.vivo.crm.customer.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerStatisticsDTO {
    private Integer totalCases;
    private Integer openCases;
    private Integer resolvedCases;
    private Double averageSatisfactionScore;
    private Integer totalInteractions;
    private Integer interactionsThisMonth;
    private Double averageResolutionTimeMinutes;
}
