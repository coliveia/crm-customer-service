package com.vivo.crm.customer.domain.repository;

import com.vivo.crm.customer.domain.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * TMF632 - Organization Repository
 */
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, String> {

    /**
     * Find by identification number (CNPJ)
     */
    Optional<Organization> findByIdentificationNumber(String identificationNumber);

    /**
     * Find by trading name
     */
    Optional<Organization> findByTradingName(String tradingName);

    /**
     * Find by email
     */
    Optional<Organization> findByEmail(String email);

    /**
     * Find by status
     */
    List<Organization> findByStatus(String status);

    /**
     * Find by organization type
     */
    List<Organization> findByOrganizationType(String organizationType);

    /**
     * Search by name or trading name
     */
    @Query("SELECT o FROM Organization o WHERE " +
           "LOWER(o.name) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(o.tradingName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Organization> searchByName(@Param("name") String name);

    /**
     * Find by identification type and number
     */
    Optional<Organization> findByIdentificationTypeAndIdentificationNumber(
            String identificationType, String identificationNumber);

    /**
     * Find child organizations
     */
    List<Organization> findByParentOrganizationId(String parentOrganizationId);

    /**
     * Count by status
     */
    long countByStatus(String status);

    /**
     * Find legal entities only
     */
    List<Organization> findByIsLegalEntityTrue();
}
