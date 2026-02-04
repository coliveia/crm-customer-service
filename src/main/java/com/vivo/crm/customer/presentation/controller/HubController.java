package com.vivo.crm.customer.presentation.controller;

import com.vivo.crm.customer.domain.model.Hub;
import com.vivo.crm.customer.domain.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * TMF - Hub REST Controller
 * Event Subscription Management for all TMF APIs
 * 
 * This controller handles event subscriptions (webhooks) for:
 * - TMF629 Customer Management events
 * - TMF632 Party Management events
 * - TMF669 Party Role Management events
 * 
 * Base URLs:
 * - /tmf-api/customer/v5/hub
 * - /tmf-api/partyManagement/v5/hub
 * - /tmf-api/partyRoleManagement/v5/hub
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class HubController {

    private final HubRepository hubRepository;

    // ========== TMF629 Customer Management Hub ==========

    /**
     * Create subscription for Customer events
     * POST /tmf-api/customer/v5/hub
     */
    @PostMapping("/tmf-api/customer/v5/hub")
    public ResponseEntity<Hub> createCustomerHub(@RequestBody Hub hub) {
        log.info("POST /tmf-api/customer/v5/hub - Creating subscription: {}", hub.getCallback());
        
        Hub saved = hubRepository.save(hub);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Get subscription for Customer events
     * GET /tmf-api/customer/v5/hub/{id}
     */
    @GetMapping("/tmf-api/customer/v5/hub/{id}")
    public ResponseEntity<Hub> getCustomerHub(@PathVariable String id) {
        log.info("GET /tmf-api/customer/v5/hub/{}", id);
        
        Optional<Hub> hub = hubRepository.findById(id);
        
        return hub
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete subscription for Customer events
     * DELETE /tmf-api/customer/v5/hub/{id}
     */
    @DeleteMapping("/tmf-api/customer/v5/hub/{id}")
    public ResponseEntity<Void> deleteCustomerHub(@PathVariable String id) {
        log.info("DELETE /tmf-api/customer/v5/hub/{}", id);
        
        if (!hubRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        hubRepository.deleteById(id);
        
        return ResponseEntity.noContent().build();
    }

    // ========== TMF632 Party Management Hub ==========

    /**
     * Create subscription for Party events
     * POST /tmf-api/partyManagement/v5/hub
     */
    @PostMapping("/tmf-api/partyManagement/v5/hub")
    public ResponseEntity<Hub> createPartyHub(@RequestBody Hub hub) {
        log.info("POST /tmf-api/partyManagement/v5/hub - Creating subscription: {}", hub.getCallback());
        
        Hub saved = hubRepository.save(hub);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Get subscription for Party events
     * GET /tmf-api/partyManagement/v5/hub/{id}
     */
    @GetMapping("/tmf-api/partyManagement/v5/hub/{id}")
    public ResponseEntity<Hub> getPartyHub(@PathVariable String id) {
        log.info("GET /tmf-api/partyManagement/v5/hub/{}", id);
        
        Optional<Hub> hub = hubRepository.findById(id);
        
        return hub
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete subscription for Party events
     * DELETE /tmf-api/partyManagement/v5/hub/{id}
     */
    @DeleteMapping("/tmf-api/partyManagement/v5/hub/{id}")
    public ResponseEntity<Void> deletePartyHub(@PathVariable String id) {
        log.info("DELETE /tmf-api/partyManagement/v5/hub/{}", id);
        
        if (!hubRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        hubRepository.deleteById(id);
        
        return ResponseEntity.noContent().build();
    }

    // ========== TMF669 Party Role Management Hub ==========

    /**
     * Create subscription for PartyRole events
     * POST /tmf-api/partyRoleManagement/v5/hub
     */
    @PostMapping("/tmf-api/partyRoleManagement/v5/hub")
    public ResponseEntity<Hub> createPartyRoleHub(@RequestBody Hub hub) {
        log.info("POST /tmf-api/partyRoleManagement/v5/hub - Creating subscription: {}", hub.getCallback());
        
        Hub saved = hubRepository.save(hub);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Get subscription for PartyRole events
     * GET /tmf-api/partyRoleManagement/v5/hub/{id}
     */
    @GetMapping("/tmf-api/partyRoleManagement/v5/hub/{id}")
    public ResponseEntity<Hub> getPartyRoleHub(@PathVariable String id) {
        log.info("GET /tmf-api/partyRoleManagement/v5/hub/{}", id);
        
        Optional<Hub> hub = hubRepository.findById(id);
        
        return hub
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete subscription for PartyRole events
     * DELETE /tmf-api/partyRoleManagement/v5/hub/{id}
     */
    @DeleteMapping("/tmf-api/partyRoleManagement/v5/hub/{id}")
    public ResponseEntity<Void> deletePartyRoleHub(@PathVariable String id) {
        log.info("DELETE /tmf-api/partyRoleManagement/v5/hub/{}", id);
        
        if (!hubRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        hubRepository.deleteById(id);
        
        return ResponseEntity.noContent().build();
    }
}
