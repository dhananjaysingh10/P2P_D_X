package com.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimelineUpdate {
    @JsonProperty
    private Long id;

    @JsonProperty
    @NotNull(message = "Campaign ID is required")
    private Long campaignId;

    @JsonProperty
    @NotNull(message = "Institution ID is required")
    private Long institutionId;

    @JsonProperty
    @NotBlank(message = "Campaign description is required")
    private String description;

    @JsonProperty
    private String proofFilePath;

    @JsonProperty
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @JsonProperty
    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;
}


