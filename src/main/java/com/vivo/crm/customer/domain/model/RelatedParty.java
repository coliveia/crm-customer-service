package com.vivo.crm.customer.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * TMF629 - RelatedParty Entity
 * Represents a relationship between party roles
 */
@Entity
@Table(name = "related_party")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelatedParty {

    @Id
    @Column(name = "related_party_id", length = 50)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_role_id", nullable = false)
    private PartyRole partyRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_party_role_id")
    private PartyRole relatedPartyRole;

    @Column(name = "relationship_type", length = 100)
    private String relationshipType; // PARENT, CHILD, SPOUSE, etc

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "valid_to")
    private LocalDateTime validTo;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
    }
}
