package com.vivo.crm.customer.presentation.controller;

import com.vivo.crm.customer.domain.model.Customer;
import com.vivo.crm.customer.domain.repository.CustomerRepository;
import com.vivo.crm.customer.presentation.dto.CustomerDTO;
import com.vivo.crm.customer.presentation.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * TMF629 - Customer Management REST Controller
 * Exposes Customer API following TMF629 v5.0.1 specification
 * 
 * Base URL: /tmf-api/customer/v5/customer
 */
@RestController
@RequestMapping("/tmf-api/customer/v5/customer")
@RequiredArgsConstructor
@Slf4j
public class TMF629CustomerController {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    /**
     * List or find Customer objects
     * GET /customer
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listCustomer(
            @RequestParam(required = false) String fields,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "100") Integer limit) {
        log.info("GET /tmf-api/customer/v5/customer - Listing customers");
        
        List<Customer> customers = customerRepository.findAll();
        
        // Apply pagination
        int start = Math.min(offset, customers.size());
        int end = Math.min(offset + limit, customers.size());
        List<Customer> pagedResult = customers.subList(start, end);
        
        // Convert to TMF629 format
        List<Map<String, Object>> tmfCustomers = pagedResult.stream()
                .map(this::customerToTMF629Format)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(customers.size()))
                .header("X-Result-Count", String.valueOf(pagedResult.size()))
                .body(tmfCustomers);
    }

    /**
     * Creates a Customer
     * POST /customer
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCustomer(@RequestBody Map<String, Object> customerData) {
        log.info("POST /tmf-api/customer/v5/customer - Creating customer");
        
        Customer customer = tmf629FormatToCustomer(customerData);
        Customer saved = customerRepository.save(customer);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(customerToTMF629Format(saved));
    }

    /**
     * Retrieves a Customer by ID
     * GET /customer/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> retrieveCustomer(
            @PathVariable String id,
            @RequestParam(required = false) String fields) {
        log.info("GET /tmf-api/customer/v5/customer/{} - Retrieving customer", id);
        
        Optional<Customer> customer = customerRepository.findById(id);
        
        return customer
                .map(c -> ResponseEntity.ok(customerToTMF629Format(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates partially a Customer
     * PATCH /customer/{id}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, Object>> patchCustomer(
            @PathVariable String id,
            @RequestBody Map<String, Object> updates) {
        log.info("PATCH /tmf-api/customer/v5/customer/{} - Updating customer", id);
        
        Optional<Customer> existingOpt = customerRepository.findById(id);
        
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Customer existing = existingOpt.get();
        
        // Apply partial updates
        if (updates.containsKey("name")) {
            existing.setName((String) updates.get("name"));
        }
        if (updates.containsKey("status")) {
            existing.setStatus((String) updates.get("status"));
        }
        if (updates.containsKey("segment")) {
            existing.setSegment((String) updates.get("segment"));
        }
        if (updates.containsKey("formattedName")) {
            existing.setFormattedName((String) updates.get("formattedName"));
        }
        if (updates.containsKey("givenName")) {
            existing.setGivenName((String) updates.get("givenName"));
        }
        if (updates.containsKey("familyName")) {
            existing.setFamilyName((String) updates.get("familyName"));
        }
        if (updates.containsKey("preferredGivenName")) {
            existing.setPreferredGivenName((String) updates.get("preferredGivenName"));
        }
        if (updates.containsKey("tradingName")) {
            existing.setTradingName((String) updates.get("tradingName"));
        }
        
        // Handle nested creditProfile
        if (updates.containsKey("creditProfile")) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> creditProfiles = (List<Map<String, Object>>) updates.get("creditProfile");
            if (creditProfiles != null && !creditProfiles.isEmpty()) {
                Map<String, Object> cp = creditProfiles.get(0);
                if (cp.containsKey("creditScore")) {
                    existing.setCreditScore((Integer) cp.get("creditScore"));
                }
                if (cp.containsKey("creditRiskRating")) {
                    existing.setCreditRiskRating((Integer) cp.get("creditRiskRating"));
                }
            }
        }
        
        Customer updated = customerRepository.save(existing);
        
        return ResponseEntity.ok(customerToTMF629Format(updated));
    }

    /**
     * Deletes a Customer
     * DELETE /customer/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String id) {
        log.info("DELETE /tmf-api/customer/v5/customer/{} - Deleting customer", id);
        
        if (!customerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        customerRepository.deleteById(id);
        
        return ResponseEntity.noContent().build();
    }

    // ========== Helper methods ==========

    /**
     * Converts Customer entity to TMF629 format
     */
    private Map<String, Object> customerToTMF629Format(Customer customer) {
        Map<String, Object> tmf = new HashMap<>();
        
        // Basic fields
        tmf.put("id", customer.getCustomerId());
        tmf.put("href", "/tmf-api/customer/v5/customer/" + customer.getCustomerId());
        tmf.put("name", customer.getName());
        tmf.put("status", customer.getStatus());
        tmf.put("@type", "Customer");
        tmf.put("@baseType", "PartyRole");
        
        // Engaged party
        Map<String, Object> engagedParty = new HashMap<>();
        engagedParty.put("id", customer.getPartyRoleId());
        engagedParty.put("name", customer.getFormattedName() != null ? customer.getFormattedName() : customer.getName());
        engagedParty.put("@type", customer.getCustomerType());
        engagedParty.put("@referredType", customer.getCustomerType());
        
        // Add individual/organization specific fields
        if ("Individual".equals(customer.getCustomerType())) {
            engagedParty.put("givenName", customer.getGivenName());
            engagedParty.put("familyName", customer.getFamilyName());
            engagedParty.put("formattedName", customer.getFormattedName());
            engagedParty.put("preferredGivenName", customer.getPreferredGivenName());
            
            // Individual identification
            if (customer.getIdentificationNumber() != null) {
                Map<String, Object> identification = new HashMap<>();
                identification.put("identificationType", customer.getIdentificationType());
                identification.put("identificationId", customer.getIdentificationNumber());
                identification.put("@type", "IndividualIdentification");
                engagedParty.put("individualIdentification", List.of(identification));
            }
        } else if ("Organization".equals(customer.getCustomerType())) {
            engagedParty.put("tradingName", customer.getTradingName());
            
            // Organization identification
            if (customer.getIdentificationNumber() != null) {
                Map<String, Object> identification = new HashMap<>();
                identification.put("identificationType", customer.getIdentificationType());
                identification.put("identificationId", customer.getIdentificationNumber());
                identification.put("@type", "OrganizationIdentification");
                engagedParty.put("organizationIdentification", List.of(identification));
            }
        }
        
        tmf.put("engagedParty", engagedParty);
        
        // Credit profile
        if (customer.getCreditScore() != null || customer.getCreditRiskRating() != null) {
            Map<String, Object> creditProfile = new HashMap<>();
            creditProfile.put("creditScore", customer.getCreditScore());
            creditProfile.put("creditRiskRating", customer.getCreditRiskRating());
            creditProfile.put("@type", "CreditProfile");
            tmf.put("creditProfile", List.of(creditProfile));
        }
        
        // Contact medium
        if (customer.getEmail() != null || customer.getPhone() != null) {
            List<Map<String, Object>> contactMedia = new java.util.ArrayList<>();
            
            if (customer.getEmail() != null) {
                Map<String, Object> emailContact = new HashMap<>();
                emailContact.put("mediumType", "email");
                emailContact.put("emailAddress", customer.getEmail());
                emailContact.put("@type", "EmailContactMedium");
                contactMedia.add(emailContact);
            }
            
            if (customer.getPhone() != null) {
                Map<String, Object> phoneContact = new HashMap<>();
                phoneContact.put("mediumType", "phone");
                phoneContact.put("phoneNumber", customer.getPhone());
                phoneContact.put("@type", "PhoneContactMedium");
                contactMedia.add(phoneContact);
            }
            
            tmf.put("contactMedium", contactMedia);
        }
        
        // Characteristics
        List<Map<String, Object>> characteristics = new java.util.ArrayList<>();
        
        if (customer.getSegment() != null) {
            Map<String, Object> segmentChar = new HashMap<>();
            segmentChar.put("name", "segment");
            segmentChar.put("value", customer.getSegment());
            segmentChar.put("@type", "StringCharacteristic");
            characteristics.add(segmentChar);
        }
        
        if (customer.getRiskLevel() != null) {
            Map<String, Object> riskChar = new HashMap<>();
            riskChar.put("name", "riskLevel");
            riskChar.put("value", customer.getRiskLevel());
            riskChar.put("@type", "StringCharacteristic");
            characteristics.add(riskChar);
        }
        
        if (customer.getBiometriaStatus() != null) {
            Map<String, Object> biometriaChar = new HashMap<>();
            biometriaChar.put("name", "biometriaStatus");
            biometriaChar.put("value", customer.getBiometriaStatus());
            biometriaChar.put("@type", "StringCharacteristic");
            characteristics.add(biometriaChar);
            
            Map<String, Object> biometriaMessageChar = new HashMap<>();
            biometriaMessageChar.put("name", "biometriaMessage");
            biometriaMessageChar.put("value", customer.getBiometriaMessage());
            biometriaMessageChar.put("@type", "StringCharacteristic");
            characteristics.add(biometriaMessageChar);
        }
        
        if (customer.getCodigoGrupo() != null) {
            Map<String, Object> grupoChar = new HashMap<>();
            grupoChar.put("name", "codigoGrupo");
            grupoChar.put("value", customer.getCodigoGrupo());
            grupoChar.put("@type", "StringCharacteristic");
            characteristics.add(grupoChar);
        }
        
        if (customer.getNomeGrupo() != null) {
            Map<String, Object> nomeGrupoChar = new HashMap<>();
            nomeGrupoChar.put("name", "nomeGrupo");
            nomeGrupoChar.put("value", customer.getNomeGrupo());
            nomeGrupoChar.put("@type", "StringCharacteristic");
            characteristics.add(nomeGrupoChar);
        }
        
        if (!characteristics.isEmpty()) {
            tmf.put("characteristic", characteristics);
        }
        
        // Validity period
        Map<String, Object> validFor = new HashMap<>();
        validFor.put("startDateTime", customer.getCreatedAt());
        tmf.put("validFor", validFor);
        
        return tmf;
    }

