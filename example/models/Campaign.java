package com.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.common.aliasing.qual.Unique;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Campaign model representing a fundraising campaign created by a beneficiary
 * and managed by an institution.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Campaign {

    @Id
    @JsonProperty
    private Long id;

    @JsonProperty
    @NotNull(message = "Beneficiary ID is required")
    private Long beneficiaryId;

    @JsonProperty
    @NotNull(message = "Institution ID is required")
    private Long institutionId;

    @JsonProperty
    @NotBlank(message = "Campaign title is required")
    private String title;

    @JsonProperty
    @NotBlank(message = "Campaign description is required")
    private String description;

    @JsonProperty
    @NotNull(message = "Fund needed is required")
    @Positive(message = "Fund needed must be positive")
    private BigDecimal fundNeeded;

    @JsonProperty
    private BigDecimal fundRaised;

    @JsonProperty
    private BigDecimal goalAmount;

    @JsonProperty
    @NotBlank(message = "Campaign category is required")
    private String category; // e.g., Medical, Education, Emergency, Social Cause

    // Report file stored in Docstore
    @JsonProperty
    private String reportFileId; // Docstore file ID for the general report

    // Campaign status
    @JsonProperty
    @NotNull
    private Boolean isLive;

    @JsonProperty
    @NotNull
    private Boolean isVerified;

    @JsonProperty
    private Boolean isApproved;

    @JsonProperty
    private Boolean isFulfilled;

    @JsonProperty
    private Boolean isClosed;

    // Verification details
    @JsonProperty
    private Long verifiedBy; // Institution user/admin ID who verified

    @JsonProperty
    private Long verifiedAt;

    @JsonProperty
    private String verificationNotes;

    @JsonProperty
    private String rejectionReason;

    // Campaign timeline
    @JsonProperty
    private Long startDate;

    @JsonProperty
    private Long endDate;

    @JsonProperty
    private Integer durationInDays;

    // Campaign visibility and engagement
    @JsonProperty
    private Integer viewCount;

    @JsonProperty
    private Integer shareCount;

    @JsonProperty
    private Integer donorCount;

    // Media
    @JsonProperty
    private String thumbnailUrl;

    @JsonProperty
    private List<String> imageUrls;

    @JsonProperty
    private List<String> videoUrls;

    // Priority and urgency
    @JsonProperty
    private String urgencyLevel; // LOW, MEDIUM, HIGH, CRITICAL

    @JsonProperty
    private Boolean isFeatured;

    @JsonProperty
    private Integer priorityScore;

    // Beneficiary story
    @JsonProperty
    private String beneficiaryStory;

    @JsonProperty
    private String impactStatement;

    // Timestamps
    @JsonProperty
    private Long createdAt;

    @JsonProperty
    private Long updatedAt;

    @JsonProperty
    private Long approvedAt;

    @JsonProperty
    private Long closedAt;

    /**
     * Check if campaign is active and can receive donations
     */
    public boolean canReceiveDonations() {
        boolean canReceive = isLive != null && isLive &&
                            isVerified != null && isVerified &&
                            isApproved != null && isApproved &&
                            (isClosed == null || !isClosed) &&
                            (isFulfilled == null || !isFulfilled);

        if (!canReceive) {
            log.warn("Campaign {} cannot receive donations. Live: {}, Verified: {}, Approved: {}, Closed: {}, Fulfilled: {}",
                    id, isLive, isVerified, isApproved, isClosed, isFulfilled);
        }

        return canReceive;
    }

    /**
     * Check if campaign goal is reached
     */
    public boolean isGoalReached() {
        if (fundRaised == null || fundNeeded == null) {
            return false;
        }
        return fundRaised.compareTo(fundNeeded) >= 0;
    }

    /**
     * Calculate funding percentage
     */
    public double getFundingPercentage() {
        if (fundRaised == null || fundNeeded == null || fundNeeded.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return fundRaised.divide(fundNeeded, 4, RoundingMode.HALF_UP)
                         .multiply(BigDecimal.valueOf(100))
                         .doubleValue();
    }

    /**
     * Get remaining amount needed
     */
    public BigDecimal getRemainingAmount() {
        if (fundRaised == null || fundNeeded == null) {
            return fundNeeded;
        }
        BigDecimal remaining = fundNeeded.subtract(fundRaised);
        return remaining.compareTo(BigDecimal.ZERO) > 0 ? remaining : BigDecimal.ZERO;
    }

    /**
     * Add donation to campaign
     */
    public void addDonation(BigDecimal amount) {
        if (fundRaised == null) {
            fundRaised = BigDecimal.ZERO;
        }
        fundRaised = fundRaised.add(amount);

        if (donorCount == null) {
            donorCount = 0;
        }
        donorCount++;

        log.info("Donation of {} added to campaign {}. Total raised: {}", amount, id, fundRaised);

        // Check if goal is reached
        if (isGoalReached()) {
            isFulfilled = true;
            log.info("Campaign {} has reached its funding goal!", id);
        }
    }

    /**
     * Approve campaign
     */
    public void approveCampaign(Long approverInstitutionUserId) {
        this.isApproved = true;
        this.isVerified = true;
        this.verifiedBy = approverInstitutionUserId;
        this.approvedAt = System.currentTimeMillis();
        this.verifiedAt = System.currentTimeMillis();
        log.info("Campaign {} approved by institution user {}", id, approverInstitutionUserId);
    }

    /**
     * Reject campaign
     */
    public void rejectCampaign(String reason, Long rejectorInstitutionUserId) {
        this.isApproved = false;
        this.isVerified = true;
        this.rejectionReason = reason;
        this.verifiedBy = rejectorInstitutionUserId;
        this.verifiedAt = System.currentTimeMillis();
        this.isLive = false;
        log.info("Campaign {} rejected by institution user {}. Reason: {}", id, rejectorInstitutionUserId, reason);
    }

    /**
     * Close campaign
     */
    public void closeCampaign() {
        this.isClosed = true;
        this.isLive = false;
        this.closedAt = System.currentTimeMillis();
        log.info("Campaign {} closed at {}", id, closedAt);
    }

    /**
     * Increment view count
     */
    public void incrementViewCount() {
        if (viewCount == null) {
            viewCount = 0;
        }
        viewCount++;
    }

    /**
     * Increment share count
     */
    public void incrementShareCount() {
        if (shareCount == null) {
            shareCount = 0;
        }
        shareCount++;
    }

    /**
     * Check if campaign has expired
     */
    public boolean isExpired() {
        if (endDate == null) {
            return false;
        }
        return System.currentTimeMillis() > endDate;
    }

    /**
     * Validate campaign for approval
     */
    public boolean isValidForApproval() {
        boolean isValid = beneficiaryId != null &&
                         institutionId != null &&
                         title != null &&
                         description != null &&
                         fundNeeded != null &&
                         category != null;

        if (!isValid) {
            log.warn("Campaign {} validation failed for approval", id);
        }

        return isValid;
    }
}
