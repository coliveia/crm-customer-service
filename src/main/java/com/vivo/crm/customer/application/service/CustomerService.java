package com.vivo.crm.customer.application.service;

import com.vivo.crm.customer.domain.model.Customer;
import com.vivo.crm.customer.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

/**
 * Service para operações de negócio de Customers
 * Implementa lógica de negócio e orquestração
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    /**
     * Cria um novo customer
     */
    @Transactional
    public Mono<Customer> createCustomer(Customer customer) {
        return Mono.fromCallable(() -> {
            log.info("Creating new customer: {}", customer.getExternalId());
            
            if (customer.getCustomerId() == null) {
                customer.setCustomerId(UUID.randomUUID().toString());
            }
            
            Customer saved = customerRepository.save(customer);
            log.info("Customer created successfully: {}", saved.getCustomerId());
            
            // TODO: Publicar evento CustomerCreatedEvent no Kafka
            
            return saved;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Busca customer por ID
     */
    @Transactional(readOnly = true)
    public Mono<Customer> getCustomerById(String customerId) {
        return Mono.fromCallable(() -> 
            customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId))
        ).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Busca customer por external ID
     */
    @Transactional(readOnly = true)
    public Mono<Customer> getCustomerByExternalId(String externalId) {
        return Mono.fromCallable(() ->
            customerRepository.findByExternalId(externalId)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + externalId))
        ).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Lista todos os customers por status
     */
    @Transactional(readOnly = true)
    public Flux<Customer> getCustomersByStatus(String status) {
        return Flux.fromIterable(customerRepository.findByStatus(status))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Lista todos os customers por segmento
     */
    @Transactional(readOnly = true)
    public Flux<Customer> getCustomersBySegment(String segment) {
        return Flux.fromIterable(customerRepository.findBySegment(segment))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Lista todos os customers com paginação
     */
    @Transactional(readOnly = true)
    public Mono<Page<Customer>> getAllCustomers(Pageable pageable) {
        return Mono.fromCallable(() -> customerRepository.findAll(pageable))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Ativa um customer
     */
    @Transactional
    public Mono<Customer> activateCustomer(String customerId, String actor) {
        return Mono.fromCallable(() -> {
            log.info("Activating customer: {}", customerId);
            
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));
            
            customer.activate(actor);
            Customer updated = customerRepository.save(customer);
            
            log.info("Customer activated: {}", customerId);
            // TODO: Publicar evento CustomerActivatedEvent no Kafka
            
            return updated;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Desativa um customer
     */
    @Transactional
    public Mono<Customer> deactivateCustomer(String customerId, String actor) {
        return Mono.fromCallable(() -> {
            log.info("Deactivating customer: {}", customerId);
            
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));
            
            customer.deactivate(actor);
            Customer updated = customerRepository.save(customer);
            
            log.info("Customer deactivated: {}", customerId);
            // TODO: Publicar evento CustomerDeactivatedEvent no Kafka
            
            return updated;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Suspende um customer
     */
    @Transactional
    public Mono<Customer> suspendCustomer(String customerId, String reason, String actor) {
        return Mono.fromCallable(() -> {
            log.info("Suspending customer: {} - Reason: {}", customerId, reason);
            
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));
            
            customer.suspend(reason, actor);
            Customer updated = customerRepository.save(customer);
            
            log.info("Customer suspended: {}", customerId);
            // TODO: Publicar evento CustomerSuspendedEvent no Kafka
            
            return updated;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Atualiza o segmento do customer
     */
    @Transactional
    public Mono<Customer> updateSegment(String customerId, String newSegment, String actor) {
        return Mono.fromCallable(() -> {
            log.info("Updating customer segment: {} to {}", customerId, newSegment);
            
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));
            
            customer.updateSegment(newSegment, actor);
            Customer updated = customerRepository.save(customer);
            
            log.info("Customer segment updated: {}", customerId);
            // TODO: Publicar evento CustomerSegmentUpdatedEvent no Kafka
            
            return updated;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Atualiza o nível de risco do customer
     */
    @Transactional
    public Mono<Customer> updateRiskLevel(String customerId, String newRiskLevel, String actor) {
        return Mono.fromCallable(() -> {
            log.info("Updating customer risk level: {} to {}", customerId, newRiskLevel);
            
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));
            
            customer.updateRiskLevel(newRiskLevel, actor);
            Customer updated = customerRepository.save(customer);
            
            log.info("Customer risk level updated: {}", customerId);
            // TODO: Publicar evento CustomerRiskLevelUpdatedEvent no Kafka
            
            return updated;
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
