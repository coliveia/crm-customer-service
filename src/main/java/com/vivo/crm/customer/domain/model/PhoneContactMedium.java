package com.vivo.crm.customer.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TMF629 - PhoneContactMedium Entity
 * Specialization of ContactMedium for phone numbers
 */
@Entity
@Table(name = "phone_contact_medium")
@DiscriminatorValue("PHONE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneContactMedium extends ContactMedium {

    @Column(name = "phone_number", length = 20, nullable = false)
    private String phoneNumber;

    @Column(name = "country_code", length = 5)
    private String countryCode;

    @Column(name = "area_code", length = 5)
    private String areaCode;
}
