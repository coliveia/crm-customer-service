package com.vivo.crm.customer.presentation.controller;

import com.vivo.crm.customer.domain.model.Organization;
import com.vivo.crm.customer.domain.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * TMF632 - Organization REST Controller
 * Party Management API - Organization resource
 * 
 * Base URL: /tmf-api/partyManagement/v5/organization
 */
@RestController
@RequestMapping("/tmf-api/partyManagement/v5/organization")
@RequiredArgsConstructor
@Slf4j
public class OrganizationController {

    private final OrganizationRepository organizationRepository;

    /**
     * List or find Organization objects
     * GET /organization
     */
    @GetMapping
    public ResponseEntity<List<Organization>> listOrganization(
            @RequestParam(required = false) String fields,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "100") Integer limit) {
        log.info("GET /tmf-api/partyManagement/v5/organization - Listing organizations");
        
        List<Organization> organizations = organizationRepository.findAll();
        
        // Apply pagination
        int start = Math.min(offset, organizations.size());
        int end = Math.min(offset + limit, organizations.size());
        List<Organization> pagedResult = organizations.subList(start, end);
        
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(organizations.size()))
                .header("X-Result-Count", String.valueOf(pagedResult.size()))
                .body(pagedResult);
    }

    /**
     * Creates an Organization
     * POST /organization
     */
    @PostMapping
    public ResponseEntity<Organization> createOrganization(@RequestBody Organization organization) {
        log.info("POST /tmf-api/partyManagement/v5/organization - Creating organization: {}", 
                organization.getName());
        
        Organization saved = organizationRepository.save(organization);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Retrieves an Organization by ID
     * GET /organization/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Organization> retrieveOrganization(
            @PathVariable String id,
            @RequestParam(required = false) String fields) {
        log.info("GET /tmf-api/partyManagement/v5/organization/{} - Retrieving organization", id);
        
        Optional<Organization> organization = organizationRepository.findById(id);
        
        return organization
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates partially an Organization
     * PATCH /organization/{id}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Organization> patchOrganization(
            @PathVariable String id,
            @RequestBody Map<String, Object> updates) {
        log.info("PATCH /tmf-api/partyManagement/v5/organization/{} - Updating organization", id);
        
        Optional<Organization> existingOpt = organizationRepository.findById(id);
        
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Organization existing = existingOpt.get();
        
        // Apply partial updates
        if (updates.containsKey("name")) {
            existing.setName((String) updates.get("name"));
        }
        if (updates.containsKey("tradingName")) {
            existing.setTradingName((String) updates.get("tradingName"));
        }
        if (updates.containsKey("nameType")) {
            existing.setNameType((String) updates.get("nameType"));
        }
        if (updates.containsKey("organizationType")) {
            existing.setOrganizationType((String) updates.get("organizationType"));
        }
        if (updates.containsKey("isLegalEntity")) {
            existing.setIsLegalEntity((Boolean) updates.get("isLegalEntity"));
        }
        if (updates.containsKey("isHeadOffice")) {
            existing.setIsHeadOffice((Boolean) updates.get("isHeadOffice"));
        }
        if (updates.containsKey("email")) {
            existing.setEmail((String) updates.get("email"));
        }
        if (updates.containsKey("phone")) {
            existing.setPhone((String) updates.get("phone"));
        }
        if (updates.containsKey("status")) {
            existing.setStatus((String) updates.get("status"));
        }
        
        Organization updated = organizationRepository.save(existing);
        
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes an Organization
     * DELETE /organization/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable String id) {
        log.info("DELETE /tmf-api/partyManagement/v5/organization/{} - Deleting organization", id);
        
        if (!organizationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        organizationRepository.deleteById(id);
        
        return ResponseEntity.noContent().build();
    }

    // ========== Additional endpoints ==========

    /**
     * Find by identification
     * GET /organization/identification/{type}/{number}
     */
    @GetMapping("/identification/{type}/{number}")
    public ResponseEntity<Organization> findByIdentification(
            @PathVariable String type,
            @PathVariable String number) {
        log.info("GET /tmf-api/partyManagement/v5/organization/identification/{}/{}", type, number);
        
        Optional<Organization> organization = organizationRepository
                .findByIdentificationTypeAndIdentificationNumber(type, number);
        
        return organization
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Search by name
     * GET /organization/search?name={name}
     */
    @GetMapping("/search")
    public ResponseEntity<List<Organization>> searchByName(@RequestParam String name) {
        log.info("GET /tmf-api/partyManagement/v5/organization/search?name={}", name);
        
        List<Organization> organizations = organizationRepository.searchByName(name);
        
        return ResponseEntity.ok(organizations);
    }

    /**
     * Find child organizations
     * GET /organization/{id}/children
     */
    @GetMapping("/{id}/children")
    public ResponseEntity<List<Organization>> findChildOrganizations(@PathVariable String id) {
        log.info("GET /tmf-api/partyManagement/v5/organization/{}/children", id);
        
        List<Organization> children = organizationRepository.findByParentOrganizationId(id);
        
        return ResponseEntity.ok(children);
    }
}
