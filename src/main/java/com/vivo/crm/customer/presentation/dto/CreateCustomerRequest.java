package com.vivo.crm.customer.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TMF629 - Create Customer Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCustomerRequest {

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

    @JsonProperty("contextData")
    private String contextData;
}
