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
 * TMF629 - Customer Service (Reactive)
 * Business logic for customer management using Reactive streams
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerServiceReactive {

    private final CustomerRepository customerRepository;

    /**
     * Create a new customer (Reactive)
     */
    public Mono<Customer> createCustomer(Customer customer) {
        return Mono.fromCallable(() -> {
            log.info("Creating customer: {}", customer.getName());
            
            if (customer.getCustomerId() == null) {
                customer.setCustomerId(UUID.randomUUID().toString());
            }
            if (customer.getPartyRoleId() == null) {
                customer.setPartyRoleId(UUID.randomUUID().toString());
            }
            if (customer.getStatus() == null) {
                customer.setStatus("ACTIVE");
            }
            
            Customer saved = customerRepository.save(customer);
            log.info("Customer created successfully: {}", saved.getCustomerId());
            
            // TODO: Publicar evento CustomerCreatedEvent no Kafka
            
            return saved;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Get customer by ID (Reactive)
     */
    @Transactional(readOnly = true)
    public Mono<Customer> getCustomerById(String customerId) {
        return Mono.fromCallable(() -> {
            log.debug("Getting customer by ID: {}", customerId);
            return customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));
        }).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Get customer by external ID (Reactive)
     */
    @Transactional(readOnly = true)
    public Mono<Customer> getCustomerByExternalId(String externalId) {
        return Mono.fromCallable(() -> {
            log.debug("Getting customer by external ID: {}", externalId);
            return customerRepository.findByExternalId(externalId)
                    .orElseThrow(() -> new RuntimeException("Customer not found: " + externalId));
        }).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Get customer by email (Reactive)
     */
    @Transactional(readOnly = true)
    public Mono<Customer> getCustomerByEmail(String email) {
        return Mono.fromCallable(() -> {
            log.debug("Getting customer by email: {}", email);
            return customerRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Customer not found: " + email));
        }).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Get customer by CPF/CNPJ (Reactive)
     */
    @Transactional(readOnly = true)
    public Mono<Customer> getCustomerByCpfCnpj(String cpfCnpj) {
        return Mono.fromCallable(() -> {
            log.debug("Getting customer by CPF/CNPJ: {}", cpfCnpj);
            return customerRepository.findByCpfCnpj(cpfCnpj)
                    .orElseThrow(() -> new RuntimeException("Customer not found: " + cpfCnpj));
        }).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Get customer by party role ID (Reactive)
     */
    @Transactional(readOnly = true)
    public Mono<Customer> getCustomerByPartyRoleId(String partyRoleId) {
        return Mono.fromCallable(() -> {
            log.debug("Getting customer by party role ID: {}", partyRoleId);
            return customerRepository.findByPartyRoleId(partyRoleId)
                    .orElseThrow(() -> new RuntimeException("Customer not found: " + partyRoleId));
        }).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Get all customers with pagination (Reactive)
     */
    @Transactional(readOnly = true)
    public Mono<Page<Customer>> getAllCustomers(Pageable pageable) {
        return Mono.fromCallable(() -> {
            log.debug("Getting all customers with pagination");
            return customerRepository.findAll(pageable);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Search customers by name (Reactive)
     */
    @Transactional(readOnly = true)
    public Flux<Customer> searchCustomersByName(String name) {
        return Mono.fromCallable(() -> {
            log.debug("Searching customers by name: {}", name);
            return customerRepository.searchByName(name, Pageable.unpaged()).getContent();
        }).flatMapMany(Flux::fromIterable).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Get customers by status (Reactive)
     */
    @Transactional(readOnly = true)
    public Flux<Customer> getCustomersByStatus(String status) {
        return Mono.fromCallable(() -> {
            log.debug("Getting customers by status: {}", status);
            return customerRepository.findByStatus(status, Pageable.unpaged()).getContent();
        }).flatMapMany(Flux::fromIterable).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Get customers by segment (Reactive)
     */
    @Transactional(readOnly = true)
    public Flux<Customer> getCustomersBySegment(String segment) {
        return Mono.fromCallable(() -> {
            log.debug("Getting customers by segment: {}", segment);
            return customerRepository.findBySegment(segment, Pageable.unpaged()).getContent();
        }).flatMapMany(Flux::fromIterable).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Get customers by risk level (Reactive)
     */
    @Transactional(readOnly = true)
    public Flux<Customer> getCustomersByRiskLevel(String riskLevel) {
        return Mono.fromCallable(() -> {
            log.debug("Getting customers by risk level: {}", riskLevel);
            return customerRepository.findByRiskLevel(riskLevel, Pageable.unpaged()).getContent();
        }).flatMapMany(Flux::fromIterable).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Get customers by status and segment (Reactive)
     */
    @Transactional(readOnly = true)
    public Mono<Page<Customer>> getCustomersByStatusAndSegment(String status, String segment, Pageable pageable) {
        return Mono.fromCallable(() -> {
            log.debug("Getting customers by status: {} and segment: {}", status, segment);
            return customerRepository.findByStatusAndSegment(status, segment, pageable);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Get high risk active customers (Reactive)
     */
    @Transactional(readOnly = true)
    public Flux<Customer> getHighRiskActiveCustomers() {
        return Mono.fromCallable(() -> {
            log.debug("Getting high risk active customers");
            return customerRepository.findHighRiskActiveCustomers();
        }).flatMapMany(Flux::fromIterable).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Update customer (Reactive)
     */
    public Mono<Customer> updateCustomer(Customer customer) {
        return Mono.fromCallable(() -> {
            log.info("Updating customer: {}", customer.getCustomerId());
            Customer updated = customerRepository.save(customer);
            
            // TODO: Publicar evento CustomerUpdatedEvent no Kafka
            
            return updated;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Activate customer (Reactive)
     */
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
     * Deactivate customer (Reactive)
     */
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
     * Suspend customer (Reactive)
     */
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
     * Delete customer (Reactive)
     */
    public Mono<Void> deleteCustomer(String customerId) {
        return Mono.fromCallable(() -> {
            log.info("Deleting customer: {}", customerId);
            customerRepository.deleteById(customerId);
            
            // TODO: Publicar evento CustomerDeletedEvent no Kafka
            return null;
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    /**
     * Count customers by status (Reactive)
     */
    @Transactional(readOnly = true)
    public Mono<Long> countCustomersByStatus(String status) {
        return Mono.fromCallable(() -> {
            log.debug("Counting customers by status: {}", status);
            return customerRepository.countByStatus(status);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