    /**
     * Converts TMF629 format to Customer entity
     */
    private Customer tmf629FormatToCustomer(Map<String, Object> tmf) {
        Customer customer = new Customer();
        
        customer.setName((String) tmf.get("name"));
        customer.setStatus((String) tmf.getOrDefault("status", "ACTIVE"));
        
        // Extract engaged party info
        if (tmf.containsKey("engagedParty")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> engagedParty = (Map<String, Object>) tmf.get("engagedParty");
            
            String partyType = (String) engagedParty.get("@type");
            
            if ("Individual".equals(partyType)) {
                customer.setIdentificationType("CPF");
                customer.setGivenName((String) engagedParty.get("givenName"));
                customer.setFamilyName((String) engagedParty.get("familyName"));
                customer.setFormattedName((String) engagedParty.get("formattedName"));
                customer.setPreferredGivenName((String) engagedParty.get("preferredGivenName"));
                
                // Extract individual identification
                if (engagedParty.containsKey("individualIdentification")) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> identifications = 
                            (List<Map<String, Object>>) engagedParty.get("individualIdentification");
                    if (!identifications.isEmpty()) {
                        Map<String, Object> id = identifications.get(0);
                        customer.setIdentificationType((String) id.get("identificationType"));
                        customer.setIdentificationNumber((String) id.get("identificationId"));
                    }
                }
            } else if ("Organization".equals(partyType)) {
                customer.setIdentificationType("CNPJ");
                customer.setTradingName((String) engagedParty.get("tradingName"));
                
                // Extract organization identification
                if (engagedParty.containsKey("organizationIdentification")) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> identifications = 
                            (List<Map<String, Object>>) engagedParty.get("organizationIdentification");
                    if (!identifications.isEmpty()) {
                        Map<String, Object> id = identifications.get(0);
                        customer.setIdentificationType((String) id.get("identificationType"));
                        customer.setIdentificationNumber((String) id.get("identificationId"));
                    }
                }
            }
        }
        
        // Extract credit profile
        if (tmf.containsKey("creditProfile")) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> creditProfiles = (List<Map<String, Object>>) tmf.get("creditProfile");
            if (!creditProfiles.isEmpty()) {
                Map<String, Object> cp = creditProfiles.get(0);
                if (cp.get("creditScore") != null) {
                    customer.setCreditScore((Integer) cp.get("creditScore"));
                }
                if (cp.get("creditRiskRating") != null) {
                    customer.setCreditRiskRating((Integer) cp.get("creditRiskRating"));
                }
            }
        }
        
        // Extract contact medium
        if (tmf.containsKey("contactMedium")) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> contactMedia = (List<Map<String, Object>>) tmf.get("contactMedium");
            for (Map<String, Object> contact : contactMedia) {
                String type = (String) contact.get("@type");
                if ("EmailContactMedium".equals(type)) {
                    customer.setEmail((String) contact.get("emailAddress"));
                } else if ("PhoneContactMedium".equals(type)) {
                    customer.setPhone((String) contact.get("phoneNumber"));
                }
            }
        }
        
        // Extract characteristics
        if (tmf.containsKey("characteristic")) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> characteristics = (List<Map<String, Object>>) tmf.get("characteristic");
            for (Map<String, Object> char_ : characteristics) {
                String name = (String) char_.get("name");
                Object value = char_.get("value");
                
                switch (name) {
                    case "segment":
                        customer.setSegment((String) value);
                        break;
                    case "riskLevel":
                        customer.setRiskLevel((String) value);
                        break;
                    case "biometriaStatus":
                        customer.setBiometriaStatus((String) value);
                        break;
                    case "codigoGrupo":
                        customer.setCodigoGrupo((String) value);
                        break;
                    case "nomeGrupo":
                        customer.setNomeGrupo((String) value);
                        break;
                }
            }
        }
        
        return customer;
    }
}
