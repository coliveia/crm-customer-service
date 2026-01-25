package com.vivo.crm.customer.service;

import com.vivo.crm.customer.dto.*;
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
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerCaseRepository caseRepository;
    private final InteractionRepository interactionRepository;

    public CustomerService(CustomerRepository customerRepository,
                          CustomerCaseRepository caseRepository,
                          InteractionRepository interactionRepository) {
        this.customerRepository = customerRepository;
        this.caseRepository = caseRepository;
        this.interactionRepository = interactionRepository;
    }

    // Customer Management
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        log.info("Creating customer: {}", customerDTO.getExternalId());
        
        Customer customer = customerDTO.toEntity();
        Customer saved = customerRepository.save(customer);
        
        log.info("Customer created with ID: {}", saved.getId());
        return CustomerDTO.fromEntity(saved);
    }

    public CustomerDTO getCustomerById(Long id) {
        log.info("Fetching customer with ID: {}", id);
        
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));
        
        return CustomerDTO.fromEntity(customer);
    }

    public CustomerDTO getCustomerByExternalId(String externalId) {
        log.info("Fetching customer with external ID: {}", externalId);
        
        Customer customer = customerRepository.findByExternalId(externalId)
                .orElseThrow(() -> new RuntimeException("Customer not found with external ID: " + externalId));
        
        return CustomerDTO.fromEntity(customer);
    }

    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        log.info("Updating customer with ID: {}", id);
        
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));
        
        customer.setName(customerDTO.getName());
        customer.setPhone(customerDTO.getPhone());
        customer.setSegment(customerDTO.getSegment());
        customer.setPreferredChannel(customerDTO.getPreferredChannel());
        customer.setAddress(customerDTO.getAddress());
        customer.setCity(customerDTO.getCity());
        customer.setState(customerDTO.getState());
        customer.setZipCode(customerDTO.getZipCode());
        
        Customer updated = customerRepository.save(customer);
        log.info("Customer updated with ID: {}", id);
        
        return CustomerDTO.fromEntity(updated);
    }

    public Page<CustomerDTO> getAllCustomers(Pageable pageable) {
        log.info("Fetching all customers");
        return customerRepository.findAll(pageable)
                .map(CustomerDTO::fromEntity);
    }

    public Page<CustomerDTO> getCustomersByStatus(String status, Pageable pageable) {
        log.info("Fetching customers with status: {}", status);
        Customer.CustomerStatus customerStatus = Customer.CustomerStatus.valueOf(status);
        return customerRepository.findByStatus(customerStatus, pageable)
                .map(CustomerDTO::fromEntity);
    }

    // Customer View 360
    public CustomerView360DTO getCustomerView360(Long customerId) {
        log.info("Fetching 360 view for customer: {}", customerId);
        
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
        
        // Get open cases
        List<CustomerCase> openCases = caseRepository.findOpenCasesByCustomer(customer);
        List<CustomerCaseDTO> openCasesDTO = openCases.stream()
                .map(CustomerCaseDTO::fromEntity)
                .collect(Collectors.toList());
        
        // Get recent interactions
        List<Interaction> recentInteractions = interactionRepository
                .findRecentInteractionsByCustomer(customer, Pageable.ofSize(10));
        List<InteractionDTO> recentInteractionsDTO = recentInteractions.stream()
                .map(InteractionDTO::fromEntity)
                .collect(Collectors.toList());
        
        // Calculate statistics
        CustomerStatisticsDTO statistics = calculateCustomerStatistics(customer);
        
        // Determine risk level
        String riskLevel = determineRiskLevel(customer, statistics);
        
        // Get next recommended action
        String nextAction = getNextRecommendedAction(customer, statistics);
        
        return CustomerView360DTO.builder()
                .customer(CustomerDTO.fromEntity(customer))
                .openCases(openCasesDTO)
                .recentInteractions(recentInteractionsDTO)
                .statistics(statistics)
                .riskLevel(riskLevel)
                .nextRecommendedAction(nextAction)
                .build();
    }

    private CustomerStatisticsDTO calculateCustomerStatistics(Customer customer) {
        Long totalCases = (long) customer.getCases().size();
        Long openCases = caseRepository.countByCustomerAndStatus(customer, CustomerCase.CaseStatus.OPEN);
        Long resolvedCases = caseRepository.countByCustomerAndStatus(customer, CustomerCase.CaseStatus.RESOLVED);
        
        Double avgSatisfaction = customer.getCases().stream()
                .filter(c -> c.getSatisfaction_score() != null)
                .mapToDouble(CustomerCase::getSatisfaction_score)
                .average()
                .orElse(0.0);
        
        Long totalInteractions = interactionRepository.countByCustomer(customer);
        LocalDateTime monthAgo = LocalDateTime.now().minusMonths(1);
        Long interactionsThisMonth = interactionRepository.countInteractionsSince(customer, monthAgo);
        
        Double avgResolutionTime = customer.getCases().stream()
                .filter(c -> c.getResolution_time_minutes() != null)
                .mapToDouble(CustomerCase::getResolution_time_minutes)
                .average()
                .orElse(0.0);
        
        return CustomerStatisticsDTO.builder()
                .totalCases(totalCases.intValue())
                .openCases(openCases.intValue())
                .resolvedCases(resolvedCases.intValue())
                .averageSatisfactionScore(avgSatisfaction)
                .totalInteractions(totalInteractions.intValue())
                .interactionsThisMonth(interactionsThisMonth.intValue())
                .averageResolutionTimeMinutes(avgResolutionTime)
                .build();
    }

    private String determineRiskLevel(Customer customer, CustomerStatisticsDTO statistics) {
        // Risk level based on multiple factors
        if (statistics.getOpenCases() > 3 || statistics.getAverageSatisfactionScore() < 2.0) {
            return "HIGH";
        } else if (statistics.getOpenCases() > 1 || statistics.getAverageSatisfactionScore() < 3.0) {
            return "MEDIUM";
        }
        return "LOW";
    }

    private String getNextRecommendedAction(Customer customer, CustomerStatisticsDTO statistics) {
        if (statistics.getOpenCases() > 0) {
            return "Follow up on open cases";
        } else if (statistics.getAverageSatisfactionScore() < 3.0) {
            return "Conduct satisfaction survey";
        } else if (customer.getLast_interaction() != null && 
                   customer.getLast_interaction().isBefore(LocalDateTime.now().minusDays(30))) {
            return "Proactive outreach";
        }
        return "Maintain relationship";
    }
}
