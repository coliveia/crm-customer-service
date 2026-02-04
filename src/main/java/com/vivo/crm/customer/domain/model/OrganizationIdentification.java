package com.vivo.crm.customer.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * TMF632 - OrganizationIdentification Entity
 * Represents identification documents for an organization
 * 
 * Examples: CNPJ, IE (Inscrição Estadual), IM (Inscrição Municipal)
 */
@Entity
@Table(name = "organization_identification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationIdentification {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    /**
     * Type of identification (CNPJ, IE, IM, NIRE)
     */
    @Column(name = "identification_type", length = 50, nullable = false)
    private String identificationType;

    /**
     * Identification number/value
     */
    @Column(name = "identification_id", length = 50, nullable = false)
    private String identificationId;

    /**
     * Authority that issued the identification
     */
    @Column(name = "issuing_authority", length = 100)
    private String issuingAuthority;

    /**
     * Date when identification was issued
     */
    @Column(name = "issuing_date")
    private LocalDate issuingDate;

    /**
     * Start date of validity period
     */
    @Column(name = "valid_from")
    private LocalDate validFrom;

    /**
     * End date of validity period
     */
    @Column(name = "valid_to")
    private LocalDate validTo;

    /**
     * Reference to the organization
     */
    @Column(name = "organization_id", length = 50)
    private String organizationId;

    // ========== TMF metadata ==========

    @Column(name = "at_type", length = 100)
    private String atType;

    @Column(name = "at_base_type", length = 100)
    private String atBaseType;

    @Column(name = "at_schema_location", length = 500)
    private String atSchemaLocation;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
        if (atType == null) {
            atType = "OrganizationIdentification";
        }
    }

    /**
     * Checks if identification is currently valid
     */
    public boolean isValid() {
        LocalDate now = LocalDate.now();
        if (validFrom != null && now.isBefore(validFrom)) {
            return false;
        }
        if (validTo != null && now.isAfter(validTo)) {
            return false;
        }
        return true;
    }
}
