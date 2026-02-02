package com.vivo.crm.customer.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * TMF629 - Customer DTO
 * Data Transfer Object for Customer API responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {

    @JsonProperty("id")
    private String customerId;

    @JsonProperty("partyRoleId")
    private String partyRoleId;

    @JsonProperty("externalId")
    private String externalId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("cpfCnpj")
    private String cpfCnpj;

    @JsonProperty("segment")
    private String segment;

    @JsonProperty("preferredChannel")
    private String preferredChannel;

    @JsonProperty("riskLevel")
    private String riskLevel;

    @JsonProperty("status")
    private String status;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    @JsonProperty("createdBy")
    private String createdBy;

    @JsonProperty("updatedBy")
    private String updatedBy;

    @JsonProperty("contextData")
    private String contextData;

    @JsonProperty("href")
    private String href;
}
