package com.vivo.crm.customer.service;

import com.vivo.crm.customer.dto.CustomerCaseDTO;
import com.vivo.crm.customer.entity.Customer;
import com.vivo.crm.customer.entity.CustomerCase;
import com.vivo.crm.customer.repository.CustomerCaseRepository;
import com.vivo.crm.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerCaseServiceTest {

    @Mock
    private CustomerCaseRepository caseRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerCaseService caseService;

    private Customer testCustomer;
    private CustomerCase testCase;
    private CustomerCaseDTO testCaseDTO;

    @BeforeEach
    void setUp() {
        testCustomer = Customer.builder()
                .id(1L)
                .externalId("EXT-001")
                .name("John Doe")
                .email("john@example.com")
                .build();

        testCase = CustomerCase.builder()
                .id(1L)
                .caseNumber("CASE-001")
                .customer(testCustomer)
                .title("Issue with billing")
                .description("Customer reported billing issue")
                .status(CustomerCase.CaseStatus.OPEN)
                .priority(CustomerCase.CasePriority.HIGH)
                .category("Billing")
                .build();

        testCaseDTO = CustomerCaseDTO.builder()
                .id(1L)
                .caseNumber("CASE-001")
                .customerId(1L)
                .title("Issue with billing")
                .description("Customer reported billing issue")
                .status("OPEN")
                .priority("HIGH")
                .category("Billing")
                .build();
    }

    @Test
    void testCreateCase() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(caseRepository.save(any(CustomerCase.class))).thenReturn(testCase);

        CustomerCaseDTO result = caseService.createCase(1L, testCaseDTO);

        assertNotNull(result);
        assertEquals("CASE-001", result.getCaseNumber());
        verify(customerRepository, times(1)).findById(1L);
        verify(caseRepository, times(1)).save(any(CustomerCase.class));
    }

    @Test
    void testGetCaseById() {
        when(caseRepository.findById(1L)).thenReturn(Optional.of(testCase));

        CustomerCaseDTO result = caseService.getCaseById(1L);

        assertNotNull(result);
        assertEquals("CASE-001", result.getCaseNumber());
        verify(caseRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCaseByCaseNumber() {
        when(caseRepository.findByCaseNumber("CASE-001")).thenReturn(Optional.of(testCase));

        CustomerCaseDTO result = caseService.getCaseByCaseNumber("CASE-001");

        assertNotNull(result);
        assertEquals("CASE-001", result.getCaseNumber());
        verify(caseRepository, times(1)).findByCaseNumber("CASE-001");
    }

    @Test
    void testUpdateCase() {
        when(caseRepository.findById(1L)).thenReturn(Optional.of(testCase));
        when(caseRepository.save(any(CustomerCase.class))).thenReturn(testCase);

        CustomerCaseDTO updateDTO = CustomerCaseDTO.builder()
                .title("Updated Issue")
                .status("IN_PROGRESS")
                .priority("CRITICAL")
                .build();

        CustomerCaseDTO result = caseService.updateCase(1L, updateDTO);

        assertNotNull(result);
        verify(caseRepository, times(1)).findById(1L);
        verify(caseRepository, times(1)).save(any(CustomerCase.class));
    }

    @Test
    void testResolveCase() {
        when(caseRepository.findById(1L)).thenReturn(Optional.of(testCase));
        when(caseRepository.save(any(CustomerCase.class))).thenReturn(testCase);

        CustomerCaseDTO result = caseService.resolveCase(1L, "Issue resolved", 4.5);

        assertNotNull(result);
        verify(caseRepository, times(1)).findById(1L);
        verify(caseRepository, times(1)).save(any(CustomerCase.class));
    }

    @Test
    void testGetCasesByCustomer() {
        Page<CustomerCase> casePage = new PageImpl<>(java.util.List.of(testCase));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(caseRepository.findByCustomer(eq(testCustomer), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(casePage);

        Page<CustomerCaseDTO> result = caseService.getCasesByCustomer(1L, PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCasesByStatus() {
        Page<CustomerCase> casePage = new PageImpl<>(java.util.List.of(testCase));
        when(caseRepository.findByStatus(eq(CustomerCase.CaseStatus.OPEN), any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(casePage);

        Page<CustomerCaseDTO> result = caseService.getCasesByStatus("OPEN", PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}
