package com.vivo.crm.customer.repository;

import com.vivo.crm.customer.entity.CustomerCase;
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
public interface CustomerCaseRepository extends JpaRepository<CustomerCase, Long> {
    Optional<CustomerCase> findByCaseNumber(String caseNumber);
    Page<CustomerCase> findByCustomer(Customer customer, Pageable pageable);
    Page<CustomerCase> findByCustomerAndStatus(Customer customer, CustomerCase.CaseStatus status, Pageable pageable);
    Page<CustomerCase> findByStatus(CustomerCase.CaseStatus status, Pageable pageable);
    Page<CustomerCase> findByPriority(CustomerCase.CasePriority priority, Pageable pageable);
    List<CustomerCase> findByCustomerAndStatus(Customer customer, CustomerCase.CaseStatus status);
    
    @Query("SELECT c FROM CustomerCase c WHERE c.customer = :customer AND c.status IN ('OPEN', 'IN_PROGRESS', 'WAITING_CUSTOMER')")
    List<CustomerCase> findOpenCasesByCustomer(@Param("customer") Customer customer);
    
    @Query("SELECT COUNT(c) FROM CustomerCase c WHERE c.customer = :customer AND c.status = :status")
    Long countByCustomerAndStatus(@Param("customer") Customer customer, @Param("status") CustomerCase.CaseStatus status);
}
