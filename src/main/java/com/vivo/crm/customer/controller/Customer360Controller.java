package com.vivo.crm.customer.controller;

import com.vivo.crm.customer.dto.Customer360DTO;
import com.vivo.crm.customer.service.Customer360Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for Customer360 Duality View
 * Provides endpoints to access consolidated customer data
 * Data is retrieved from Oracle Duality View (customer_360_dv)
 */
@RestController
@RequestMapping("/api/customer360")
@Tag(name = "Customer 360", description = "Customer 360° consolidated view endpoints (Oracle Duality View)")
public class Customer360Controller {

    private final Customer360Service customer360Service;

    public Customer360Controller(Customer360Service customer360Service) {
        this.customer360Service = customer360Service;
    }

    /**
     * Get complete customer 360 view by customer ID
     * Returns consolidated data: identification, products, financials, cases, interactions, statistics
     */
    @GetMapping("/{customerId}")
    @Operation(summary = "Get customer 360 view by ID", 
               description = "Retrieve complete customer view with all consolidated data from Duality View")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer 360 view retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Customer360DTO.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Customer360DTO> getCustomer360(
            @Parameter(description = "Customer ID") @PathVariable Long customerId) {
        Customer360DTO view = customer360Service.getCustomer360ById(customerId);
        return ResponseEntity.ok(view);
    }

    /**
     * Get complete customer 360 view by external ID
     */
    @GetMapping("/external/{externalId}")
    @Operation(summary = "Get customer 360 view by external ID",
               description = "Retrieve complete customer view using external ID")
    @ApiResponse(responseCode = "200", description = "Customer 360 view retrieved successfully")
    public ResponseEntity<Customer360DTO> getCustomer360ByExternalId(
            @Parameter(description = "External customer ID") @PathVariable String externalId) {
        Customer360DTO view = customer360Service.getCustomer360ByExternalId(externalId);
        return ResponseEntity.ok(view);
    }

    /**
     * Get all customers 360 views by status
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get customers 360 view by status",
               description = "Retrieve all customers with specific status")
    @ApiResponse(responseCode = "200", description = "Customers retrieved successfully")
    public ResponseEntity<Page<Customer360DTO>> getCustomer360ByStatus(
            @Parameter(description = "Customer status") @PathVariable String status,
            Pageable pageable) {
        Page<Customer360DTO> views = customer360Service.getCustomer360ByStatus(status, pageable);
        return ResponseEntity.ok(views);
    }

    /**
     * Get all customers 360 views by segment
     */
    @GetMapping("/segment/{segment}")
    @Operation(summary = "Get customers 360 view by segment",
               description = "Retrieve all customers in specific segment")
    @ApiResponse(responseCode = "200", description = "Customers retrieved successfully")
    public ResponseEntity<Page<Customer360DTO>> getCustomer360BySegment(
            @Parameter(description = "Customer segment") @PathVariable String segment,
            Pageable pageable) {
        Page<Customer360DTO> views = customer360Service.getCustomer360BySegment(segment, pageable);
        return ResponseEntity.ok(views);
    }

    /**
     * Get all customers 360 views
     */
    @GetMapping
    @Operation(summary = "Get all customers 360 view",
               description = "Retrieve all customers with pagination")
    @ApiResponse(responseCode = "200", description = "Customers retrieved successfully")
    public ResponseEntity<Page<Customer360DTO>> getAllCustomer360(Pageable pageable) {
        Page<Customer360DTO> views = customer360Service.getAllCustomer360(pageable);
        return ResponseEntity.ok(views);
    }

    /**
     * Get customer identification data only
     */
    @GetMapping("/{customerId}/identification")
    @Operation(summary = "Get customer identification",
               description = "Retrieve customer identification data")
    @ApiResponse(responseCode = "200", description = "Identification retrieved successfully")
    public ResponseEntity<Customer360DTO> getCustomerIdentification(
            @Parameter(description = "Customer ID") @PathVariable Long customerId) {
        Customer360DTO view = customer360Service.getCustomerIdentification(customerId);
        return ResponseEntity.ok(view);
    }

    /**
     * Get customer products only
     */
    @GetMapping("/{customerId}/products")
    @Operation(summary = "Get customer products",
               description = "Retrieve customer contracted products")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    public ResponseEntity<Customer360DTO> getCustomerProducts(
            @Parameter(description = "Customer ID") @PathVariable Long customerId) {
        Customer360DTO view = customer360Service.getCustomerProducts(customerId);
        return ResponseEntity.ok(view);
    }

    /**
     * Get customer financials only
     */
    @GetMapping("/{customerId}/financials")
    @Operation(summary = "Get customer financials",
               description = "Retrieve customer financial data")
    @ApiResponse(responseCode = "200", description = "Financials retrieved successfully")
    public ResponseEntity<Customer360DTO> getCustomerFinancials(
            @Parameter(description = "Customer ID") @PathVariable Long customerId) {
        Customer360DTO view = customer360Service.getCustomerFinancials(customerId);
        return ResponseEntity.ok(view);
    }

    /**
     * Get customer cases/tickets only
     */
    @GetMapping("/{customerId}/cases")
    @Operation(summary = "Get customer cases",
               description = "Retrieve customer support cases and tickets")
    @ApiResponse(responseCode = "200", description = "Cases retrieved successfully")
    public ResponseEntity<Customer360DTO> getCustomerCases(
            @Parameter(description = "Customer ID") @PathVariable Long customerId) {
        Customer360DTO view = customer360Service.getCustomerCases(customerId);
        return ResponseEntity.ok(view);
    }

    /**
     * Get customer interactions only
     */
    @GetMapping("/{customerId}/interactions")
    @Operation(summary = "Get customer interactions",
               description = "Retrieve customer interaction history")
    @ApiResponse(responseCode = "200", description = "Interactions retrieved successfully")
    public ResponseEntity<Customer360DTO> getCustomerInteractions(
            @Parameter(description = "Customer ID") @PathVariable Long customerId) {
        Customer360DTO view = customer360Service.getCustomerInteractions(customerId);
        return ResponseEntity.ok(view);
    }

    /**
     * Get customer statistics only
     */
    @GetMapping("/{customerId}/statistics")
    @Operation(summary = "Get customer statistics",
               description = "Retrieve customer statistics and metrics")
    @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    public ResponseEntity<Customer360DTO> getCustomerStatistics(
            @Parameter(description = "Customer ID") @PathVariable Long customerId) {
        Customer360DTO view = customer360Service.getCustomerStatistics(customerId);
        return ResponseEntity.ok(view);
    }
}
