package com.vivo.crm.customer.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vivo.crm.customer.dto.Customer360DTO;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity mapped to Oracle Duality View customer_360_dv
 * This view consolidates customer data from multiple tables into a single JSON response
 * Implements CQRS pattern automatically
 */
@Entity
@Table(name = "customer_360_dv")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer360View {
    
    @Id
    @Column(name = "customer_id")
    private Long customerId;
    
    @Column(name = "external_id")
    private String externalId;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "cpf")
    private String cpf;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "segment")
    private String segment;
    
    @Column(name = "preferred_channel")
    private String preferredChannel;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "state")
    private String state;
    
    @Column(name = "zip_code")
    private String zipCode;
    
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;
    
    /**
     * JSON data returned from Duality View
     * Contains all nested data: identification, products, financials, cases, interactions, statistics
     */
    @Column(name = "data", columnDefinition = "CLOB")
    private String data;
    
    /**
     * Transient field to hold parsed DTO
     * Populated by @PostLoad method
     */
    @Transient
    private Customer360DTO parsedData;
    
    /**
     * Parse JSON data from Duality View into DTO
     * Called automatically after entity is loaded from database
     */
    @PostLoad
    public void parseJsonData() {
        if (data != null && !data.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            try {
                this.parsedData = mapper.readValue(data, Customer360DTO.class);
            } catch (JsonProcessingException e) {
                this.parsedData = new Customer360DTO();
            }
        }
    }
    
    /**
     * Get parsed DTO, parsing if necessary
     */
    public Customer360DTO getParsedData() {
        if (parsedData == null) {
            parseJsonData();
        }
        return parsedData;
    }
}
