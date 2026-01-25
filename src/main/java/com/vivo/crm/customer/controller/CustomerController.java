package com.vivo.crm.customer.controller;

import com.vivo.crm.customer.dto.*;
import com.vivo.crm.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/customer")
@Tag(name = "Customer", description = "Customer management endpoints")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @Operation(summary = "Create a new customer", description = "Create a new customer in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully",
                    content = @Content(schema = @Schema(implementation = CustomerDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        log.info("POST /customer - Creating customer: {}", customerDTO.getExternalId());
        CustomerDTO created = customerService.createCustomer(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID", description = "Retrieve customer details by customer ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found",
                    content = @Content(schema = @Schema(implementation = CustomerDTO.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CustomerDTO> getCustomerById(
            @Parameter(description = "Customer ID") @PathVariable Long id) {
        log.info("GET /customer/{} - Fetching customer", id);
        CustomerDTO customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/external/{externalId}")
    @Operation(summary = "Get customer by external ID", description = "Retrieve customer details by external ID")
    @ApiResponse(responseCode = "200", description = "Customer found")
    public ResponseEntity<CustomerDTO> getCustomerByExternalId(
            @Parameter(description = "External ID") @PathVariable String externalId) {
        log.info("GET /customer/external/{} - Fetching customer", externalId);
        CustomerDTO customer = customerService.getCustomerByExternalId(externalId);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer", description = "Update customer information")
    @ApiResponse(responseCode = "200", description = "Customer updated successfully")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @Parameter(description = "Customer ID") @PathVariable Long id,
            @Valid @RequestBody CustomerDTO customerDTO) {
        log.info("PUT /customer/{} - Updating customer", id);
        CustomerDTO updated = customerService.updateCustomer(id, customerDTO);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    @Operation(summary = "Get all customers", description = "Retrieve all customers with pagination")
    @ApiResponse(responseCode = "200", description = "Customers retrieved successfully")
    public ResponseEntity<Page<CustomerDTO>> getAllCustomers(Pageable pageable) {
        log.info("GET /customer - Fetching all customers");
        Page<CustomerDTO> customers = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get customers by status", description = "Retrieve customers filtered by status")
    @ApiResponse(responseCode = "200", description = "Customers retrieved successfully")
    public ResponseEntity<Page<CustomerDTO>> getCustomersByStatus(
            @Parameter(description = "Customer status") @PathVariable String status,
            Pageable pageable) {
        log.info("GET /customer/status/{} - Fetching customers by status", status);
        Page<CustomerDTO> customers = customerService.getCustomersByStatus(status, pageable);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}/view360")
    @Operation(summary = "Get customer 360 view", description = "Retrieve complete customer view with all data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer 360 view retrieved",
                    content = @Content(schema = @Schema(implementation = CustomerView360DTO.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<CustomerView360DTO> getCustomerView360(
            @Parameter(description = "Customer ID") @PathVariable Long id) {
        log.info("GET /customer/{}/view360 - Fetching customer 360 view", id);
        CustomerView360DTO view360 = customerService.getCustomerView360(id);
        return ResponseEntity.ok(view360);
    }
}
