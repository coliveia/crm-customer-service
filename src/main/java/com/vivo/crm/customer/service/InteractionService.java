package com.vivo.crm.customer.service;

import com.vivo.crm.customer.dto.InteractionDTO;
import com.vivo.crm.customer.entity.Customer;
import com.vivo.crm.customer.entity.CustomerCase;
import com.vivo.crm.customer.entity.Interaction;
import com.vivo.crm.customer.repository.CustomerCaseRepository;
import com.vivo.crm.customer.repository.CustomerRepository;
import com.vivo.crm.customer.repository.InteractionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class InteractionService {

    private final InteractionRepository interactionRepository;
    private final CustomerRepository customerRepository;
    private final CustomerCaseRepository caseRepository;

    public InteractionService(InteractionRepository interactionRepository,
                             CustomerRepository customerRepository,
                             CustomerCaseRepository caseRepository) {
        this.interactionRepository = interactionRepository;
        this.customerRepository = customerRepository;
        this.caseRepository = caseRepository;
    }

    public InteractionDTO createInteraction(Long customerId, Long caseId, InteractionDTO interactionDTO) {
        log.info("Creating interaction for customer: {} and case: {}", customerId, caseId);
        
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
        
        Interaction interaction = interactionDTO.toEntity();
        interaction.setCustomer(customer);
        interaction.setInteractionId(generateInteractionId());
        
        if (caseId != null) {
            CustomerCase customerCase = caseRepository.findById(caseId)
                    .orElseThrow(() -> new RuntimeException("Case not found with ID: " + caseId));
            interaction.setCustomerCase(customerCase);
        }
        
        // Update customer's last interaction timestamp
        customer.setLast_interaction(LocalDateTime.now());
        customerRepository.save(customer);
        
        Interaction saved = interactionRepository.save(interaction);
        log.info("Interaction created with ID: {}", saved.getInteractionId());
        
        return InteractionDTO.fromEntity(saved);
    }

    public InteractionDTO getInteractionById(Long id) {
        log.info("Fetching interaction with ID: {}", id);
        
        Interaction interaction = interactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interaction not found with ID: " + id));
        
        return InteractionDTO.fromEntity(interaction);
    }

    public InteractionDTO getInteractionByInteractionId(String interactionId) {
        log.info("Fetching interaction with ID: {}", interactionId);
        
        Interaction interaction = interactionRepository.findByInteractionId(interactionId)
                .orElseThrow(() -> new RuntimeException("Interaction not found with ID: " + interactionId));
        
        return InteractionDTO.fromEntity(interaction);
    }

    public InteractionDTO updateInteraction(Long id, InteractionDTO interactionDTO) {
        log.info("Updating interaction with ID: {}", id);
        
        Interaction interaction = interactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interaction not found with ID: " + id));
        
        interaction.setMessage(interactionDTO.getMessage());
        interaction.setSentiment(interactionDTO.getSentiment());
        interaction.setSentiment_score(interactionDTO.getSentimentScore());
        interaction.setNotes(interactionDTO.getNotes());
        
        Interaction updated = interactionRepository.save(interaction);
        log.info("Interaction updated with ID: {}", id);
        
        return InteractionDTO.fromEntity(updated);
    }

    public Page<InteractionDTO> getInteractionsByCustomer(Long customerId, Pageable pageable) {
        log.info("Fetching interactions for customer: {}", customerId);
        
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
        
        return interactionRepository.findByCustomer(customer, pageable)
                .map(InteractionDTO::fromEntity);
    }

    public Page<InteractionDTO> getInteractionsByCase(Long caseId, Pageable pageable) {
        log.info("Fetching interactions for case: {}", caseId);
        
        CustomerCase customerCase = caseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found with ID: " + caseId));
        
        return interactionRepository.findByCustomerCase(customerCase, pageable)
                .map(InteractionDTO::fromEntity);
    }

    public Page<InteractionDTO> getInteractionsByChannel(String channel, Pageable pageable) {
        log.info("Fetching interactions by channel: {}", channel);
        Interaction.InteractionChannel interactionChannel = Interaction.InteractionChannel.valueOf(channel);
        return interactionRepository.findByChannel(interactionChannel, pageable)
                .map(InteractionDTO::fromEntity);
    }

    private String generateInteractionId() {
        return "INT-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
