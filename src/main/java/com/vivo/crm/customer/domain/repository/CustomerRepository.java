package com.vivo.crm.customer.domain.repository;

import com.vivo.crm.customer.domain.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * TMF629 - Customer Repository
 * Data access layer for Customer entities
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    Optional<Customer> findByExternalId(String externalId);

    Page<Customer> findByExternalId(String externalId, Pageable pageable);

    Optional<Customer> findByEmail(String email);

    Page<Customer> findByEmail(String email, Pageable pageable);

    Optional<Customer> findByCpfCnpj(String cpfCnpj);

    Optional<Customer> findByPartyRoleId(String partyRoleId);

    Page<Customer> findByStatus(String status, Pageable pageable);

    Page<Customer> findBySegment(String segment, Pageable pageable);

    Page<Customer> findByRiskLevel(String riskLevel, Pageable pageable);

    Page<Customer> findByPreferredChannel(String preferredChannel, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE c.status = :status AND c.segment = :segment")
    Page<Customer> findByStatusAndSegment(@Param("status") String status, @Param("segment") String segment, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Customer> searchByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE c.status = 'ACTIVE' AND c.riskLevel = 'HIGH'")
    List<Customer> findHighRiskActiveCustomers();

    @Query("SELECT COUNT(c) FROM Customer c WHERE c.status = :status")
    long countByStatus(@Param("status") String status);
}
