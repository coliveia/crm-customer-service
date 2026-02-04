package com.vivo.crm.customer.presentation.controller;

import com.vivo.crm.customer.domain.model.Individual;
import com.vivo.crm.customer.domain.repository.IndividualRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * TMF632 - Individual REST Controller
 * Party Management API - Individual resource
 * 
 * Base URL: /tmf-api/partyManagement/v5/individual
 */
@RestController
@RequestMapping("/tmf-api/partyManagement/v5/individual")
@RequiredArgsConstructor
@Slf4j
public class IndividualController {

    private final IndividualRepository individualRepository;

    /**
     * List or find Individual objects
     * GET /individual
     */
    @GetMapping
    public ResponseEntity<List<Individual>> listIndividual(
            @RequestParam(required = false) String fields,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "100") Integer limit) {
        log.info("GET /tmf-api/partyManagement/v5/individual - Listing individuals");
        
        List<Individual> individuals = individualRepository.findAll();
        
        // Apply pagination
        int start = Math.min(offset, individuals.size());
        int end = Math.min(offset + limit, individuals.size());
        List<Individual> pagedResult = individuals.subList(start, end);
        
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(individuals.size()))
                .header("X-Result-Count", String.valueOf(pagedResult.size()))
                .body(pagedResult);
    }

    /**
     * Creates an Individual
     * POST /individual
     */
    @PostMapping
    public ResponseEntity<Individual> createIndividual(@RequestBody Individual individual) {
        log.info("POST /tmf-api/partyManagement/v5/individual - Creating individual: {}", 
                individual.getGivenName());
        
        Individual saved = individualRepository.save(individual);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Retrieves an Individual by ID
     * GET /individual/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Individual> retrieveIndividual(
            @PathVariable String id,
            @RequestParam(required = false) String fields) {
        log.info("GET /tmf-api/partyManagement/v5/individual/{} - Retrieving individual", id);
        
        Optional<Individual> individual = individualRepository.findById(id);
        
        return individual
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates partially an Individual
     * PATCH /individual/{id}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Individual> patchIndividual(
            @PathVariable String id,
            @RequestBody Map<String, Object> updates) {
        log.info("PATCH /tmf-api/partyManagement/v5/individual/{} - Updating individual", id);
        
        Optional<Individual> existingOpt = individualRepository.findById(id);
        
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Individual existing = existingOpt.get();
        
        // Apply partial updates
        if (updates.containsKey("givenName")) {
            existing.setGivenName((String) updates.get("givenName"));
        }
        if (updates.containsKey("familyName")) {
            existing.setFamilyName((String) updates.get("familyName"));
        }
        if (updates.containsKey("formattedName")) {
            existing.setFormattedName((String) updates.get("formattedName"));
        }
        if (updates.containsKey("preferredGivenName")) {
            existing.setPreferredGivenName((String) updates.get("preferredGivenName"));
        }
        if (updates.containsKey("title")) {
            existing.setTitle((String) updates.get("title"));
        }
        if (updates.containsKey("gender")) {
            existing.setGender((String) updates.get("gender"));
        }
        if (updates.containsKey("nationality")) {
            existing.setNationality((String) updates.get("nationality"));
        }
        if (updates.containsKey("maritalStatus")) {
            existing.setMaritalStatus((String) updates.get("maritalStatus"));
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
        
        Individual updated = individualRepository.save(existing);
        
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes an Individual
     * DELETE /individual/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndividual(@PathVariable String id) {
        log.info("DELETE /tmf-api/partyManagement/v5/individual/{} - Deleting individual", id);
        
        if (!individualRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        individualRepository.deleteById(id);
        
        return ResponseEntity.noContent().build();
    }

    // ========== Additional endpoints ==========

    /**
     * Find by identification
     * GET /individual/identification/{type}/{number}
     */
    @GetMapping("/identification/{type}/{number}")
    public ResponseEntity<Individual> findByIdentification(
            @PathVariable String type,
            @PathVariable String number) {
        log.info("GET /tmf-api/partyManagement/v5/individual/identification/{}/{}", type, number);
        
        Optional<Individual> individual = individualRepository
                .findByIdentificationTypeAndIdentificationNumber(type, number);
        
        return individual
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Search by name
     * GET /individual/search?name={name}
     */
    @GetMapping("/search")
    public ResponseEntity<List<Individual>> searchByName(@RequestParam String name) {
        log.info("GET /tmf-api/partyManagement/v5/individual/search?name={}", name);
        
        List<Individual> individuals = individualRepository.searchByName(name);
        
        return ResponseEntity.ok(individuals);
    }
}
