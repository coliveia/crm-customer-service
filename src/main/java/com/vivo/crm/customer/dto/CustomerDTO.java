package com.vivo.crm.customer.dto;

import com.vivo.crm.customer.entity.Customer;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {
    private Long id;
    private String externalId;
    private String name;
    private String email;
    private String phone;
    private String cpf;
    private String status;
    private String segment;
    private String preferredChannel;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private Double lifetimeValue;
    private Integer totalPurchases;
    private LocalDateTime lastInteraction;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CustomerCaseDTO> cases;
    private List<InteractionDTO> interactions;

    public static CustomerDTO fromEntity(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .externalId(customer.getExternalId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .cpf(customer.getCpf())
                .status(customer.getStatus().toString())
                .segment(customer.getSegment())
                .preferredChannel(customer.getPreferredChannel())
                .address(customer.getAddress())
                .city(customer.getCity())
                .state(customer.getState())
                .zipCode(customer.getZipCode())
                .lifetimeValue(customer.getLifetime_value())
                .totalPurchases(customer.getTotal_purchases())
                .lastInteraction(customer.getLast_interaction())
                .createdAt(customer.getCreated_at())
                .updatedAt(customer.getUpdated_at())
                .build();
    }

    public Customer toEntity() {
        return Customer.builder()
                .externalId(this.externalId)
                .name(this.name)
                .email(this.email)
                .phone(this.phone)
                .cpf(this.cpf)
                .status(Customer.CustomerStatus.valueOf(this.status))
                .segment(this.segment)
                .preferredChannel(this.preferredChannel)
                .address(this.address)
                .city(this.city)
                .state(this.state)
                .zipCode(this.zipCode)
                .lifetime_value(this.lifetimeValue)
                .total_purchases(this.totalPurchases)
                .build();
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class CustomerCreateRequest {
    private String externalId;
    private String name;
    private String email;
    private String phone;
    private String cpf;
    private String segment;
    private String preferredChannel;
    private String address;
    private String city;
    private String state;
    private String zipCode;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class CustomerUpdateRequest {
    private String name;
    private String phone;
    private String segment;
    private String preferredChannel;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String status;
}
