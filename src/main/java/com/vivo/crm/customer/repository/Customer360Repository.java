package com.vivo.crm.customer.repository;

import com.vivo.crm.customer.entity.Customer360View;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Customer360 Duality View
 * Provides read-only access to consolidated customer data
 * All queries are executed against the Oracle Duality View
 */
@Repository
public interface Customer360Repository extends JpaRepository<Customer360View, Long> {
    
    /**
     * Find customer 360 view by customer ID
     * @param customerId the customer ID
     * @return Optional containing Customer360View if found
     */
    @Query(value = "SELECT * FROM customer_360_dv WHERE customer_id = :customerId", 
           nativeQuery = true)
    Optional<Customer360View> findCustomer360ById(@Param("customerId") Long customerId);
    
    /**
     * Find customer 360 view by external ID
     * @param externalId the external customer ID
     * @return Optional containing Customer360View if found
     */
    @Query(value = "SELECT * FROM customer_360_dv WHERE external_id = :externalId", 
           nativeQuery = true)
    Optional<Customer360View> findCustomer360ByExternalId(@Param("externalId") String externalId);
    
    /**
     * Find all customers 360 views by status
     * @param status the customer status (ACTIVE, INACTIVE, SUSPENDED, PROSPECT)
     * @param pageable pagination information
     * @return Page of Customer360View
     */
    @Query(value = "SELECT * FROM customer_360_dv WHERE status = :status", 
           nativeQuery = true)
    Page<Customer360View> findCustomer360ByStatus(@Param("status") String status, Pageable pageable);
    
    /**
     * Find all customers 360 views by segment
     * @param segment the customer segment
     * @param pageable pagination information
     * @return Page of Customer360View
     */
    @Query(value = "SELECT * FROM customer_360_dv WHERE segment = :segment", 
           nativeQuery = true)
    Page<Customer360View> findCustomer360BySegment(@Param("segment") String segment, Pageable pageable);
    
    /**
     * Find all customers 360 views
     * @param pageable pagination information
     * @return Page of Customer360View
     */
    @Query(value = "SELECT * FROM customer_360_dv", 
           nativeQuery = true)
    Page<Customer360View> findAllCustomer360(Pageable pageable);
}
