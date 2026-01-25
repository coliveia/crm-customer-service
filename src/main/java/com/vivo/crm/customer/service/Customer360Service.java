package com.vivo.crm.customer.service;

import com.vivo.crm.customer.dto.Customer360DTO;
import com.vivo.crm.customer.entity.Customer360View;
import com.vivo.crm.customer.repository.Customer360Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for Customer360 Duality View
 * Provides consolidated customer data including:
 * - Customer identification
 * - Contracted products
 * - Financial data
 * - Support cases/tickets
 * - Interaction history
 * - Statistics
 */
@Service
@Transactional(readOnly = true)
public class Customer360Service {

    private final Customer360Repository customer360Repository;

    public Customer360Service(Customer360Repository customer360Repository) {
        this.customer360Repository = customer360Repository;
    }

    /**
     * Get complete customer 360 view by customer ID
     * @param customerId the customer ID
     * @return Customer360DTO with consolidated data
     * @throws RuntimeException if customer not found
     */
    public Customer360DTO getCustomer360ById(Long customerId) {
        Customer360View view = customer360Repository.findCustomer360ById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
        
        return view.getParsedData();
    }

    /**
     * Get complete customer 360 view by external ID
     * @param externalId the external customer ID
     * @return Customer360DTO with consolidated data
     * @throws RuntimeException if customer not found
     */
    public Customer360DTO getCustomer360ByExternalId(String externalId) {
        Customer360View view = customer360Repository.findCustomer360ByExternalId(externalId)
                .orElseThrow(() -> new RuntimeException("Customer not found with external ID: " + externalId));
        
        return view.getParsedData();
    }

    /**
     * Get all customers 360 views by status
     * @param status the customer status
     * @param pageable pagination information
     * @return Page of Customer360DTO
     */
    public Page<Customer360DTO> getCustomer360ByStatus(String status, Pageable pageable) {
        Page<Customer360View> views = customer360Repository.findCustomer360ByStatus(status, pageable);
        return views.map(Customer360View::getParsedData);
    }

    /**
     * Get all customers 360 views by segment
     * @param segment the customer segment
     * @param pageable pagination information
     * @return Page of Customer360DTO
     */
    public Page<Customer360DTO> getCustomer360BySegment(String segment, Pageable pageable) {
        Page<Customer360View> views = customer360Repository.findCustomer360BySegment(segment, pageable);
        return views.map(Customer360View::getParsedData);
    }

    /**
     * Get all customers 360 views
     * @param pageable pagination information
     * @return Page of Customer360DTO
     */
    public Page<Customer360DTO> getAllCustomer360(Pageable pageable) {
        Page<Customer360View> views = customer360Repository.findAllCustomer360(pageable);
        return views.map(Customer360View::getParsedData);
    }

    /**
     * Get customer identification data
     * @param customerId the customer ID
     * @return Customer360DTO with identification data
     */
    public Customer360DTO getCustomerIdentification(Long customerId) {
        return getCustomer360ById(customerId);
    }

    /**
     * Get customer products
     * @param customerId the customer ID
     * @return Customer360DTO with products data
     */
    public Customer360DTO getCustomerProducts(Long customerId) {
        return getCustomer360ById(customerId);
    }

    /**
     * Get customer financials
     * @param customerId the customer ID
     * @return Customer360DTO with financials data
     */
    public Customer360DTO getCustomerFinancials(Long customerId) {
        return getCustomer360ById(customerId);
    }

    /**
     * Get customer cases/tickets
     * @param customerId the customer ID
     * @return Customer360DTO with cases data
     */
    public Customer360DTO getCustomerCases(Long customerId) {
        return getCustomer360ById(customerId);
    }

    /**
     * Get customer interactions
     * @param customerId the customer ID
     * @return Customer360DTO with interactions data
     */
    public Customer360DTO getCustomerInteractions(Long customerId) {
        return getCustomer360ById(customerId);
    }

    /**
     * Get customer statistics
     * @param customerId the customer ID
     * @return Customer360DTO with statistics data
     */
    public Customer360DTO getCustomerStatistics(Long customerId) {
        return getCustomer360ById(customerId);
    }
}
