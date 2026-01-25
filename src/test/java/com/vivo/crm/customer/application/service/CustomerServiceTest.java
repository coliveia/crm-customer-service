package com.vivo.crm.customer.application.service;

import com.vivo.crm.customer.domain.model.Customer;
import com.vivo.crm.customer.domain.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = Customer.builder()
                .customerId(UUID.randomUUID().toString())
                .externalId("EXT-001")
                .name("John Doe")
                .email("john@example.com")
                .phone("11999999999")
                .cpf("12345678900")
                .status("ACTIVE")
                .segment("Premium")
                .riskLevel("LOW")
                .build();
    }

    @Test
    void testCreateCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        StepVerifier.create(customerService.createCustomer(testCustomer))
                .expectNext(testCustomer)
                .verifyComplete();

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testGetCustomerById() {
        when(customerRepository.findById(testCustomer.getCustomerId()))
                .thenReturn(Optional.of(testCustomer));

        StepVerifier.create(customerService.getCustomerById(testCustomer.getCustomerId()))
                .expectNext(testCustomer)
                .verifyComplete();

        verify(customerRepository, times(1)).findById(testCustomer.getCustomerId());
    }

    @Test
    void testGetCustomerByExternalId() {
        when(customerRepository.findByExternalId("EXT-001"))
                .thenReturn(Optional.of(testCustomer));

        StepVerifier.create(customerService.getCustomerByExternalId("EXT-001"))
                .expectNext(testCustomer)
                .verifyComplete();

        verify(customerRepository, times(1)).findByExternalId("EXT-001");
    }

    @Test
    void testActivateCustomer() {
        when(customerRepository.findById(testCustomer.getCustomerId()))
                .thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        StepVerifier.create(customerService.activateCustomer(testCustomer.getCustomerId(), "admin"))
                .expectNext(testCustomer)
                .verifyComplete();

        verify(customerRepository, times(1)).findById(testCustomer.getCustomerId());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testDeactivateCustomer() {
        when(customerRepository.findById(testCustomer.getCustomerId()))
                .thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        StepVerifier.create(customerService.deactivateCustomer(testCustomer.getCustomerId(), "admin"))
                .expectNext(testCustomer)
                .verifyComplete();

        verify(customerRepository, times(1)).findById(testCustomer.getCustomerId());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testSuspendCustomer() {
        when(customerRepository.findById(testCustomer.getCustomerId()))
                .thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        StepVerifier.create(customerService.suspendCustomer(testCustomer.getCustomerId(), "Fraud", "admin"))
                .expectNext(testCustomer)
                .verifyComplete();

        verify(customerRepository, times(1)).findById(testCustomer.getCustomerId());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testUpdateSegment() {
        when(customerRepository.findById(testCustomer.getCustomerId()))
                .thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        StepVerifier.create(customerService.updateSegment(testCustomer.getCustomerId(), "Gold", "admin"))
                .expectNext(testCustomer)
                .verifyComplete();

        verify(customerRepository, times(1)).findById(testCustomer.getCustomerId());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testUpdateRiskLevel() {
        when(customerRepository.findById(testCustomer.getCustomerId()))
                .thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        StepVerifier.create(customerService.updateRiskLevel(testCustomer.getCustomerId(), "HIGH", "admin"))
                .expectNext(testCustomer)
                .verifyComplete();

        verify(customerRepository, times(1)).findById(testCustomer.getCustomerId());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }
}
