package com.vivo.crm.customer.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "interactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String interactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id")
    private CustomerCase customerCase;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InteractionChannel channel;

    @Column(nullable = false)
    private String agentId;

    @Column(nullable = false)
    private String agentName;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InteractionDirection direction;

    @Column
    private Integer duration_seconds;

    @Column
    private LocalDateTime created_at;

    @Column
    private LocalDateTime updated_at;

    @Column
    private String sentiment;

    @Column
    private Double sentiment_score;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now();
        updated_at = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updated_at = LocalDateTime.now();
    }

    public enum InteractionChannel {
        CHAT, EMAIL, PHONE, SOCIAL_MEDIA, WHATSAPP, SMS
    }

    public enum InteractionDirection {
        INBOUND, OUTBOUND
    }
}
