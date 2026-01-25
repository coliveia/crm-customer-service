package com.vivo.crm.customer.dto;

import com.vivo.crm.customer.entity.Interaction;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InteractionDTO {
    private Long id;
    private String interactionId;
    private Long customerId;
    private Long caseId;
    private String channel;
    private String agentId;
    private String agentName;
    private String message;
    private String direction;
    private Integer durationSeconds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String sentiment;
    private Double sentimentScore;
    private String notes;

    public static InteractionDTO fromEntity(Interaction interaction) {
        return InteractionDTO.builder()
                .id(interaction.getId())
                .interactionId(interaction.getInteractionId())
                .customerId(interaction.getCustomer().getId())
                .caseId(interaction.getCustomerCase() != null ? interaction.getCustomerCase().getId() : null)
                .channel(interaction.getChannel().toString())
                .agentId(interaction.getAgentId())
                .agentName(interaction.getAgentName())
                .message(interaction.getMessage())
                .direction(interaction.getDirection().toString())
                .durationSeconds(interaction.getDuration_seconds())
                .createdAt(interaction.getCreated_at())
                .updatedAt(interaction.getUpdated_at())
                .sentiment(interaction.getSentiment())
                .sentimentScore(interaction.getSentiment_score())
                .notes(interaction.getNotes())
                .build();
    }

    public Interaction toEntity() {
        return Interaction.builder()
                .interactionId(this.interactionId)
                .channel(Interaction.InteractionChannel.valueOf(this.channel))
                .agentId(this.agentId)
                .agentName(this.agentName)
                .message(this.message)
                .direction(Interaction.InteractionDirection.valueOf(this.direction))
                .duration_seconds(this.durationSeconds)
                .sentiment(this.sentiment)
                .sentiment_score(this.sentimentScore)
                .notes(this.notes)
                .build();
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class InteractionCreateRequest {
    private String channel;
    private String agentId;
    private String agentName;
    private String message;
    private String direction;
    private Integer durationSeconds;
    private String sentiment;
    private Double sentimentScore;
    private String notes;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class InteractionUpdateRequest {
    private String message;
    private String sentiment;
    private Double sentimentScore;
    private String notes;
}
