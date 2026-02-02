package com.vivo.crm.customer.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * TMF629 - Customer Entity
 * Represents a customer (inherits from PartyRole)
 * Mapped directly to CUSTOMER table (not using Duality View)
 */
@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @Column(name = "customer_id", length = 50)
    private String customerId;

    @Column(name = "party_role_id", length = 50, nullable = false)
    private String partyRoleId;

    @Column(name = "external_id", length = 100)
    private String externalId;

    @Column(name = "customer_name", length = 255, nullable = false)
    private String name;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "cpf_cnpj", length = 20)
    private String cpfCnpj;

    @Column(name = "segment", length = 100)
    private String segment;

    @Column(name = "preferred_channel", length = 50)
    private String preferredChannel;

    @Column(name = "risk_level", length = 50)
    private String riskLevel; // LOW, MEDIUM, HIGH

    @Column(name = "status", length = 50, nullable = false)
    private String status; // ACTIVE, INACTIVE, PROSPECT, SUSPENDED

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

    @Column(name = "context_data", columnDefinition = "JSON")
    private String contextData;

    @PrePersist
    protected void onCreate() {
        if (customerId == null) {
            customerId = java.util.UUID.randomUUID().toString();
        }
        if (partyRoleId == null) {
            partyRoleId = java.util.UUID.randomUUID().toString();
        }
        if (status == null) {
            status = "ACTIVE";
        }
    }

    /**
     * Ativa o cliente
     */
    public void activate(String actor) {
        this.status = "ACTIVE";
        this.updatedBy = actor;
    }

    /**
     * Desativa o cliente
     */
    public void deactivate(String actor) {
        this.status = "INACTIVE";
        this.updatedBy = actor;
    }

    /**
     * Suspende o cliente
     */
    public void suspend(String reason, String actor) {
        this.status = "SUSPENDED";
        this.riskLevel = "HIGH";
        this.updatedBy = actor;
    }

    /**
     * Verifica se o cliente está ativo
     */
    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }

    /**
     * Verifica se o cliente está suspenso
     */
    public boolean isSuspended() {
        return "SUSPENDED".equals(this.status);
    }
}
