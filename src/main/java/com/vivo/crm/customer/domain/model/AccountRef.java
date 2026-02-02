package com.vivo.crm.customer.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * TMF629 - AccountRef Entity
 * Reference to an account related to a party role
 */
@Entity
@Table(name = "account_ref")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRef {

    @Id
    @Column(name = "account_ref_id", length = 50)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_role_id", nullable = false)
    private PartyRole partyRole;

    @Column(name = "account_id", length = 50, nullable = false)
    private String accountId;

    @Column(name = "account_name", length = 255)
    private String accountName;

    @Column(name = "account_href", length = 500)
    private String accountHref;

    @Column(name = "account_type", length = 50)
    private String accountType; // BILLING, SETTLEMENT, etc

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
