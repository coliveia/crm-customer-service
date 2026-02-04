package com.vivo.crm.customer.presentation.mapper;

import com.vivo.crm.customer.domain.model.Customer;
import com.vivo.crm.customer.presentation.dto.CreateCustomerRequest;
import com.vivo.crm.customer.presentation.dto.CustomerDTO;
import org.springframework.stereotype.Component;

/**
 * TMF629 - Customer Mapper
 * Converts between Customer entity and DTOs
 * 
 * Atualizado para incluir novos campos TMF629/TMF632:
 * - formattedName, givenName, familyName, preferredGivenName
 * - tradingName
 * - identificationType, identificationNumber
 * - creditScore, creditRiskRating
 * - biometriaStatus, biometriaMessage
 * - codigoGrupo, nomeGrupo
 */
@Component
public class CustomerMapper {

    /**
     * Convert Customer entity to DTO
     */
    public CustomerDTO toDTO(Customer customer) {
        if (customer == null) {
            return null;
        }

        return CustomerDTO.builder()
                .customerId(customer.getCustomerId())
                .partyRoleId(customer.getPartyRoleId())
                .externalId(customer.getExternalId())
                .name(customer.getName())
                // Novos campos TMF632 Individual
                .formattedName(customer.getFormattedName())
                .givenName(customer.getGivenName())
                .familyName(customer.getFamilyName())
                .preferredGivenName(customer.getPreferredGivenName())
                // Novos campos TMF632 Organization
                .tradingName(customer.getTradingName())
                // Novos campos TMF632 Party Identification
                .identificationType(customer.getIdentificationType())
                .identificationNumber(customer.getIdentificationNumber())
                .customerType(customer.getCustomerType())
                // Novos campos TMF669 Credit Profile
                .creditScore(customer.getCreditScore())
                .creditRiskRating(customer.getCreditRiskRating())
                // Novos campos de negócio
                .biometriaStatus(customer.getBiometriaStatus())
                .biometriaMessage(customer.getBiometriaMessage())
                .codigoGrupo(customer.getCodigoGrupo())
                .nomeGrupo(customer.getNomeGrupo())
                // Campos existentes
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .cpfCnpj(customer.getCpfCnpj())
                .segment(customer.getSegment())
                .preferredChannel(customer.getPreferredChannel())
                .riskLevel(customer.getRiskLevel())
                .status(customer.getStatus())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .createdBy(customer.getCreatedBy())
                .updatedBy(customer.getUpdatedBy())
                .contextData(customer.getContextData())
                .href("/api/v1/customers/" + customer.getCustomerId())
                // Campos TMF
                .atType("Customer")
                .atBaseType("PartyRole")
                .build();
    }

    /**
     * Convert CreateCustomerRequest to Customer entity
     */
    public Customer toEntity(CreateCustomerRequest request) {
        if (request == null) {
            return null;
        }

        return Customer.builder()
                .externalId(request.getExternalId())
                .name(request.getName())
                // Novos campos TMF632 Individual
                .formattedName(request.getFormattedName())
                .givenName(request.getGivenName())
                .familyName(request.getFamilyName())
                .preferredGivenName(request.getPreferredGivenName())
                // Novos campos TMF632 Organization
                .tradingName(request.getTradingName())
                // Novos campos TMF632 Party Identification
                .identificationType(request.getIdentificationType())
                .identificationNumber(request.getIdentificationNumber())
                // Novos campos TMF669 Credit Profile
                .creditScore(request.getCreditScore())
                .creditRiskRating(request.getCreditRiskRating())
                // Novos campos de negócio
                .biometriaStatus(request.getBiometriaStatus())
                .codigoGrupo(request.getCodigoGrupo())
                .nomeGrupo(request.getNomeGrupo())
                // Campos existentes
                .email(request.getEmail())
                .phone(request.getPhone())
                .cpfCnpj(request.getCpfCnpj())
                .segment(request.getSegment())
                .preferredChannel(request.getPreferredChannel())
                .riskLevel(request.getRiskLevel())
                .status(request.getStatus() != null ? request.getStatus() : "ACTIVE")
                .contextData(request.getContextData())
                .build();
    }

    /**
     * Update Customer entity from DTO
     */
    public void updateEntityFromDTO(CustomerDTO dto, Customer customer) {
        if (dto == null || customer == null) {
            return;
        }

        // Novos campos TMF632 Individual
        if (dto.getFormattedName() != null) {
            customer.setFormattedName(dto.getFormattedName());
        }
        if (dto.getGivenName() != null) {
            customer.setGivenName(dto.getGivenName());
        }
        if (dto.getFamilyName() != null) {
            customer.setFamilyName(dto.getFamilyName());
        }
        if (dto.getPreferredGivenName() != null) {
            customer.setPreferredGivenName(dto.getPreferredGivenName());
        }
        // Novos campos TMF632 Organization
        if (dto.getTradingName() != null) {
            customer.setTradingName(dto.getTradingName());
        }
        // Novos campos TMF632 Party Identification
        if (dto.getIdentificationType() != null) {
            customer.setIdentificationType(dto.getIdentificationType());
        }
        if (dto.getIdentificationNumber() != null) {
            customer.setIdentificationNumber(dto.getIdentificationNumber());
        }
        // Novos campos TMF669 Credit Profile
        if (dto.getCreditScore() != null) {
            customer.setCreditScore(dto.getCreditScore());
        }
        if (dto.getCreditRiskRating() != null) {
            customer.setCreditRiskRating(dto.getCreditRiskRating());
        }
        // Novos campos de negócio
        if (dto.getBiometriaStatus() != null) {
            customer.setBiometriaStatus(dto.getBiometriaStatus());
        }
        if (dto.getCodigoGrupo() != null) {
            customer.setCodigoGrupo(dto.getCodigoGrupo());
        }
        if (dto.getNomeGrupo() != null) {
            customer.setNomeGrupo(dto.getNomeGrupo());
        }
        // Campos existentes
        if (dto.getEmail() != null) {
            customer.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null) {
            customer.setPhone(dto.getPhone());
        }
        if (dto.getSegment() != null) {
            customer.setSegment(dto.getSegment());
        }
        if (dto.getPreferredChannel() != null) {
            customer.setPreferredChannel(dto.getPreferredChannel());
        }
        if (dto.getRiskLevel() != null) {
            customer.setRiskLevel(dto.getRiskLevel());
        }
        if (dto.getStatus() != null) {
            customer.setStatus(dto.getStatus());
        }
        if (dto.getContextData() != null) {
            customer.setContextData(dto.getContextData());
        }
    }
}
