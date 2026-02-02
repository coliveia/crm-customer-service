package com.vivo.crm.customer.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * TMF629 - PaymentMethod Entity
 * Represents a payment method for a party role
 */
@Entity
@Table(name = "payment_method")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethod {

    @Id
    @Column(name = "payment_method_id", length = 50)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_role_id", nullable = false)
    private PartyRole partyRole;

    @Column(name = "payment_type", length = 50)
    private String paymentType; // CREDIT_CARD, BANK_TRANSFER, etc

    @Column(name = "payment_method_details", columnDefinition = "JSON")
    private String paymentMethodDetails;

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
