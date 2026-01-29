package com.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Institution model that acts as a mediator between donors and beneficiaries.
 * Institutions verify and approve campaigns for donation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Institution {

    @JsonProperty
    private Long id;

    @JsonProperty
    @NotBlank(message = "Institution name is required")
    private String name;

    @JsonProperty
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @JsonProperty
    @NotBlank(message = "Phone number is required")
    private String phone;

    @JsonProperty
    @NotBlank(message = "Registered GST number is required")
    private String registeredGst;

    @JsonProperty
    @NotBlank(message = "Company PAN is required")
    private String companyPan;

    @JsonProperty
    private String address;

    @JsonProperty
    private String city;

    @JsonProperty
    private String state;

    @JsonProperty
    private String pincode;

    @JsonProperty
    private String country;

    @JsonProperty
    @NotBlank(message = "Institution type is required")
    private String institutionType; // e.g., NGO, Hospital, Educational, Government

    @JsonProperty
    private String registrationNumber; // Government registration number

    @JsonProperty
    private String description;

    @JsonProperty
    private String website;

    @JsonProperty
    private String logoUrl;

    // Verification and approval status
    @JsonProperty
    @NotNull
    private Boolean isVerified;

    @JsonProperty
    @NotNull
    private Boolean isActive;

    // Contact person details
    @JsonProperty
    private String contactPersonName;

    @JsonProperty
    private String contactPersonEmail;

    @JsonProperty
    private String contactPersonPhone;

    @JsonProperty
    private String contactPersonDesignation;

    // Documents for verification
    @JsonProperty
    private String gstCertificateUrl;

    @JsonProperty
    private String panCardUrl;

    @JsonProperty
    private String registrationCertificateUrl;

    @JsonProperty
    private List<String> additionalDocuments;

    // Approval authority details
    @JsonProperty
    private String approverName;

    @JsonProperty
    private String approverEmail;

    @JsonProperty
    private String approverDesignation;

    // Bank details for fund transfers
    @JsonProperty
    private String bankAccountNumber;

    @JsonProperty
    private String bankName;

    @JsonProperty
    private String ifscCode;

    @JsonProperty
    private String accountHolderName;

    // Statistics
    @JsonProperty
    private Integer totalCampaignsApproved;

    @JsonProperty
    private Integer totalCampaignsRejected;

    @JsonProperty
    private Integer activeCampaigns;

    // Timestamps
    @JsonProperty
    private Long createdAt;

    @JsonProperty
    private Long updatedAt;

    @JsonProperty
    private Long verifiedAt;

    /**
     * Check if institution can approve campaigns
     */
    public boolean canApproveCampaigns() {
        boolean canApprove = isVerified != null && isVerified &&
                            isActive != null && isActive &&
                            registeredGst != null && companyPan != null;

        if (!canApprove) {
            log.warn("Institution {} cannot approve campaigns. Verified: {}, Active: {}",
                    name, isVerified, isActive);
        }

        return canApprove;
    }

    /**
     * Validate institution details for registration
     */
    public boolean isValidForRegistration() {
        boolean isValid = name != null && email != null && phone != null &&
                         registeredGst != null && companyPan != null &&
                         institutionType != null;

        if (!isValid) {
            log.warn("Institution registration validation failed for: {}", name);
        }

        return isValid;
    }

    /**
     * Check if all required documents are uploaded
     */
    public boolean hasAllRequiredDocuments() {
        return gstCertificateUrl != null &&
               panCardUrl != null &&
               registrationCertificateUrl != null;
    }

    /**
     * Increment approved campaigns count
     */
    public void incrementApprovedCampaigns() {
        if (totalCampaignsApproved == null) {
            totalCampaignsApproved = 0;
        }
        totalCampaignsApproved++;
        log.info("Institution {} approved campaign. Total approved: {}", name, totalCampaignsApproved);
    }

    /**
     * Increment rejected campaigns count
     */
    public void incrementRejectedCampaigns() {
        if (totalCampaignsRejected == null) {
            totalCampaignsRejected = 0;
        }
        totalCampaignsRejected++;
        log.info("Institution {} rejected campaign. Total rejected: {}", name, totalCampaignsRejected);
    }

    /**
     * Increment active campaigns count
     */
    public void incrementActiveCampaigns() {
        if (activeCampaigns == null) {
            activeCampaigns = 0;
        }
        activeCampaigns++;
    }

    /**
     * Decrement active campaigns count
     */
    public void decrementActiveCampaigns() {
        if (activeCampaigns == null || activeCampaigns == 0) {
            activeCampaigns = 0;
            return;
        }
        activeCampaigns--;
    }
}
