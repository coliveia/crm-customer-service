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
 * Repository para operações de persistência de Customers
 * Acessa dados através de Duality Views do Oracle
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    /**
     * Busca customer por external ID
     */
    Optional<Customer> findByExternalId(String externalId);

    /**
     * Busca customer por email
     */
    Optional<Customer> findByEmail(String email);

    /**
     * Busca customer por CPF
     */
    Optional<Customer> findByCpf(String cpf);

    /**
     * Busca todos os customers por status
     */
    List<Customer> findByStatus(String status);

    /**
     * Busca todos os customers por segmento
     */
    List<Customer> findBySegment(String segment);

    /**
     * Busca customers com paginação por status
     */
    Page<Customer> findByStatus(String status, Pageable pageable);

    /**
     * Busca customers com paginação por segmento
     */
    Page<Customer> findBySegment(String segment, Pageable pageable);

    /**
     * Busca customers com paginação por nível de risco
     */
    Page<Customer> findByRiskLevel(String riskLevel, Pageable pageable);

    /**
     * Busca customers suspensos
     */
    @Query("SELECT c FROM Customer c WHERE c.status = 'SUSPENDED'")
    List<Customer> findSuspendedCustomers();

    /**
     * Conta customers por status
     */
    long countByStatus(String status);

    /**
     * Conta customers por segmento
     */
    long countBySegment(String segment);
}
