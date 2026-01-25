package com.vivo.crm.customer.repository;

import com.vivo.crm.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByExternalId(String externalId);
    Optional<Customer> findByEmail(String email);
    Page<Customer> findByStatus(Customer.CustomerStatus status, Pageable pageable);
    Page<Customer> findBySegment(String segment, Pageable pageable);
    List<Customer> findByStatusAndSegment(Customer.CustomerStatus status, String segment);
    
    @Query("SELECT c FROM Customer c WHERE c.status = :status ORDER BY c.last_interaction DESC")
    Page<Customer> findByStatusOrderByLastInteraction(
            @Param("status") Customer.CustomerStatus status, 
            Pageable pageable);
}
