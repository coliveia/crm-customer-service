package com.vivo.crm.customer.domain.repository;

import com.vivo.crm.customer.domain.model.Hub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TMF - Hub Repository
 * For event subscriptions
 */
@Repository
public interface HubRepository extends JpaRepository<Hub, String> {

    /**
     * Find by callback URL
     */
    List<Hub> findByCallback(String callback);

    /**
     * Find active subscriptions
     */
    List<Hub> findByStatus(String status);

    /**
     * Find active subscriptions
     */
    default List<Hub> findActiveSubscriptions() {
        return findByStatus("active");
    }
}
