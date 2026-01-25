package com.vivo.crm.customer.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerView360DTO {
    private CustomerDTO customer;
    private List<CustomerCaseDTO> openCases;
    private List<InteractionDTO> recentInteractions;
    private CustomerStatisticsDTO statistics;
    private String riskLevel;
    private String nextRecommendedAction;
}
