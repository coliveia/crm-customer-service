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
 * TMF629 - CreditProfile Entity
 * Represents credit information for a party role
 */
@Entity
@Table(name = "credit_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditProfile {

    @Id
    @Column(name = "credit_profile_id", length = 50)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_role_id", nullable = false)
    private PartyRole partyRole;

    @Column(name = "credit_profile_date")
    private LocalDateTime creditProfileDate;

    @Column(name = "credit_risk_rating")
    private Integer creditRiskRating; // 1-10

    @Column(name = "credit_score")
    private Integer creditScore; // 0-1000

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "valid_to")
    private LocalDateTime validTo;

    @Column(name = "href", length = 500)
    private String href;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
        if (creditProfileDate == null) {
            creditProfileDate = LocalDateTime.now();
        }
    }
}
