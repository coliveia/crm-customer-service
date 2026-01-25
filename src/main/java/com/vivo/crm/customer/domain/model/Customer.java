package com.vivo.crm.customer.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Map;

/**
 * Customer Entity (Aggregate Root)
 * Representa um cliente no sistema CRM
 * Consolida dados de identificação, produtos, financeiros, casos e interações
 */
@Entity
@Table(name = "customers")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @Column(name = "customer_id", length = 50)
    private String customerId;

    @Column(name = "external_id", length = 100, unique = true, nullable = false)
    private String externalId;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "email", length = 255, unique = true)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "cpf", length = 14, unique = true)
    private String cpf;

    @Column(name = "status", length = 50)
    private String status; // ACTIVE, INACTIVE, SUSPENDED, PROSPECT

    @Column(name = "segment", length = 100)
    private String segment; // Premium, Standard, Basic

    @Column(name = "preferred_channel", length = 50)
    private String preferredChannel; // CHAT, EMAIL, PHONE, WHATSAPP

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 2)
    private String state;

    @Column(name = "zip_code", length = 10)
    private String zipCode;

    @Column(name = "risk_level", length = 20)
    private String riskLevel; // LOW, MEDIUM, HIGH

    // Context data (JSON column)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "context_data", columnDefinition = "JSON")
    private Map<String, Object> contextData;

    // Audit fields
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @CreatedBy
    @Column(name = "created_by", length = 100, updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    /**
     * Ativa o cliente
     */
    public void activate(String actor) {
        this.status = "ACTIVE";
        this.updatedBy = actor;
        this.updatedAt = Instant.now();
    }

    /**
     * Desativa o cliente
     */
    public void deactivate(String actor) {
        this.status = "INACTIVE";
        this.updatedBy = actor;
        this.updatedAt = Instant.now();
    }

    /**
     * Suspende o cliente
     */
    public void suspend(String reason, String actor) {
        this.status = "SUSPENDED";
        this.riskLevel = "HIGH";
        this.updatedBy = actor;
        this.updatedAt = Instant.now();
        if (this.contextData != null) {
            this.contextData.put("suspensionReason", reason);
        }
    }

    /**
     * Atualiza o segmento do cliente
     */
    public void updateSegment(String newSegment, String actor) {
        this.segment = newSegment;
        this.updatedBy = actor;
        this.updatedAt = Instant.now();
    }

    /**
     * Atualiza o nível de risco
     */
    public void updateRiskLevel(String newRiskLevel, String actor) {
        this.riskLevel = newRiskLevel;
        this.updatedBy = actor;
        this.updatedAt = Instant.now();
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
