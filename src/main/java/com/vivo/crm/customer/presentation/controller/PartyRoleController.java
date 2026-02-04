package com.vivo.crm.customer.presentation.controller;

import com.vivo.crm.customer.domain.model.Customer;
import com.vivo.crm.customer.domain.repository.CustomerRepository;
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
 * TMF669 - PartyRole REST Controller
 * Party Role Management API - PartyRole resource
 * 
 * Note: In this implementation, Customer extends PartyRole concept.
 * This controller exposes Customer data in TMF669 PartyRole format.
 * 
 * Base URL: /tmf-api/partyRoleManagement/v5/partyRole
 */
@RestController
@RequestMapping("/tmf-api/partyRoleManagement/v5/partyRole")
@RequiredArgsConstructor
@Slf4j
public class PartyRoleController {

    private final CustomerRepository customerRepository;

    /**
     * List or find PartyRole objects
     * GET /partyRole
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listPartyRole(
            @RequestParam(required = false) String fields,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "100") Integer limit) {
        log.info("GET /tmf-api/partyRoleManagement/v5/partyRole - Listing party roles");
        
        List<Customer> customers = customerRepository.findAll();
        
        // Apply pagination
        int start = Math.min(offset, customers.size());
        int end = Math.min(offset + limit, customers.size());
        List<Customer> pagedResult = customers.subList(start, end);
        
        // Convert to PartyRole format
        List<Map<String, Object>> partyRoles = pagedResult.stream()
                .map(this::customerToPartyRole)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(customers.size()))
                .header("X-Result-Count", String.valueOf(pagedResult.size()))
                .body(partyRoles);
    }

    /**
     * Creates a PartyRole
     * POST /partyRole
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPartyRole(@RequestBody Map<String, Object> partyRole) {
        log.info("POST /tmf-api/partyRoleManagement/v5/partyRole - Creating party role");
        
        // Convert PartyRole to Customer
        Customer customer = partyRoleToCustomer(partyRole);
        Customer saved = customerRepository.save(customer);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(customerToPartyRole(saved));
    }

    /**
     * Retrieves a PartyRole by ID
     * GET /partyRole/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> retrievePartyRole(
            @PathVariable String id,
            @RequestParam(required = false) String fields) {
        log.info("GET /tmf-api/partyRoleManagement/v5/partyRole/{} - Retrieving party role", id);
        
        Optional<Customer> customer = customerRepository.findById(id);
        
        return customer
                .map(c -> ResponseEntity.ok(customerToPartyRole(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates partially a PartyRole
     * PATCH /partyRole/{id}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, Object>> patchPartyRole(
            @PathVariable String id,
            @RequestBody Map<String, Object> updates) {
        log.info("PATCH /tmf-api/partyRoleManagement/v5/partyRole/{} - Updating party role", id);
        
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
        if (updates.containsKey("creditScore")) {
            existing.setCreditScore((Integer) updates.get("creditScore"));
        }
        if (updates.containsKey("creditRiskRating")) {
            existing.setCreditRiskRating((Integer) updates.get("creditRiskRating"));
        }
        
        Customer updated = customerRepository.save(existing);
        
        return ResponseEntity.ok(customerToPartyRole(updated));
    }

    /**
     * Deletes a PartyRole
     * DELETE /partyRole/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartyRole(@PathVariable String id) {
        log.info("DELETE /tmf-api/partyRoleManagement/v5/partyRole/{} - Deleting party role", id);
        
        if (!customerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        customerRepository.deleteById(id);
        
        return ResponseEntity.noContent().build();
    }

    // ========== Helper methods ==========

    /**
     * Converts Customer to TMF669 PartyRole format
     */
    private Map<String, Object> customerToPartyRole(Customer customer) {
        Map<String, Object> partyRole = new HashMap<>();
        
        partyRole.put("id", customer.getCustomerId());
        partyRole.put("href", "/tmf-api/partyRoleManagement/v5/partyRole/" + customer.getCustomerId());
        partyRole.put("name", customer.getName());
        partyRole.put("description", "Customer party role");
        partyRole.put("role", "Customer");
        partyRole.put("status", customer.getStatus());
        partyRole.put("@type", "Customer");
        partyRole.put("@baseType", "PartyRole");
        
        // Engaged party reference
        Map<String, Object> engagedParty = new HashMap<>();
        engagedParty.put("id", customer.getPartyRoleId());
        engagedParty.put("name", customer.getName());
        engagedParty.put("@type", customer.getCustomerType());
        engagedParty.put("@referredType", customer.getCustomerType());
        partyRole.put("engagedParty", engagedParty);
        
        // Credit profile
        if (customer.getCreditScore() != null || customer.getCreditRiskRating() != null) {
            Map<String, Object> creditProfile = new HashMap<>();
            creditProfile.put("creditScore", customer.getCreditScore());
            creditProfile.put("creditRiskRating", customer.getCreditRiskRating());
            creditProfile.put("@type", "CreditProfile");
            partyRole.put("creditProfile", List.of(creditProfile));
        }
        
        // Characteristics
        if (customer.getSegment() != null || customer.getRiskLevel() != null) {
            Map<String, Object> segmentChar = new HashMap<>();
            segmentChar.put("name", "segment");
            segmentChar.put("value", customer.getSegment());
            segmentChar.put("@type", "StringCharacteristic");
            
            Map<String, Object> riskChar = new HashMap<>();
            riskChar.put("name", "riskLevel");
            riskChar.put("value", customer.getRiskLevel());
            riskChar.put("@type", "StringCharacteristic");
            
            partyRole.put("characteristic", List.of(segmentChar, riskChar));
        }
        
        // Validity period
        Map<String, Object> validFor = new HashMap<>();
        validFor.put("startDateTime", customer.getCreatedAt());
        partyRole.put("validFor", validFor);
        
        return partyRole;
    }

    /**
     * Converts TMF669 PartyRole format to Customer
     */
    private Customer partyRoleToCustomer(Map<String, Object> partyRole) {
        Customer customer = new Customer();
        
        customer.setName((String) partyRole.get("name"));
        customer.setStatus((String) partyRole.getOrDefault("status", "ACTIVE"));
        
        // Extract engaged party info
        if (partyRole.containsKey("engagedParty")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> engagedParty = (Map<String, Object>) partyRole.get("engagedParty");
            String partyType = (String) engagedParty.get("@type");
            customer.setIdentificationType("Individual".equals(partyType) ? "CPF" : "CNPJ");
        }
        
        // Extract credit profile
        if (partyRole.containsKey("creditProfile")) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> creditProfiles = (List<Map<String, Object>>) partyRole.get("creditProfile");
            if (!creditProfiles.isEmpty()) {
                Map<String, Object> creditProfile = creditProfiles.get(0);
                if (creditProfile.get("creditScore") != null) {
                    customer.setCreditScore((Integer) creditProfile.get("creditScore"));
                }
                if (creditProfile.get("creditRiskRating") != null) {
                    customer.setCreditRiskRating((Integer) creditProfile.get("creditRiskRating"));
                }
            }
        }
        
        return customer;
    }
}
