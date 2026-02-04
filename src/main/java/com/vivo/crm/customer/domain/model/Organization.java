package com.vivo.crm.customer.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TMF632 - Organization Entity
 * Represents a legal entity (pessoa jurídica)
 * 
 * Organization represents a group of people identified by shared interests or purpose.
 * Examples include business, department and enterprise.
 */
@Entity
@Table(name = "organization")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    /**
     * Reference to an external entity (URI)
     */
    @Column(name = "href", length = 500)
    private String href;

    /**
     * Organization name (department name for example)
     */
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    /**
     * Name that the organization trades under
     */
    @Column(name = "trading_name", length = 255)
    private String tradingName;

    /**
     * Type of the name (Co, Inc, Ltd, etc.)
     */
    @Column(name = "name_type", length = 50)
    private String nameType;

    /**
     * Type of Organization (company, department, etc.)
     */
    @Column(name = "organization_type", length = 100)
    private String organizationType;

    /**
     * If true, the organization is a legal entity known by a national referential
     */
    @Column(name = "is_legal_entity")
    private Boolean isLegalEntity;

    /**
     * If true, the organization is the head office
     */
    @Column(name = "is_head_office")
    private Boolean isHeadOffice;

    /**
     * Status of the organization (initialized, validated, closed)
     */
    @Column(name = "status", length = 50)
    private String status;

    // ========== Existence period ==========

    /**
     * Start date of organization existence
     */
    @Column(name = "exists_during_start")
    private LocalDate existsDuringStart;

    /**
     * End date of organization existence
     */
    @Column(name = "exists_during_end")
    private LocalDate existsDuringEnd;

    // ========== Identification fields ==========

    /**
     * Primary identification type (CNPJ, IE, IM)
     */
    @Column(name = "identification_type", length = 50)
    private String identificationType;

    /**
     * Primary identification number
     */
    @Column(name = "identification_number", length = 50)
    private String identificationNumber;

    /**
     * Issuing authority for identification
     */
    @Column(name = "issuing_authority", length = 100)
    private String issuingAuthority;

    /**
     * Date of identification issuance
     */
    @Column(name = "issuing_date")
    private LocalDate issuingDate;

    // ========== Contact fields ==========

    /**
     * Primary email address
     */
    @Column(name = "email", length = 255)
    private String email;

    /**
     * Primary phone number
     */
    @Column(name = "phone", length = 50)
    private String phone;

    // ========== Parent organization ==========

    /**
     * Parent organization ID (for hierarchy)
     */
    @Column(name = "parent_organization_id", length = 50)
    private String parentOrganizationId;

    /**
     * Type of relationship with parent
     */
    @Column(name = "parent_relationship_type", length = 100)
    private String parentRelationshipType;

    // ========== TMF metadata ==========

    @Column(name = "at_type", length = 100)
    private String atType;

    @Column(name = "at_base_type", length = 100)
    private String atBaseType;

    @Column(name = "at_schema_location", length = 500)
    private String atSchemaLocation;

    // ========== Audit fields ==========

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    // ========== Relationships ==========

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "organization_id")
    @Builder.Default
    private List<ContactMedium> contactMedium = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "organization_id")
    @Builder.Default
    private List<OrganizationIdentification> organizationIdentification = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
        if (atType == null) {
            atType = "Organization";
        }
        if (atBaseType == null) {
            atBaseType = "Party";
        }
        if (status == null) {
            status = "initialized";
        }
        if (isLegalEntity == null) {
            isLegalEntity = true;
        }
    }

    /**
     * Validates the organization
     */
    public void validate(String actor) {
        this.status = "validated";
        this.updatedBy = actor;
    }

    /**
     * Closes the organization
     */
    public void close(LocalDate closeDate, String actor) {
        this.status = "closed";
        this.existsDuringEnd = closeDate;
        this.updatedBy = actor;
    }

    /**
     * Checks if organization is active
     */
    public boolean isActive() {
        return !"closed".equals(status);
    }
}
