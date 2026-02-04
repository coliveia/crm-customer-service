package com.vivo.crm.customer.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * TMF - Hub Entity
 * Represents an event subscription (webhook registration)
 * 
 * Used for registering callbacks to receive notifications about
 * resource lifecycle events (create, update, delete, state change).
 */
@Entity
@Table(name = "hub")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hub {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    /**
     * The callback URL where events will be sent
     */
    @Column(name = "callback", length = 500, nullable = false)
    private String callback;

    /**
     * Query string to filter events (e.g., "eventType=CustomerCreateEvent")
     */
    @Column(name = "query", length = 1000)
    private String query;

    /**
     * Status of the subscription (active, inactive)
     */
    @Column(name = "status", length = 50)
    private String status;

    // ========== Audit fields ==========

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
        if (status == null) {
            status = "active";
        }
    }

    /**
     * Checks if subscription is active
     */
    public boolean isActive() {
        return "active".equals(status);
    }

    /**
     * Deactivates the subscription
     */
    public void deactivate() {
        this.status = "inactive";
    }
}
