package com.vivo.crm.customer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for Customer 360 Duality View
 * Represents consolidated customer data from Oracle Duality View
 * Includes: identification, products, financials, cases, interactions, statistics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer360DTO {
    
    @JsonProperty("customerId")
    private Long customerId;
    
    @JsonProperty("externalId")
    private String externalId;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("phone")
    private String phone;
    
    @JsonProperty("cpf")
    private String cpf;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("segment")
    private String segment;
    
    @JsonProperty("preferredChannel")
    private String preferredChannel;
    
    @JsonProperty("address")
    private String address;
    
    @JsonProperty("city")
    private String city;
    
    @JsonProperty("state")
    private String state;
    
    @JsonProperty("zipCode")
    private String zipCode;
    
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
    
    // Nested data from Duality View
    
    @JsonProperty("identification")
    private IdentificationDTO identification;
    
    @JsonProperty("products")
    private List<ProductDTO> products;
    
    @JsonProperty("financials")
    private FinancialsDTO financials;
    
    @JsonProperty("cases")
    private List<CaseDTO> cases;
    
    @JsonProperty("interactions")
    private List<InteractionDTO> interactions;
    
    @JsonProperty("statistics")
    private StatisticsDTO statistics;
}

/**
 * Customer identification data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class IdentificationDTO {
    @JsonProperty("customerId")
    private Long customerId;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("phone")
    private String phone;
    
    @JsonProperty("cpf")
    private String cpf;
    
    @JsonProperty("segment")
    private String segment;
    
    @JsonProperty("status")
    private String status;
}

/**
 * Customer contracted products
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class ProductDTO {
    @JsonProperty("productId")
    private Long productId;
    
    @JsonProperty("productName")
    private String productName;
    
    @JsonProperty("productCode")
    private String productCode;
    
    @JsonProperty("contractDate")
    private LocalDate contractDate;
    
    @JsonProperty("expiryDate")
    private LocalDate expiryDate;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("monthlyValue")
    private BigDecimal monthlyValue;
}

/**
 * Customer financial data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class FinancialsDTO {
    @JsonProperty("lifetimeValue")
    private BigDecimal lifetimeValue;
    
    @JsonProperty("totalPurchases")
    private Integer totalPurchases;
    
    @JsonProperty("totalRevenue")
    private BigDecimal totalRevenue;
    
    @JsonProperty("totalPaid")
    private BigDecimal totalPaid;
    
    @JsonProperty("totalPending")
    private BigDecimal totalPending;
    
    @JsonProperty("averageTicketValue")
    private BigDecimal averageTicketValue;
    
    @JsonProperty("lastPaymentDate")
    private LocalDate lastPaymentDate;
    
    @JsonProperty("paymentStatus")
    private String paymentStatus;
    
    @JsonProperty("creditLimit")
    private BigDecimal creditLimit;
    
    @JsonProperty("currentBalance")
    private BigDecimal currentBalance;
}

/**
 * Customer support case/ticket
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class CaseDTO {
    @JsonProperty("caseId")
    private Long caseId;
    
    @JsonProperty("caseNumber")
    private String caseNumber;
    
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("priority")
    private String priority;
    
    @JsonProperty("category")
    private String category;
    
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("resolvedAt")
    private LocalDateTime resolvedAt;
    
    @JsonProperty("satisfactionScore")
    private Double satisfactionScore;
}

/**
 * Customer interaction (chat, email, phone, etc)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class InteractionDTO {
    @JsonProperty("interactionId")
    private Long interactionId;
    
    @JsonProperty("channel")
    private String channel;
    
    @JsonProperty("agentName")
    private String agentName;
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("sentiment")
    private String sentiment;
    
    @JsonProperty("sentimentScore")
    private Double sentimentScore;
    
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
}

/**
 * Customer statistics and metrics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class StatisticsDTO {
    @JsonProperty("totalCases")
    private Integer totalCases;
    
    @JsonProperty("openCases")
    private Integer openCases;
    
    @JsonProperty("resolvedCases")
    private Integer resolvedCases;
    
    @JsonProperty("averageSatisfactionScore")
    private Double averageSatisfactionScore;
    
    @JsonProperty("totalInteractions")
    private Integer totalInteractions;
    
    @JsonProperty("averageResolutionTimeMinutes")
    private Double averageResolutionTimeMinutes;
}
