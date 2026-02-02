package com.vivo.crm.customer.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TMF629 - PartyRole Entity
 * Represents a role that a party can play
 */
@Entity
@Table(name = "party_role")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "party_role_type", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartyRole {

    @Id
    @Column(name = "party_role_id", length = 50)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    @Column(name = "party_role_type", length = 50, insertable = false, updatable = false)
    private String partyRoleType;

    @Column(name = "role_name", length = 255, nullable = false)
    private String roleName;

    @Column(name = "role_description", length = 1000)
    private String roleDescription;

    @Column(name = "status", length = 50, nullable = false)
    private String status; // ACTIVE, INACTIVE, SUSPENDED

    @Column(name = "status_reason", length = 500)
    private String statusReason;

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "valid_to")
    private LocalDateTime validTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engaged_party_id")
    private Party engagedParty;

    @Column(name = "party_role_specification_id", length = 50)
    private String partyRoleSpecificationId;

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

    @OneToMany(mappedBy = "partyRole", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ContactMedium> contactMedia = new ArrayList<>();

    @OneToMany(mappedBy = "partyRole", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Characteristic> characteristics = new ArrayList<>();

    @OneToMany(mappedBy = "partyRole", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CreditProfile> creditProfiles = new ArrayList<>();

    @OneToMany(mappedBy = "partyRole", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AccountRef> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "partyRole", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RelatedParty> relatedParties = new ArrayList<>();

    @OneToMany(mappedBy = "partyRole", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PaymentMethod> paymentMethods = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
    }
}
