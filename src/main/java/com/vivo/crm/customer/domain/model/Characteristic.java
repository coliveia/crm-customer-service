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
 * TMF629 - Characteristic Entity
 * Represents a dynamic characteristic of a party role
 */
@Entity
@Table(name = "characteristic")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Characteristic {

    @Id
    @Column(name = "characteristic_id", length = 50)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_role_id", nullable = false)
    private PartyRole partyRole;

    @Column(name = "characteristic_name", length = 255, nullable = false)
    private String name;

    @Column(name = "characteristic_value", length = 1000)
    private String value;

    @Column(name = "value_type", length = 50)
    private String valueType; // STRING, INTEGER, BOOLEAN, DATE, etc

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "context_data", columnDefinition = "JSON")
    private String contextData;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
    }
}
