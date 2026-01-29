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

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Transaction model representing a donation transaction from a donor to a beneficiary
 * through a campaign managed by an institution.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Transaction {

    @JsonProperty
    private Long id;

    @JsonProperty
    @NotNull(message = "Donor ID is required")
    private Long donorId;

    @JsonProperty
    @NotNull(message = "Campaign ID is required")
    private Long campaignId;

    @JsonProperty
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @JsonProperty
    private BigDecimal platformFee;

    @JsonProperty
    private BigDecimal netAmount;

    // Payment details
    @JsonProperty
    @NotBlank(message = "Payment method is required")
    private String paymentMethod; // UPI, Card, NetBanking, Wallet

    @JsonProperty
    private String paymentGateway; // PhonePe, Razorpay, etc.

    @JsonProperty
    private String transactionId; // Payment gateway transaction ID

    @JsonProperty
    private String upiId; // UPI ID if payment method is UPI

    // Transaction status
    @JsonProperty
    @NotNull
    @NotBlank(message = "Transaction status is required")
    private String status; // PENDING, SUCCESS, FAILED, REFUNDED, CANCELLED

    @JsonProperty
    private String failureReason;

    @JsonProperty
    private Boolean isAnonymous;

    @JsonProperty
    private String donorMessage;

    // Receipt
    @JsonProperty
    private String receiptNumber;

    @JsonProperty
    private String receiptUrl;

    // Refund details
    @JsonProperty
    private BigDecimal refundAmount;

    @JsonProperty
    private String refundReason;

    // Timestamps
    @JsonProperty
    private Long createdAt;

    @JsonProperty
    private Long updatedAt;

    /**
     * Check if transaction is successful
     */
    public boolean isSuccess() {
        return "SUCCESS".equalsIgnoreCase(status);
    }

    /**
     * Check if transaction is pending
     */
    public boolean isPending() {
        return "PENDING".equalsIgnoreCase(status);
    }

    /**
     * Check if transaction is failed
     */
    public boolean isFailed() {
        return "FAILED".equalsIgnoreCase(status);
    }

    /**
     * Check if transaction is refunded
     */
    public boolean isRefunded() {
        return "REFUNDED".equalsIgnoreCase(status);
    }

    /**
     * Mark transaction as successful
     */
    public void markAsSuccess() {
        this.status = "SUCCESS";
        this.updatedAt = System.currentTimeMillis();
        log.info("Transaction {} marked as successful. Amount: {}, Donor: {}, Campaign: {}",
                id, amount, donorId, campaignId);
    }

    /**
     * Mark transaction as failed
     */
    public void markAsFailed(String reason) {
        this.status = "FAILED";
        this.failureReason = reason;
        this.updatedAt = System.currentTimeMillis();
        log.warn("Transaction {} marked as failed. Reason: {}", id, reason);
    }

    /**
     * Mark transaction as refunded
     */
    public void markAsRefunded(String reason, BigDecimal refundAmt) {
        this.status = "REFUNDED";
        this.refundReason = reason;
        this.refundAmount = refundAmt;
        this.updatedAt = System.currentTimeMillis();
        log.info("Transaction {} marked as refunded. Amount: {}, Reason: {}", id, refundAmt, reason);
    }

    /**
     * Calculate net amount after platform fee
     */
    public void calculateNetAmount(BigDecimal feePercentage) {
        if (amount == null) {
            log.warn("Cannot calculate net amount. Transaction amount is null");
            return;
        }

        this.platformFee = amount.multiply(feePercentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        this.netAmount = amount.subtract(platformFee);

        log.info("Net amount calculated for transaction {}. Amount: {}, Fee: {}, Net: {}",
                id, amount, platformFee, netAmount);
    }

    /**
     * Generate receipt
     */
    public void generateReceipt(String receiptNum, String receiptUrlPath) {
        this.receiptNumber = receiptNum;
        this.receiptUrl = receiptUrlPath;
        log.info("Receipt generated for transaction {}. Receipt Number: {}", id, receiptNum);
    }

    /**
     * Validate transaction for processing
     */
    public boolean isValidForProcessing() {
        boolean isValid = donorId != null &&
                         campaignId != null &&
                         amount != null &&
                         amount.compareTo(BigDecimal.ZERO) > 0 &&
                         paymentMethod != null;

        if (!isValid) {
            log.warn("Transaction {} validation failed for processing", id);
        }

        return isValid;
    }
}
