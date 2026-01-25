package com.vivo.crm.customer.repository;

import com.vivo.crm.customer.entity.Customer;
import com.vivo.crm.customer.entity.CustomerCase;
import com.vivo.crm.customer.entity.Interaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Long> {
    Optional<Interaction> findByInteractionId(String interactionId);
    Page<Interaction> findByCustomer(Customer customer, Pageable pageable);
    Page<Interaction> findByCustomerCase(CustomerCase customerCase, Pageable pageable);
    Page<Interaction> findByChannel(Interaction.InteractionChannel channel, Pageable pageable);
    
    @Query("SELECT i FROM Interaction i WHERE i.customer = :customer ORDER BY i.created_at DESC")
    List<Interaction> findRecentInteractionsByCustomer(@Param("customer") Customer customer, Pageable pageable);
    
    @Query("SELECT i FROM Interaction i WHERE i.customer = :customer AND i.created_at >= :since ORDER BY i.created_at DESC")
    List<Interaction> findInteractionsSince(@Param("customer") Customer customer, @Param("since") LocalDateTime since);
    
    @Query("SELECT COUNT(i) FROM Interaction i WHERE i.customer = :customer")
    Long countByCustomer(@Param("customer") Customer customer);
    
    @Query("SELECT COUNT(i) FROM Interaction i WHERE i.customer = :customer AND i.created_at >= :since")
    Long countInteractionsSince(@Param("customer") Customer customer, @Param("since") LocalDateTime since);
}
