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
 * TMF629 - Party Entity
 * Base class for all parties (Individual, Organization)
 */
@Entity
@Table(name = "party")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "party_type", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Party {

    @Id
    @Column(name = "party_id", length = 50)
    private String id;

    @Column(name = "party_type", length = 50, insertable = false, updatable = false)
    private String partyType;

    @Column(name = "href", length = 500)
    private String href;

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
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
    }
}
