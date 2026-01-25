package com.vivo.crm.customer.interfaces.rest;

import com.vivo.crm.customer.application.service.CustomerService;
import com.vivo.crm.customer.domain.model.Customer;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST Controller para operações de Customer Management
 * Usa WebFlux para APIs reativas
 */
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Customer Management", description = "APIs para gerenciamento de clientes")
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Cria um novo customer
     */
    @PostMapping
    @Operation(summary = "Criar novo customer", description = "Cria um novo cliente no sistema")
    public Mono<ResponseEntity<Customer>> createCustomer(
            @Valid @RequestBody Customer customer) {
        
        log.info("POST /api/v1/customers - Creating new customer");
        
        return customerService.createCustomer(customer)
                .map(createdCustomer -> ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer))
                .doOnSuccess(response -> log.info("Customer created: {}", response.getBody().getCustomerId()))
                .doOnError(error -> log.error("Error creating customer", error));
    }

    /**
     * Busca customer por ID
     */
    @GetMapping("/{customerId}")
    @Operation(summary = "Buscar customer por ID", description = "Retorna os detalhes de um customer específico")
    public Mono<ResponseEntity<Customer>> getCustomerById(
            @Parameter(description = "ID do customer") @PathVariable String customerId) {
        
        log.info("GET /api/v1/customers/{} - Fetching customer", customerId);
        
        return customerService.getCustomerById(customerId)
                .map(ResponseEntity::ok)
                .doOnError(error -> log.error("Error fetching customer: {}", customerId, error));
    }

    /**
     * Busca customer por external ID
     */
    @GetMapping("/external/{externalId}")
    @Operation(summary = "Buscar customer por external ID", description = "Retorna um customer pelo ID externo")
    public Mono<ResponseEntity<Customer>> getCustomerByExternalId(
            @Parameter(description = "External ID do customer") @PathVariable String externalId) {
        
        log.info("GET /api/v1/customers/external/{} - Fetching customer", externalId);
        
        return customerService.getCustomerByExternalId(externalId)
                .map(ResponseEntity::ok)
                .doOnError(error -> log.error("Error fetching customer by external ID: {}", externalId, error));
    }

    /**
     * Lista todos os customers
     */
    @GetMapping
    @Operation(summary = "Listar todos os customers", description = "Retorna todos os customers com paginação")
    public Mono<ResponseEntity<Page<Customer>>> getAllCustomers(Pageable pageable) {
        
        log.info("GET /api/v1/customers - Fetching all customers");
        
        return customerService.getAllCustomers(pageable)
                .map(ResponseEntity::ok)
                .doOnError(error -> log.error("Error fetching customers", error));
    }

    /**
     * Lista customers por status
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Listar customers por status", description = "Retorna todos os customers com um status específico")
    public Flux<Customer> getCustomersByStatus(
            @Parameter(description = "Status do customer") @PathVariable String status) {
        
        log.info("GET /api/v1/customers/status/{} - Fetching customers by status", status);
        
        return customerService.getCustomersByStatus(status);
    }

    /**
     * Lista customers por segmento
     */
    @GetMapping("/segment/{segment}")
    @Operation(summary = "Listar customers por segmento", description = "Retorna todos os customers de um segmento específico")
    public Flux<Customer> getCustomersBySegment(
            @Parameter(description = "Segmento do customer") @PathVariable String segment) {
        
        log.info("GET /api/v1/customers/segment/{} - Fetching customers by segment", segment);
        
        return customerService.getCustomersBySegment(segment);
    }

    /**
     * Ativa um customer
     */
    @PutMapping("/{customerId}/activate")
    @Operation(summary = "Ativar customer", description = "Ativa um customer no sistema")
    public Mono<ResponseEntity<Customer>> activateCustomer(
            @Parameter(description = "ID do customer") @PathVariable String customerId,
            @RequestParam String actor) {
        
        log.info("PUT /api/v1/customers/{}/activate - Activating customer", customerId);
        
        return customerService.activateCustomer(customerId, actor)
                .map(ResponseEntity::ok)
                .doOnError(error -> log.error("Error activating customer: {}", customerId, error));
    }

    /**
     * Desativa um customer
     */
    @PutMapping("/{customerId}/deactivate")
    @Operation(summary = "Desativar customer", description = "Desativa um customer no sistema")
    public Mono<ResponseEntity<Customer>> deactivateCustomer(
            @Parameter(description = "ID do customer") @PathVariable String customerId,
            @RequestParam String actor) {
        
        log.info("PUT /api/v1/customers/{}/deactivate - Deactivating customer", customerId);
        
        return customerService.deactivateCustomer(customerId, actor)
                .map(ResponseEntity::ok)
                .doOnError(error -> log.error("Error deactivating customer: {}", customerId, error));
    }

    /**
     * Suspende um customer
     */
    @PutMapping("/{customerId}/suspend")
    @Operation(summary = "Suspender customer", description = "Suspende um customer no sistema")
    public Mono<ResponseEntity<Customer>> suspendCustomer(
            @Parameter(description = "ID do customer") @PathVariable String customerId,
            @RequestParam String reason,
            @RequestParam String actor) {
        
        log.info("PUT /api/v1/customers/{}/suspend - Suspending customer", customerId);
        
        return customerService.suspendCustomer(customerId, reason, actor)
                .map(ResponseEntity::ok)
                .doOnError(error -> log.error("Error suspending customer: {}", customerId, error));
    }

    /**
     * Atualiza o segmento do customer
     */
    @PutMapping("/{customerId}/segment")
    @Operation(summary = "Atualizar segmento", description = "Atualiza o segmento de um customer")
    public Mono<ResponseEntity<Customer>> updateSegment(
            @Parameter(description = "ID do customer") @PathVariable String customerId,
            @RequestParam String newSegment,
            @RequestParam String actor) {
        
        log.info("PUT /api/v1/customers/{}/segment - Updating segment to: {}", customerId, newSegment);
        
        return customerService.updateSegment(customerId, newSegment, actor)
                .map(ResponseEntity::ok)
                .doOnError(error -> log.error("Error updating customer segment: {}", customerId, error));
    }

    /**
     * Atualiza o nível de risco do customer
     */
    @PutMapping("/{customerId}/risk-level")
    @Operation(summary = "Atualizar nível de risco", description = "Atualiza o nível de risco de um customer")
    public Mono<ResponseEntity<Customer>> updateRiskLevel(
            @Parameter(description = "ID do customer") @PathVariable String customerId,
            @RequestParam String newRiskLevel,
            @RequestParam String actor) {
        
        log.info("PUT /api/v1/customers/{}/risk-level - Updating risk level to: {}", customerId, newRiskLevel);
        
        return customerService.updateRiskLevel(customerId, newRiskLevel, actor)
                .map(ResponseEntity::ok)
                .doOnError(error -> log.error("Error updating customer risk level: {}", customerId, error));
    }
}
