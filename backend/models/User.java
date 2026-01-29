package com.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * User model that serves both Donor and Beneficiary roles.
 * Common fields are shared, and additional beneficiary-specific fields
 * are included for campaign registration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class User {

    @JsonProperty
    private Long id;

    @JsonProperty
    @NotBlank(message = "Name is required")
    private String name;

    @JsonProperty
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @JsonProperty
    @NotBlank(message = "Phone number is required")
    private String phone;

    @JsonProperty
    @NotBlank(message = "PAN is required")
    private String pan;

    // Beneficiary-specific fields (null for donors)
    @JsonProperty
    private String reason;

    @JsonProperty
    private String additionalDocs; // JSON array stored as string

    @JsonProperty
    private String aadharCard; // Aadhaar card details

    @JsonProperty
    private BigDecimal fundNeeded;

    // Flag to identify if user is acting as beneficiary
    @JsonProperty
    private Boolean isBeneficiary;

    /**
     * Check if this user has beneficiary details filled
     */
    public boolean hasBeneficiaryDetails() {
        return reason != null && fundNeeded != null;
    }

    /**
     * Validates beneficiary-specific fields
     */
    public boolean isValidBeneficiary() {
        boolean isValid = name != null && email != null && phone != null &&
                         pan != null && reason != null && fundNeeded != null;

        if (!isValid) {
            log.warn("Beneficiary validation failed for user: {}", email);
        }

        return isValid;
    }

    /**
     * Validates donor-specific fields
     */
    public boolean isValidDonor() {
        boolean isValid = name != null && email != null && phone != null &&
                         pan != null;

        if (!isValid) {
            log.warn("Donor validation failed for user: {}", email);
        }

        return isValid;
    }
}
