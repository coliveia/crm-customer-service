package com.vivo.crm.customer.service;

import com.vivo.crm.customer.dto.CustomerDTO;
import com.vivo.crm.customer.dto.CustomerView360DTO;
import com.vivo.crm.customer.entity.Customer;
import com.vivo.crm.customer.repository.CustomerCaseRepository;
import com.vivo.crm.customer.repository.CustomerRepository;
import com.vivo.crm.customer.repository.InteractionRepository;
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
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerCaseRepository caseRepository;

    @Mock
    private InteractionRepository interactionRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;
    private CustomerDTO testCustomerDTO;

    @BeforeEach
    void setUp() {
        testCustomer = Customer.builder()
                .id(1L)
                .externalId("EXT-001")
                .name("John Doe")
                .email("john@example.com")
                .phone("11999999999")
                .status(Customer.CustomerStatus.ACTIVE)
                .segment("Premium")
                .cases(new ArrayList<>())
                .interactions(new ArrayList<>())
                .build();

        testCustomerDTO = CustomerDTO.builder()
                .id(1L)
                .externalId("EXT-001")
                .name("John Doe")
                .email("john@example.com")
                .phone("11999999999")
                .status("ACTIVE")
                .segment("Premium")
                .build();
    }

    @Test
    void testCreateCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        CustomerDTO result = customerService.createCustomer(testCustomerDTO);

        assertNotNull(result);
        assertEquals("EXT-001", result.getExternalId());
        assertEquals("John Doe", result.getName());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testGetCustomerById() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));

        CustomerDTO result = customerService.getCustomerById(1L);

        assertNotNull(result);
        assertEquals("EXT-001", result.getExternalId());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCustomerByIdNotFound() {
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> customerService.getCustomerById(999L));
    }

    @Test
    void testGetCustomerByExternalId() {
        when(customerRepository.findByExternalId("EXT-001")).thenReturn(Optional.of(testCustomer));

        CustomerDTO result = customerService.getCustomerByExternalId("EXT-001");

        assertNotNull(result);
        assertEquals("EXT-001", result.getExternalId());
        verify(customerRepository, times(1)).findByExternalId("EXT-001");
    }

    @Test
    void testUpdateCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        CustomerDTO updateDTO = CustomerDTO.builder()
                .name("Jane Doe")
                .phone("11988888888")
                .build();

        CustomerDTO result = customerService.updateCustomer(1L, updateDTO);

        assertNotNull(result);
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testGetAllCustomers() {
        Page<Customer> customerPage = new PageImpl<>(java.util.List.of(testCustomer));
        when(customerRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(customerPage);

        Page<CustomerDTO> result = customerService.getAllCustomers(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(customerRepository, times(1)).findAll(any(org.springframework.data.domain.Pageable.class));
    }

    @Test
    void testGetCustomerView360() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(caseRepository.findOpenCasesByCustomer(testCustomer)).thenReturn(new ArrayList<>());
        when(interactionRepository.findRecentInteractionsByCustomer(eq(testCustomer), any()))
                .thenReturn(new ArrayList<>());
        when(caseRepository.countByCustomerAndStatus(eq(testCustomer), any())).thenReturn(0L);
        when(interactionRepository.countByCustomer(testCustomer)).thenReturn(0L);
        when(interactionRepository.countInteractionsSince(eq(testCustomer), any())).thenReturn(0L);

        CustomerView360DTO result = customerService.getCustomerView360(1L);

        assertNotNull(result);
        assertNotNull(result.getCustomer());
        assertNotNull(result.getStatistics());
        assertNotNull(result.getRiskLevel());
        verify(customerRepository, times(1)).findById(1L);
    }
}
