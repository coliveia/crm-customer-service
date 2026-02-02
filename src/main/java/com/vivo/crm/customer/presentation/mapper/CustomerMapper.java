package com.vivo.crm.customer.presentation.mapper;

import com.vivo.crm.customer.domain.model.Customer;
import com.vivo.crm.customer.presentation.dto.CreateCustomerRequest;
import com.vivo.crm.customer.presentation.dto.CustomerDTO;
import org.springframework.stereotype.Component;

/**
 * TMF629 - Customer Mapper
 * Converts between Customer entity and DTOs
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
                .href("/customers/" + customer.getCustomerId())
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
