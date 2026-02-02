package com.vivo.crm.customer.presentation.controller;

import com.vivo.crm.customer.application.service.CustomerServiceReactive;
import com.vivo.crm.customer.presentation.dto.CreateCustomerRequest;
import com.vivo.crm.customer.presentation.dto.CustomerDTO;
import com.vivo.crm.customer.presentation.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * TMF629 - Customer REST Controller
 * Reactive API endpoints for customer management
 */
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerServiceReactive customerService;
    private final CustomerMapper customerMapper;

    /**
     * Create a new customer
     * POST /customers
     */
    @PostMapping
    public Mono<ResponseEntity<CustomerDTO>> createCustomer(@RequestBody Mono<CreateCustomerRequest> request) {
        log.info("POST /customers - Creating new customer");
        
        return request
                .map(customerMapper::toEntity)
                .flatMap(customerService::createCustomer)
                .map(customerMapper::toDTO)
                .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto))
                .onErrorResume(e -> {
                    log.error("Error creating customer", e);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    /**
     * Get customer by ID
     * GET /customers/{id}
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CustomerDTO>> getCustomerById(@PathVariable String id) {
        log.info("GET /customers/{} - Getting customer", id);
        
        return customerService.getCustomerById(id)
                .map(customerMapper::toDTO)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Customer not found: {}", id);
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }

    /**
     * Get customer by external ID
     * GET /customers/external/{externalId}
     */
    @GetMapping("/external/{externalId}")
    public Mono<ResponseEntity<CustomerDTO>> getCustomerByExternalId(@PathVariable String externalId) {
        log.info("GET /customers/external/{} - Getting customer by external ID", externalId);
        
        return customerService.getCustomerByExternalId(externalId)
                .map(customerMapper::toDTO)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Customer not found with external ID: {}", externalId);
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }

    /**
     * Get customer by email
     * GET /customers/email/{email}
     */
    @GetMapping("/email/{email}")
    public Mono<ResponseEntity<CustomerDTO>> getCustomerByEmail(@PathVariable String email) {
        log.info("GET /customers/email/{} - Getting customer by email", email);
        
        return customerService.getCustomerByEmail(email)
                .map(customerMapper::toDTO)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Customer not found with email: {}", email);
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }

    /**
     * Get customer by CPF/CNPJ
     * GET /customers/cpf/{cpfCnpj}
     */
    @GetMapping("/cpf/{cpfCnpj}")
    public Mono<ResponseEntity<CustomerDTO>> getCustomerByCpfCnpj(@PathVariable String cpfCnpj) {
        log.info("GET /customers/cpf/{} - Getting customer by CPF/CNPJ", cpfCnpj);
        
        return customerService.getCustomerByCpfCnpj(cpfCnpj)
                .map(customerMapper::toDTO)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Customer not found with CPF/CNPJ: {}", cpfCnpj);
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }

    /**
     * Search customers by name
     * GET /customers/search?name={name}
     */
    @GetMapping("/search")
    public Flux<CustomerDTO> searchCustomersByName(@RequestParam String name) {
        log.info("GET /customers/search?name={} - Searching customers", name);
        
        return customerService.searchCustomersByName(name)
                .map(customerMapper::toDTO)
                .onErrorResume(e -> {
                    log.error("Error searching customers by name: {}", name);
                    return Flux.empty();
                });
    }

    /**
     * Get customers by status
     * GET /customers/status/{status}
     */
    @GetMapping("/status/{status}")
    public Flux<CustomerDTO> getCustomersByStatus(@PathVariable String status) {
        log.info("GET /customers/status/{} - Getting customers by status", status);
        
        return customerService.getCustomersByStatus(status)
                .map(customerMapper::toDTO)
                .onErrorResume(e -> {
                    log.error("Error getting customers by status: {}", status);
                    return Flux.empty();
                });
    }

    /**
     * Get customers by segment
     * GET /customers/segment/{segment}
     */
    @GetMapping("/segment/{segment}")
    public Flux<CustomerDTO> getCustomersBySegment(@PathVariable String segment) {
        log.info("GET /customers/segment/{} - Getting customers by segment", segment);
        
        return customerService.getCustomersBySegment(segment)
                .map(customerMapper::toDTO)
                .onErrorResume(e -> {
                    log.error("Error getting customers by segment: {}", segment);
                    return Flux.empty();
                });
    }

    /**
     * Get customers by risk level
     * GET /customers/risk/{riskLevel}
     */
    @GetMapping("/risk/{riskLevel}")
    public Flux<CustomerDTO> getCustomersByRiskLevel(@PathVariable String riskLevel) {
        log.info("GET /customers/risk/{} - Getting customers by risk level", riskLevel);
        
        return customerService.getCustomersByRiskLevel(riskLevel)
                .map(customerMapper::toDTO)
                .onErrorResume(e -> {
                    log.error("Error getting customers by risk level: {}", riskLevel);
                    return Flux.empty();
                });
    }

    /**
     * Get high risk active customers
     * GET /customers/risk/high/active
     */
    @GetMapping("/risk/high/active")
    public Flux<CustomerDTO> getHighRiskActiveCustomers() {
        log.info("GET /customers/risk/high/active - Getting high risk active customers");
        
        return customerService.getHighRiskActiveCustomers()
                .map(customerMapper::toDTO)
                .onErrorResume(e -> {
                    log.error("Error getting high risk active customers");
                    return Flux.empty();
                });
    }

    /**
     * Update customer
     * PUT /customers/{id}
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CustomerDTO>> updateCustomer(
            @PathVariable String id,
            @RequestBody Mono<CustomerDTO> request) {
        log.info("PUT /customers/{} - Updating customer", id);
        
        return request
                .flatMap(dto -> customerService.getCustomerById(id)
                        .doOnNext(customer -> customerMapper.updateEntityFromDTO(dto, customer))
                        .flatMap(customerService::updateCustomer))
                .map(customerMapper::toDTO)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Error updating customer: {}", id);
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }

    /**
     * Activate customer
     * POST /customers/{id}/activate
     */
    @PostMapping("/{id}/activate")
    public Mono<ResponseEntity<CustomerDTO>> activateCustomer(
            @PathVariable String id,
            @RequestParam(required = false, defaultValue = "SYSTEM") String actor) {
        log.info("POST /customers/{}/activate - Activating customer", id);
        
        return customerService.activateCustomer(id, actor)
                .map(customerMapper::toDTO)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Error activating customer: {}", id);
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }

    /**
     * Deactivate customer
     * POST /customers/{id}/deactivate
     */
    @PostMapping("/{id}/deactivate")
    public Mono<ResponseEntity<CustomerDTO>> deactivateCustomer(
            @PathVariable String id,
            @RequestParam(required = false, defaultValue = "SYSTEM") String actor) {
        log.info("POST /customers/{}/deactivate - Deactivating customer", id);
        
        return customerService.deactivateCustomer(id, actor)
                .map(customerMapper::toDTO)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Error deactivating customer: {}", id);
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }

    /**
     * Suspend customer
     * POST /customers/{id}/suspend
     */
    @PostMapping("/{id}/suspend")
    public Mono<ResponseEntity<CustomerDTO>> suspendCustomer(
            @PathVariable String id,
            @RequestParam String reason,
            @RequestParam(required = false, defaultValue = "SYSTEM") String actor) {
        log.info("POST /customers/{}/suspend - Suspending customer with reason: {}", id, reason);
        
        return customerService.suspendCustomer(id, reason, actor)
                .map(customerMapper::toDTO)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Error suspending customer: {}", id);
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }

    /**
     * Delete customer
     * DELETE /customers/{id}
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable String id) {
        log.info("DELETE /customers/{} - Deleting customer", id);
        
        return customerService.deleteCustomer(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorResume(e -> {
                    log.error("Error deleting customer: {}", id);
                    return Mono.just(ResponseEntity.notFound().build());
                });
    }

    /**
     * Count customers by status
     * GET /customers/count/status/{status}
     */
    @GetMapping("/count/status/{status}")
    public Mono<ResponseEntity<Long>> countCustomersByStatus(@PathVariable String status) {
        log.info("GET /customers/count/status/{} - Counting customers by status", status);
        
        return customerService.countCustomersByStatus(status)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Error counting customers by status: {}", status);
                    return Mono.just(ResponseEntity.ok(0L));
                });
    }
}
