package com.vivo.crm.customer.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_cases")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String caseNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CaseStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CasePriority priority;

    @Column
    private String category;

    @Column
    private String subcategory;

    @Column
    private String assignedTo;

    @Column
    private LocalDateTime created_at;

    @Column
    private LocalDateTime updated_at;

    @Column
    private LocalDateTime resolved_at;

    @Column
    private Integer resolution_time_minutes;

    @Column(columnDefinition = "TEXT")
    private String resolution_notes;

    @Column
    private Double satisfaction_score;

    @OneToMany(mappedBy = "customerCase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Interaction> interactions = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now();
        updated_at = LocalDateTime.now();
        if (status == null) {
            status = CaseStatus.OPEN;
        }
        if (priority == null) {
            priority = CasePriority.MEDIUM;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updated_at = LocalDateTime.now();
    }

    public enum CaseStatus {
        OPEN, IN_PROGRESS, WAITING_CUSTOMER, RESOLVED, CLOSED, ESCALATED
    }

    public enum CasePriority {
        LOW, MEDIUM, HIGH, CRITICAL
    }
}
