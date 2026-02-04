package com.vivo.crm.customer.domain.repository;

import com.vivo.crm.customer.domain.model.Individual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * TMF632 - Individual Repository
 */
@Repository
public interface IndividualRepository extends JpaRepository<Individual, String> {

    /**
     * Find by identification number (CPF)
     */
    Optional<Individual> findByIdentificationNumber(String identificationNumber);

    /**
     * Find by email
     */
    Optional<Individual> findByEmail(String email);

    /**
     * Find by phone
     */
    Optional<Individual> findByPhone(String phone);

    /**
     * Find by status
     */
    List<Individual> findByStatus(String status);

    /**
     * Search by name (given name or family name)
     */
    @Query("SELECT i FROM Individual i WHERE " +
           "LOWER(i.givenName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(i.familyName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(i.formattedName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Individual> searchByName(@Param("name") String name);

    /**
     * Find by identification type and number
     */
    Optional<Individual> findByIdentificationTypeAndIdentificationNumber(
            String identificationType, String identificationNumber);

    /**
     * Count by status
     */
    long countByStatus(String status);
}
