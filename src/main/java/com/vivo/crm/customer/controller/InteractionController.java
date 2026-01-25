package com.vivo.crm.customer.controller;

import com.vivo.crm.customer.dto.InteractionDTO;
import com.vivo.crm.customer.service.InteractionService;
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
@RequestMapping("/interaction")
@Tag(name = "Interaction", description = "Customer interaction management endpoints")
public class InteractionController {

    private final InteractionService interactionService;

    public InteractionController(InteractionService interactionService) {
        this.interactionService = interactionService;
    }

    @PostMapping("/customer/{customerId}")
    @Operation(summary = "Create a new interaction", description = "Create a new customer interaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Interaction created successfully",
                    content = @Content(schema = @Schema(implementation = InteractionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<InteractionDTO> createInteraction(
            @Parameter(description = "Customer ID") @PathVariable Long customerId,
            @Parameter(description = "Case ID (optional)") @RequestParam(required = false) Long caseId,
            @Valid @RequestBody InteractionDTO interactionDTO) {
        log.info("POST /interaction/customer/{} - Creating interaction", customerId);
        InteractionDTO created = interactionService.createInteraction(customerId, caseId, interactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get interaction by ID", description = "Retrieve interaction details by interaction ID")
    @ApiResponse(responseCode = "200", description = "Interaction found")
    public ResponseEntity<InteractionDTO> getInteractionById(
            @Parameter(description = "Interaction ID") @PathVariable Long id) {
        log.info("GET /interaction/{} - Fetching interaction", id);
        InteractionDTO interaction = interactionService.getInteractionById(id);
        return ResponseEntity.ok(interaction);
    }

    @GetMapping("/id/{interactionId}")
    @Operation(summary = "Get interaction by interaction ID", description = "Retrieve interaction details by interaction ID string")
    @ApiResponse(responseCode = "200", description = "Interaction found")
    public ResponseEntity<InteractionDTO> getInteractionByInteractionId(
            @Parameter(description = "Interaction ID string") @PathVariable String interactionId) {
        log.info("GET /interaction/id/{} - Fetching interaction", interactionId);
        InteractionDTO interaction = interactionService.getInteractionByInteractionId(interactionId);
        return ResponseEntity.ok(interaction);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update interaction", description = "Update interaction information")
    @ApiResponse(responseCode = "200", description = "Interaction updated successfully")
    public ResponseEntity<InteractionDTO> updateInteraction(
            @Parameter(description = "Interaction ID") @PathVariable Long id,
            @Valid @RequestBody InteractionDTO interactionDTO) {
        log.info("PUT /interaction/{} - Updating interaction", id);
        InteractionDTO updated = interactionService.updateInteraction(id, interactionDTO);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get interactions by customer", description = "Retrieve all interactions for a customer")
    @ApiResponse(responseCode = "200", description = "Interactions retrieved successfully")
    public ResponseEntity<Page<InteractionDTO>> getInteractionsByCustomer(
            @Parameter(description = "Customer ID") @PathVariable Long customerId,
            Pageable pageable) {
        log.info("GET /interaction/customer/{} - Fetching interactions for customer", customerId);
        Page<InteractionDTO> interactions = interactionService.getInteractionsByCustomer(customerId, pageable);
        return ResponseEntity.ok(interactions);
    }

    @GetMapping("/case/{caseId}")
    @Operation(summary = "Get interactions by case", description = "Retrieve all interactions for a case")
    @ApiResponse(responseCode = "200", description = "Interactions retrieved successfully")
    public ResponseEntity<Page<InteractionDTO>> getInteractionsByCase(
            @Parameter(description = "Case ID") @PathVariable Long caseId,
            Pageable pageable) {
        log.info("GET /interaction/case/{} - Fetching interactions for case", caseId);
        Page<InteractionDTO> interactions = interactionService.getInteractionsByCase(caseId, pageable);
        return ResponseEntity.ok(interactions);
    }

    @GetMapping("/channel/{channel}")
    @Operation(summary = "Get interactions by channel", description = "Retrieve interactions filtered by channel")
    @ApiResponse(responseCode = "200", description = "Interactions retrieved successfully")
    public ResponseEntity<Page<InteractionDTO>> getInteractionsByChannel(
            @Parameter(description = "Interaction channel") @PathVariable String channel,
            Pageable pageable) {
        log.info("GET /interaction/channel/{} - Fetching interactions by channel", channel);
        Page<InteractionDTO> interactions = interactionService.getInteractionsByChannel(channel, pageable);
        return ResponseEntity.ok(interactions);
    }
}
