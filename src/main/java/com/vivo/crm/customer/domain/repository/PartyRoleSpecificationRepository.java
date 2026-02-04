package com.vivo.crm.customer.domain.repository;

import com.vivo.crm.customer.domain.model.PartyRoleSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * TMF669 - PartyRoleSpecification Repository
 */
@Repository
public interface PartyRoleSpecificationRepository extends JpaRepository<PartyRoleSpecification, String> {

    /**
     * Find by name
     */
    Optional<PartyRoleSpecification> findByName(String name);

    /**
     * Find by status
     */
    List<PartyRoleSpecification> findByStatus(String status);

    /**
     * Find active specifications
     */
    @Query("SELECT p FROM PartyRoleSpecification p WHERE p.status = 'active' " +
           "AND (p.validFrom IS NULL OR p.validFrom <= CURRENT_DATE) " +
           "AND (p.validTo IS NULL OR p.validTo >= CURRENT_DATE)")
    List<PartyRoleSpecification> findActiveSpecifications();

    /**
     * Search by name or description
     */
    @Query("SELECT p FROM PartyRoleSpecification p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<PartyRoleSpecification> searchByNameOrDescription(@Param("query") String query);

    /**
     * Count by status
     */
    long countByStatus(String status);
}
