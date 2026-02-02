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
 * TMF629 - ContactMedium Entity (Polimórfica)
 * Represents a contact medium for a party role
 */
@Entity
@Table(name = "contact_medium")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "contact_medium_type", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactMedium {

    @Id
    @Column(name = "contact_medium_id", length = 50)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_role_id", nullable = false)
    private PartyRole partyRole;

    @Column(name = "contact_type", length = 50)
    private String contactType;

    @Column(name = "contact_medium_type", length = 50, insertable = false, updatable = false)
    private String contactMediumType;

    @Column(name = "preferred")
    private Boolean preferred = false;

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "valid_to")
    private LocalDateTime validTo;

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
