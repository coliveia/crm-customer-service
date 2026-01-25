package com.vivo.crm.customer.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String externalId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String phone;

    @Column
    private String cpf;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus status;

    @Column
    private String segment;

    @Column
    private String preferredChannel;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String zipCode;

    @Column
    private Double lifetime_value;

    @Column
    private Integer total_purchases;

    @Column
    private LocalDateTime last_interaction;

    @Column
    private LocalDateTime created_at;

    @Column
    private LocalDateTime updated_at;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CustomerCase> cases = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Interaction> interactions = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now();
        updated_at = LocalDateTime.now();
        if (status == null) {
            status = CustomerStatus.ACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updated_at = LocalDateTime.now();
    }

    public enum CustomerStatus {
        ACTIVE, INACTIVE, SUSPENDED, PROSPECT
    }
}
