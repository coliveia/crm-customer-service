package com.vivo.crm.customer.service;

import com.vivo.crm.customer.dto.Customer360DTO;
import com.vivo.crm.customer.entity.Customer360View;
import com.vivo.crm.customer.repository.Customer360Repository;
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
class Customer360ServiceTest {

    @Mock
    private Customer360Repository customer360Repository;

    @InjectMocks
    private Customer360Service customer360Service;

    private Customer360View testView;
    private Customer360DTO testDTO;

    @BeforeEach
    void setUp() {
        testDTO = Customer360DTO.builder()
                .customerId(1L)
                .externalId("EXT-001")
                .name("John Doe")
                .email("john@example.com")
                .phone("11999999999")
                .status("ACTIVE")
                .segment("Premium")
                .build();

        testView = Customer360View.builder()
                .customerId(1L)
                .externalId("EXT-001")
                .name("John Doe")
                .email("john@example.com")
                .phone("11999999999")
                .status("ACTIVE")
                .segment("Premium")
                .data("{}")
                .parsedData(testDTO)
                .build();
    }

    @Test
    void testGetCustomer360ById() {
        when(customer360Repository.findCustomer360ById(1L)).thenReturn(Optional.of(testView));

        Customer360DTO result = customer360Service.getCustomer360ById(1L);

        assertNotNull(result);
        assertEquals("EXT-001", result.getExternalId());
        assertEquals("John Doe", result.getName());
        verify(customer360Repository, times(1)).findCustomer360ById(1L);
    }

    @Test
    void testGetCustomer360ByIdNotFound() {
        when(customer360Repository.findCustomer360ById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> customer360Service.getCustomer360ById(999L));
    }

    @Test
    void testGetCustomer360ByExternalId() {
        when(customer360Repository.findCustomer360ByExternalId("EXT-001")).thenReturn(Optional.of(testView));

        Customer360DTO result = customer360Service.getCustomer360ByExternalId("EXT-001");

        assertNotNull(result);
        assertEquals("EXT-001", result.getExternalId());
        verify(customer360Repository, times(1)).findCustomer360ByExternalId("EXT-001");
    }

    @Test
    void testGetCustomer360ByStatus() {
        Page<Customer360View> viewPage = new PageImpl<>(java.util.List.of(testView));
        when(customer360Repository.findCustomer360ByStatus("ACTIVE", PageRequest.of(0, 10)))
                .thenReturn(viewPage);

        Page<Customer360DTO> result = customer360Service.getCustomer360ByStatus("ACTIVE", PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(customer360Repository, times(1)).findCustomer360ByStatus("ACTIVE", PageRequest.of(0, 10));
    }

    @Test
    void testGetCustomer360BySegment() {
        Page<Customer360View> viewPage = new PageImpl<>(java.util.List.of(testView));
        when(customer360Repository.findCustomer360BySegment("Premium", PageRequest.of(0, 10)))
                .thenReturn(viewPage);

        Page<Customer360DTO> result = customer360Service.getCustomer360BySegment("Premium", PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(customer360Repository, times(1)).findCustomer360BySegment("Premium", PageRequest.of(0, 10));
    }

    @Test
    void testGetAllCustomer360() {
        Page<Customer360View> viewPage = new PageImpl<>(java.util.List.of(testView));
        when(customer360Repository.findAllCustomer360(PageRequest.of(0, 10)))
                .thenReturn(viewPage);

        Page<Customer360DTO> result = customer360Service.getAllCustomer360(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(customer360Repository, times(1)).findAllCustomer360(PageRequest.of(0, 10));
    }

    @Test
    void testGetCustomerIdentification() {
        when(customer360Repository.findCustomer360ById(1L)).thenReturn(Optional.of(testView));

        Customer360DTO result = customer360Service.getCustomerIdentification(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(customer360Repository, times(1)).findCustomer360ById(1L);
    }

    @Test
    void testGetCustomerProducts() {
        when(customer360Repository.findCustomer360ById(1L)).thenReturn(Optional.of(testView));

        Customer360DTO result = customer360Service.getCustomerProducts(1L);

        assertNotNull(result);
        verify(customer360Repository, times(1)).findCustomer360ById(1L);
    }

    @Test
    void testGetCustomerFinancials() {
        when(customer360Repository.findCustomer360ById(1L)).thenReturn(Optional.of(testView));

        Customer360DTO result = customer360Service.getCustomerFinancials(1L);

        assertNotNull(result);
        verify(customer360Repository, times(1)).findCustomer360ById(1L);
    }

    @Test
    void testGetCustomerCases() {
        when(customer360Repository.findCustomer360ById(1L)).thenReturn(Optional.of(testView));

        Customer360DTO result = customer360Service.getCustomerCases(1L);

        assertNotNull(result);
        verify(customer360Repository, times(1)).findCustomer360ById(1L);
    }

    @Test
    void testGetCustomerInteractions() {
        when(customer360Repository.findCustomer360ById(1L)).thenReturn(Optional.of(testView));

        Customer360DTO result = customer360Service.getCustomerInteractions(1L);

        assertNotNull(result);
        verify(customer360Repository, times(1)).findCustomer360ById(1L);
    }

    @Test
    void testGetCustomerStatistics() {
        when(customer360Repository.findCustomer360ById(1L)).thenReturn(Optional.of(testView));

        Customer360DTO result = customer360Service.getCustomerStatistics(1L);

        assertNotNull(result);
        verify(customer360Repository, times(1)).findCustomer360ById(1L);
    }
}
