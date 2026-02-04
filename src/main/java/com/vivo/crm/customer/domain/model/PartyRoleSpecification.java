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

/**
 * TMF669 - PartyRoleSpecification Entity
 * Represents a specification for a type of party role
 * 
 * A PartyRoleSpecification provides characteristics to describe a partyRole
 * and the context where the partyRole is created.
 */
@Entity
@Table(name = "party_role_specification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartyRoleSpecification {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    /**
     * Reference to an external entity (URI)
     */
    @Column(name = "href", length = 500)
    private String href;

    /**
     * Name of the party role specification
     */
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    /**
     * Description of the party role specification
     */
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * Status of the specification (created, active, inactive)
     */
    @Column(name = "status", length = 50)
    private String status;

    /**
     * Status reason
     */
    @Column(name = "status_reason", length = 255)
    private String statusReason;

    /**
     * Version of the specification
     */
    @Column(name = "version", length = 50)
    private String version;

    /**
     * Last update date
     */
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    // ========== Validity period ==========

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

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
        if (atType == null) {
            atType = "PartyRoleSpecification";
        }
        if (status == null) {
            status = "created";
        }
        if (version == null) {
            version = "1.0";
        }
        lastUpdate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdate = LocalDateTime.now();
    }

    /**
     * Activates the specification
     */
    public void activate(String actor) {
        this.status = "active";
        this.updatedBy = actor;
    }

    /**
     * Deactivates the specification
     */
    public void deactivate(String reason, String actor) {
        this.status = "inactive";
        this.statusReason = reason;
        this.updatedBy = actor;
    }

    /**
     * Checks if specification is currently valid
     */
    public boolean isValid() {
        LocalDate now = LocalDate.now();
        if (validFrom != null && now.isBefore(validFrom)) {
            return false;
        }
        if (validTo != null && now.isAfter(validTo)) {
            return false;
        }
        return "active".equals(status);
    }
}
