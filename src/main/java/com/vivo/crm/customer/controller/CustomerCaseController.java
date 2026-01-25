package com.vivo.crm.customer.controller;

import com.vivo.crm.customer.dto.CustomerCaseDTO;
import com.vivo.crm.customer.service.CustomerCaseService;
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
@RequestMapping("/case")
@Tag(name = "Case", description = "Customer case management endpoints")
public class CustomerCaseController {

    private final CustomerCaseService caseService;

    public CustomerCaseController(CustomerCaseService caseService) {
        this.caseService = caseService;
    }

    @PostMapping("/customer/{customerId}")
    @Operation(summary = "Create a new case", description = "Create a new customer case")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Case created successfully",
                    content = @Content(schema = @Schema(implementation = CustomerCaseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<CustomerCaseDTO> createCase(
            @Parameter(description = "Customer ID") @PathVariable Long customerId,
            @Valid @RequestBody CustomerCaseDTO caseDTO) {
        log.info("POST /case/customer/{} - Creating case", customerId);
        CustomerCaseDTO created = caseService.createCase(customerId, caseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get case by ID", description = "Retrieve case details by case ID")
    @ApiResponse(responseCode = "200", description = "Case found")
    public ResponseEntity<CustomerCaseDTO> getCaseById(
            @Parameter(description = "Case ID") @PathVariable Long id) {
        log.info("GET /case/{} - Fetching case", id);
        CustomerCaseDTO customerCase = caseService.getCaseById(id);
        return ResponseEntity.ok(customerCase);
    }

    @GetMapping("/number/{caseNumber}")
    @Operation(summary = "Get case by case number", description = "Retrieve case details by case number")
    @ApiResponse(responseCode = "200", description = "Case found")
    public ResponseEntity<CustomerCaseDTO> getCaseByCaseNumber(
            @Parameter(description = "Case number") @PathVariable String caseNumber) {
        log.info("GET /case/number/{} - Fetching case", caseNumber);
        CustomerCaseDTO customerCase = caseService.getCaseByCaseNumber(caseNumber);
        return ResponseEntity.ok(customerCase);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update case", description = "Update case information")
    @ApiResponse(responseCode = "200", description = "Case updated successfully")
    public ResponseEntity<CustomerCaseDTO> updateCase(
            @Parameter(description = "Case ID") @PathVariable Long id,
            @Valid @RequestBody CustomerCaseDTO caseDTO) {
        log.info("PUT /case/{} - Updating case", id);
        CustomerCaseDTO updated = caseService.updateCase(id, caseDTO);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/resolve")
    @Operation(summary = "Resolve case", description = "Mark case as resolved with notes and satisfaction score")
    @ApiResponse(responseCode = "200", description = "Case resolved successfully")
    public ResponseEntity<CustomerCaseDTO> resolveCase(
            @Parameter(description = "Case ID") @PathVariable Long id,
            @RequestParam String resolutionNotes,
            @RequestParam Double satisfactionScore) {
        log.info("POST /case/{}/resolve - Resolving case", id);
        CustomerCaseDTO resolved = caseService.resolveCase(id, resolutionNotes, satisfactionScore);
        return ResponseEntity.ok(resolved);
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get cases by customer", description = "Retrieve all cases for a customer")
    @ApiResponse(responseCode = "200", description = "Cases retrieved successfully")
    public ResponseEntity<Page<CustomerCaseDTO>> getCasesByCustomer(
            @Parameter(description = "Customer ID") @PathVariable Long customerId,
            Pageable pageable) {
        log.info("GET /case/customer/{} - Fetching cases for customer", customerId);
        Page<CustomerCaseDTO> cases = caseService.getCasesByCustomer(customerId, pageable);
        return ResponseEntity.ok(cases);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get cases by status", description = "Retrieve cases filtered by status")
    @ApiResponse(responseCode = "200", description = "Cases retrieved successfully")
    public ResponseEntity<Page<CustomerCaseDTO>> getCasesByStatus(
            @Parameter(description = "Case status") @PathVariable String status,
            Pageable pageable) {
        log.info("GET /case/status/{} - Fetching cases by status", status);
        Page<CustomerCaseDTO> cases = caseService.getCasesByStatus(status, pageable);
        return ResponseEntity.ok(cases);
    }

    @GetMapping("/priority/{priority}")
    @Operation(summary = "Get cases by priority", description = "Retrieve cases filtered by priority")
    @ApiResponse(responseCode = "200", description = "Cases retrieved successfully")
    public ResponseEntity<Page<CustomerCaseDTO>> getCasesByPriority(
            @Parameter(description = "Case priority") @PathVariable String priority,
            Pageable pageable) {
        log.info("GET /case/priority/{} - Fetching cases by priority", priority);
        Page<CustomerCaseDTO> cases = caseService.getCasesByPriority(priority, pageable);
        return ResponseEntity.ok(cases);
    }
}
