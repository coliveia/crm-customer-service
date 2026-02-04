package com.vivo.crm.customer.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TMF632 - Individual Entity
 * Represents a natural person (pessoa física)
 * 
 * An Individual represents a single human being (a man, woman or child).
 * The individual can be a customer, an employee or any other person that
 * the organization needs to store information about.
 */
@Entity
@Table(name = "individual")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Individual {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    /**
     * Reference to an external entity (URI)
     */
    @Column(name = "href", length = 500)
    private String href;

    /**
     * Title of the individual (Mr., Mrs., Dr., etc.)
     */
    @Column(name = "title", length = 50)
    private String title;

    /**
     * First name or given name
     */
    @Column(name = "given_name", length = 100)
    private String givenName;

    /**
     * Last name or family name
     */
    @Column(name = "family_name", length = 100)
    private String familyName;

    /**
     * Middle name
     */
    @Column(name = "middle_name", length = 100)
    private String middleName;

    /**
     * Full name formatted for display
     */
    @Column(name = "formatted_name", length = 255)
    private String formattedName;

    /**
     * Preferred name to be used
     */
    @Column(name = "preferred_given_name", length = 100)
    private String preferredGivenName;

    /**
     * Full legal name
     */
    @Column(name = "full_name", length = 255)
    private String fullName;

    /**
     * Gender (male, female, other)
     */
    @Column(name = "gender", length = 20)
    private String gender;

    /**
     * Date of birth
     */
    @Column(name = "birth_date")
    private LocalDate birthDate;

    /**
     * Place of birth
     */
    @Column(name = "place_of_birth", length = 255)
    private String placeOfBirth;

    /**
     * Country of birth
     */
    @Column(name = "country_of_birth", length = 100)
    private String countryOfBirth;

    /**
     * Nationality
     */
    @Column(name = "nationality", length = 100)
    private String nationality;

    /**
     * Marital status (single, married, divorced, widowed)
     */
    @Column(name = "marital_status", length = 50)
    private String maritalStatus;

    /**
     * Preferred language for communication
     */
    @Column(name = "preferred_language", length = 20)
    private String preferredLanguage;

    /**
     * Status of the individual (initialized, validated, decepiased)
     */
    @Column(name = "status", length = 50)
    private String status;

    /**
     * Aristocratic title or rank
     */
    @Column(name = "aristocratic_title", length = 100)
    private String aristocraticTitle;

    /**
     * Generation suffix (Jr., Sr., III, etc.)
     */
    @Column(name = "generation", length = 20)
    private String generation;

    /**
     * Legal name (official name on legal documents)
     */
    @Column(name = "legal_name", length = 255)
    private String legalName;

    /**
     * Location (current location)
     */
    @Column(name = "location", length = 255)
    private String location;

    /**
     * Death date (if applicable)
     */
    @Column(name = "death_date")
    private LocalDate deathDate;

    // ========== Identification fields ==========

    /**
     * Primary identification type (CPF, RG, PASSPORT)
     */
    @Column(name = "identification_type", length = 50)
    private String identificationType;

    /**
     * Primary identification number
     */
    @Column(name = "identification_number", length = 50)
    private String identificationNumber;

    /**
     * Issuing authority for identification
     */
    @Column(name = "issuing_authority", length = 100)
    private String issuingAuthority;

    /**
     * Date of identification issuance
     */
    @Column(name = "issuing_date")
    private LocalDate issuingDate;

    // ========== Contact fields ==========

    /**
     * Primary email address
     */
    @Column(name = "email", length = 255)
    private String email;

    /**
     * Primary phone number
     */
    @Column(name = "phone", length = 50)
    private String phone;

    /**
     * Mobile phone number
     */
    @Column(name = "mobile_phone", length = 50)
    private String mobilePhone;

    // ========== TMF metadata ==========

    @Column(name = "at_type", length = 100)
    private String atType;

    @Column(name = "at_base_type", length = 100)
    private String atBaseType;

    @Column(name = "at_schema_location", length = 500)
    private String atSchemaLocation;

    // ========== Audit fields ==========

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

    // ========== Relationships ==========

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "individual_id")
    @Builder.Default
    private List<ContactMedium> contactMedium = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "individual_id")
    @Builder.Default
    private List<IndividualIdentification> individualIdentification = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
        if (atType == null) {
            atType = "Individual";
        }
        if (atBaseType == null) {
            atBaseType = "Party";
        }
        if (status == null) {
            status = "initialized";
        }
        // Build formatted name if not set
        if (formattedName == null && givenName != null) {
            StringBuilder sb = new StringBuilder();
            if (title != null) sb.append(title).append(" ");
            sb.append(givenName);
            if (middleName != null) sb.append(" ").append(middleName);
            if (familyName != null) sb.append(" ").append(familyName);
            formattedName = sb.toString().trim();
        }
    }

    /**
     * Returns display name (preferred or given name)
     */
    public String getDisplayName() {
        if (preferredGivenName != null && !preferredGivenName.isEmpty()) {
            return preferredGivenName;
        }
        return givenName;
    }

    /**
     * Validates the individual
     */
    public void validate(String actor) {
        this.status = "validated";
        this.updatedBy = actor;
    }

    /**
     * Marks individual as deceased
     */
    public void markDeceased(LocalDate deathDate, String actor) {
        this.status = "deceased";
        this.deathDate = deathDate;
        this.updatedBy = actor;
    }
}
