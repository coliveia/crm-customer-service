package com.vivo.crm.customer.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TMF629 - Customer DTO
 * Data Transfer Object for Customer API responses
 * 
 * Campos adicionados conforme planilha CamposParaAdicionar.xlsx:
 * - formattedName: Nome completo do cliente (TMF632 Individual)
 * - givenName: Primeiro nome (TMF632 Individual)
 * - familyName: Sobrenome (TMF632 Individual)
 * - preferredGivenName: Nome de preferência (TMF632 Individual)
 * - tradingName: Razão social para PJ (TMF632 Organization)
 * - identificationType: Tipo de documento (CPF/CNPJ)
 * - identificationNumber: Número do documento
 * - creditScore: Score de crédito (TMF669)
 * - creditRiskRating: Rating de risco
 * - biometriaStatus: Status da biometria
 * - biometriaMessage: Mensagem de biometria para atendente
 * - codigoGrupo: Código do grupo/hierarquia
 * - nomeGrupo: Nome do grupo
 * - customerType: Tipo de cliente (Individual/Organization)
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

    // ========== CAMPOS ADICIONADOS - TMF632 Individual ==========

    /**
     * Nome completo formatado do cliente
     */
    @JsonProperty("formattedName")
    private String formattedName;

    /**
     * Primeiro nome do cliente
     */
    @JsonProperty("givenName")
    private String givenName;

    /**
     * Sobrenome/Nome de família
     */
    @JsonProperty("familyName")
    private String familyName;

    /**
     * Nome de preferência escolhido pelo cliente no APP
     */
    @JsonProperty("preferredGivenName")
    private String preferredGivenName;

    // ========== CAMPOS ADICIONADOS - TMF632 Organization ==========

    /**
     * Razão Social da empresa
     */
    @JsonProperty("tradingName")
    private String tradingName;

    // ========== CAMPOS ADICIONADOS - TMF632 Party Identification ==========

    /**
     * Tipo de identificação: CPF, CNPJ
     */
    @JsonProperty("identificationType")
    private String identificationType;

    /**
     * Número do documento de identificação (sem formatação)
     */
    @JsonProperty("identificationNumber")
    private String identificationNumber;

    /**
     * Tipo de cliente derivado: Individual ou Organization
     */
    @JsonProperty("customerType")
    private String customerType;

    // ========== CAMPOS ADICIONADOS - TMF669 Credit Profile ==========

    /**
     * Score de crédito do cliente
     */
    @JsonProperty("creditScore")
    private Integer creditScore;

    /**
     * Rating de risco de crédito (1-10)
     */
    @JsonProperty("creditRiskRating")
    private Integer creditRiskRating;

    // ========== CAMPOS ADICIONADOS - Características de Negócio ==========

    /**
     * Status da biometria: COLETADA, PENDENTE, NAO_COLETADA
     */
    @JsonProperty("biometriaStatus")
    private String biometriaStatus;

    /**
     * Mensagem de biometria para o atendente
     */
    @JsonProperty("biometriaMessage")
    private String biometriaMessage;

    /**
     * Código do grupo/hierarquia da empresa
     */
    @JsonProperty("codigoGrupo")
    private String codigoGrupo;

    /**
     * Nome do grupo (CORPORATE, GOVERNO)
     */
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

    // ========== CAMPOS TMF ==========

    @JsonProperty("@type")
    private String atType;

    @JsonProperty("@baseType")
    private String atBaseType;

    @JsonProperty("@schemaLocation")
    private String atSchemaLocation;
}
