package com.vivo.crm.customer.presentation.controller;

import com.vivo.crm.customer.domain.model.PartyRoleSpecification;
import com.vivo.crm.customer.domain.repository.PartyRoleSpecificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * TMF669 - PartyRoleSpecification REST Controller
 * Party Role Management API - PartyRoleSpecification resource
 * 
 * Base URL: /tmf-api/partyRoleManagement/v5/partyRoleSpecification
 */
@RestController
@RequestMapping("/tmf-api/partyRoleManagement/v5/partyRoleSpecification")
@RequiredArgsConstructor
@Slf4j
public class PartyRoleSpecificationController {

    private final PartyRoleSpecificationRepository specificationRepository;

    /**
     * List or find PartyRoleSpecification objects
     * GET /partyRoleSpecification
     */
    @GetMapping
    public ResponseEntity<List<PartyRoleSpecification>> listPartyRoleSpecification(
            @RequestParam(required = false) String fields,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "100") Integer limit) {
        log.info("GET /tmf-api/partyRoleManagement/v5/partyRoleSpecification - Listing specifications");
        
        List<PartyRoleSpecification> specifications = specificationRepository.findAll();
        
        // Apply pagination
        int start = Math.min(offset, specifications.size());
        int end = Math.min(offset + limit, specifications.size());
        List<PartyRoleSpecification> pagedResult = specifications.subList(start, end);
        
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(specifications.size()))
                .header("X-Result-Count", String.valueOf(pagedResult.size()))
                .body(pagedResult);
    }

    /**
     * Creates a PartyRoleSpecification
     * POST /partyRoleSpecification
     */
    @PostMapping
    public ResponseEntity<PartyRoleSpecification> createPartyRoleSpecification(
            @RequestBody PartyRoleSpecification specification) {
        log.info("POST /tmf-api/partyRoleManagement/v5/partyRoleSpecification - Creating: {}", 
                specification.getName());
        
        PartyRoleSpecification saved = specificationRepository.save(specification);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Retrieves a PartyRoleSpecification by ID
     * GET /partyRoleSpecification/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<PartyRoleSpecification> retrievePartyRoleSpecification(
            @PathVariable String id,
            @RequestParam(required = false) String fields) {
        log.info("GET /tmf-api/partyRoleManagement/v5/partyRoleSpecification/{}", id);
        
        Optional<PartyRoleSpecification> specification = specificationRepository.findById(id);
        
        return specification
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates partially a PartyRoleSpecification
     * PATCH /partyRoleSpecification/{id}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<PartyRoleSpecification> patchPartyRoleSpecification(
            @PathVariable String id,
            @RequestBody Map<String, Object> updates) {
        log.info("PATCH /tmf-api/partyRoleManagement/v5/partyRoleSpecification/{}", id);
        
        Optional<PartyRoleSpecification> existingOpt = specificationRepository.findById(id);
        
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        PartyRoleSpecification existing = existingOpt.get();
        
        // Apply partial updates
        if (updates.containsKey("name")) {
            existing.setName((String) updates.get("name"));
        }
        if (updates.containsKey("description")) {
            existing.setDescription((String) updates.get("description"));
        }
        if (updates.containsKey("status")) {
            existing.setStatus((String) updates.get("status"));
        }
        if (updates.containsKey("statusReason")) {
            existing.setStatusReason((String) updates.get("statusReason"));
        }
        if (updates.containsKey("version")) {
            existing.setVersion((String) updates.get("version"));
        }
        
        PartyRoleSpecification updated = specificationRepository.save(existing);
        
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a PartyRoleSpecification
     * DELETE /partyRoleSpecification/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartyRoleSpecification(@PathVariable String id) {
        log.info("DELETE /tmf-api/partyRoleManagement/v5/partyRoleSpecification/{}", id);
        
        if (!specificationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        specificationRepository.deleteById(id);
        
        return ResponseEntity.noContent().build();
    }

    // ========== Additional endpoints ==========

    /**
     * Find active specifications
     * GET /partyRoleSpecification/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<PartyRoleSpecification>> findActiveSpecifications() {
        log.info("GET /tmf-api/partyRoleManagement/v5/partyRoleSpecification/active");
        
        List<PartyRoleSpecification> active = specificationRepository.findActiveSpecifications();
        
        return ResponseEntity.ok(active);
    }

    /**
     * Search by name or description
     * GET /partyRoleSpecification/search?query={query}
     */
    @GetMapping("/search")
    public ResponseEntity<List<PartyRoleSpecification>> searchSpecifications(
            @RequestParam String query) {
        log.info("GET /tmf-api/partyRoleManagement/v5/partyRoleSpecification/search?query={}", query);
        
        List<PartyRoleSpecification> results = specificationRepository
                .searchByNameOrDescription(query);
        
        return ResponseEntity.ok(results);
    }
}
