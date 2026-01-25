package com.vivo.crm.customer.service;

import com.vivo.crm.customer.dto.CustomerCaseDTO;
import com.vivo.crm.customer.entity.Customer;
import com.vivo.crm.customer.entity.CustomerCase;
import com.vivo.crm.customer.repository.CustomerCaseRepository;
import com.vivo.crm.customer.repository.CustomerRepository;
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
public class CustomerCaseService {

    private final CustomerCaseRepository caseRepository;
    private final CustomerRepository customerRepository;

    public CustomerCaseService(CustomerCaseRepository caseRepository,
                              CustomerRepository customerRepository) {
        this.caseRepository = caseRepository;
        this.customerRepository = customerRepository;
    }

    public CustomerCaseDTO createCase(Long customerId, CustomerCaseDTO caseDTO) {
        log.info("Creating case for customer: {}", customerId);
        
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
        
        CustomerCase customerCase = caseDTO.toEntity();
        customerCase.setCustomer(customer);
        customerCase.setCaseNumber(generateCaseNumber());
        
        CustomerCase saved = caseRepository.save(customerCase);
        log.info("Case created with number: {}", saved.getCaseNumber());
        
        return CustomerCaseDTO.fromEntity(saved);
    }

    public CustomerCaseDTO getCaseById(Long id) {
        log.info("Fetching case with ID: {}", id);
        
        CustomerCase customerCase = caseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Case not found with ID: " + id));
        
        return CustomerCaseDTO.fromEntity(customerCase);
    }

    public CustomerCaseDTO getCaseByCaseNumber(String caseNumber) {
        log.info("Fetching case with number: {}", caseNumber);
        
        CustomerCase customerCase = caseRepository.findByCaseNumber(caseNumber)
                .orElseThrow(() -> new RuntimeException("Case not found with number: " + caseNumber));
        
        return CustomerCaseDTO.fromEntity(customerCase);
    }

    public CustomerCaseDTO updateCase(Long id, CustomerCaseDTO caseDTO) {
        log.info("Updating case with ID: {}", id);
        
        CustomerCase customerCase = caseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Case not found with ID: " + id));
        
        customerCase.setTitle(caseDTO.getTitle());
        customerCase.setDescription(caseDTO.getDescription());
        customerCase.setStatus(CustomerCase.CaseStatus.valueOf(caseDTO.getStatus()));
        customerCase.setPriority(CustomerCase.CasePriority.valueOf(caseDTO.getPriority()));
        customerCase.setCategory(caseDTO.getCategory());
        customerCase.setSubcategory(caseDTO.getSubcategory());
        customerCase.setAssignedTo(caseDTO.getAssignedTo());
        
        CustomerCase updated = caseRepository.save(customerCase);
        log.info("Case updated with ID: {}", id);
        
        return CustomerCaseDTO.fromEntity(updated);
    }

    public CustomerCaseDTO resolveCase(Long id, String resolutionNotes, Double satisfactionScore) {
        log.info("Resolving case with ID: {}", id);
        
        CustomerCase customerCase = caseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Case not found with ID: " + id));
        
        customerCase.setStatus(CustomerCase.CaseStatus.RESOLVED);
        customerCase.setResolution_notes(resolutionNotes);
        customerCase.setSatisfaction_score(satisfactionScore);
        customerCase.setResolved_at(LocalDateTime.now());
        
        // Calculate resolution time
        if (customerCase.getCreated_at() != null) {
            long minutes = java.time.temporal.ChronoUnit.MINUTES.between(
                    customerCase.getCreated_at(), 
                    customerCase.getResolved_at()
            );
            customerCase.setResolution_time_minutes((int) minutes);
        }
        
        CustomerCase updated = caseRepository.save(customerCase);
        log.info("Case resolved with ID: {}", id);
        
        return CustomerCaseDTO.fromEntity(updated);
    }

    public Page<CustomerCaseDTO> getCasesByCustomer(Long customerId, Pageable pageable) {
        log.info("Fetching cases for customer: {}", customerId);
        
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
        
        return caseRepository.findByCustomer(customer, pageable)
                .map(CustomerCaseDTO::fromEntity);
    }

    public Page<CustomerCaseDTO> getCasesByStatus(String status, Pageable pageable) {
        log.info("Fetching cases with status: {}", status);
        CustomerCase.CaseStatus caseStatus = CustomerCase.CaseStatus.valueOf(status);
        return caseRepository.findByStatus(caseStatus, pageable)
                .map(CustomerCaseDTO::fromEntity);
    }

    public Page<CustomerCaseDTO> getCasesByPriority(String priority, Pageable pageable) {
        log.info("Fetching cases with priority: {}", priority);
        CustomerCase.CasePriority casePriority = CustomerCase.CasePriority.valueOf(priority);
        return caseRepository.findByPriority(casePriority, pageable)
                .map(CustomerCaseDTO::fromEntity);
    }

    private String generateCaseNumber() {
        return "CASE-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
