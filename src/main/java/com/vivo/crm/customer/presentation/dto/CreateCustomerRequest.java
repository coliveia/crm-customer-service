package com.vivo.crm.customer.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TMF629 - Create Customer Request DTO
 * 
 * Campos adicionados conforme planilha CamposParaAdicionar.xlsx:
 * - formattedName, givenName, familyName, preferredGivenName (TMF632 Individual)
 * - tradingName (TMF632 Organization)
 * - identificationType, identificationNumber (TMF632 Party Identification)
 * - creditScore, creditRiskRating (TMF669)
 * - biometriaStatus, codigoGrupo, nomeGrupo (Campos de negócio)
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

    // ========== CAMPOS ADICIONADOS - TMF632 Individual ==========

    @JsonProperty("formattedName")
    private String formattedName;

    @JsonProperty("givenName")
    private String givenName;

    @JsonProperty("familyName")
    private String familyName;

    @JsonProperty("preferredGivenName")
    private String preferredGivenName;

    // ========== CAMPOS ADICIONADOS - TMF632 Organization ==========

    @JsonProperty("tradingName")
    private String tradingName;

    // ========== CAMPOS ADICIONADOS - TMF632 Party Identification ==========

    @JsonProperty("identificationType")
    private String identificationType;

    @JsonProperty("identificationNumber")
    private String identificationNumber;

    // ========== CAMPOS ADICIONADOS - TMF669 Credit Profile ==========

    @JsonProperty("creditScore")
    private Integer creditScore;

    @JsonProperty("creditRiskRating")
    private Integer creditRiskRating;

    // ========== CAMPOS ADICIONADOS - Características de Negócio ==========

    @JsonProperty("biometriaStatus")
    private String biometriaStatus;

    @JsonProperty("codigoGrupo")
    private String codigoGrupo;

    @JsonProperty("nomeGrupo")
    private String nomeGrupo;

    // ========== CAMPOS EXISTENTES ==========

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    /**
     * @deprecated Use identificationType e identificationNumber
     */
    @Deprecated
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
