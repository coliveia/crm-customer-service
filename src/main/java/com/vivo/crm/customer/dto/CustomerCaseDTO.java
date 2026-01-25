package com.vivo.crm.customer.dto;

import com.vivo.crm.customer.entity.CustomerCase;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerCaseDTO {
    private Long id;
    private String caseNumber;
    private Long customerId;
    private String title;
    private String description;
    private String status;
    private String priority;
    private String category;
    private String subcategory;
    private String assignedTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
    private Integer resolutionTimeMinutes;
    private String resolutionNotes;
    private Double satisfactionScore;
    private List<InteractionDTO> interactions;

    public static CustomerCaseDTO fromEntity(CustomerCase customerCase) {
        return CustomerCaseDTO.builder()
                .id(customerCase.getId())
                .caseNumber(customerCase.getCaseNumber())
                .customerId(customerCase.getCustomer().getId())
                .title(customerCase.getTitle())
                .description(customerCase.getDescription())
                .status(customerCase.getStatus().toString())
                .priority(customerCase.getPriority().toString())
                .category(customerCase.getCategory())
                .subcategory(customerCase.getSubcategory())
                .assignedTo(customerCase.getAssignedTo())
                .createdAt(customerCase.getCreated_at())
                .updatedAt(customerCase.getUpdated_at())
                .resolvedAt(customerCase.getResolved_at())
                .resolutionTimeMinutes(customerCase.getResolution_time_minutes())
                .resolutionNotes(customerCase.getResolution_notes())
                .satisfactionScore(customerCase.getSatisfaction_score())
                .build();
    }

    public CustomerCase toEntity() {
        return CustomerCase.builder()
                .caseNumber(this.caseNumber)
                .title(this.title)
                .description(this.description)
                .status(CustomerCase.CaseStatus.valueOf(this.status))
                .priority(CustomerCase.CasePriority.valueOf(this.priority))
                .category(this.category)
                .subcategory(this.subcategory)
                .assignedTo(this.assignedTo)
                .build();
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class CustomerCaseCreateRequest {
    private String title;
    private String description;
    private String category;
    private String subcategory;
    private String priority;
    private String assignedTo;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class CustomerCaseUpdateRequest {
    private String title;
    private String description;
    private String status;
    private String priority;
    private String category;
    private String subcategory;
    private String assignedTo;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class CustomerCaseResolveRequest {
    private String resolutionNotes;
    private Double satisfactionScore;
}
