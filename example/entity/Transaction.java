package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id", length = 255)
    private Long transactionId;

    @Column(name = "donor_id", nullable = false)
    private Long donorId;

    @Column(name = "campaign_id", nullable = false)
    private Long campaignId;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;


//    @Column(name = "payment_gateway", length = 100)
//    private String paymentGateway;


    @Column(name = "upi_id", length = 255)
    private String upiId;

    // Transaction status
    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "failure_reason", columnDefinition = "TEXT")
    private String failureReason;
    @Column(name = "is_anonymous")
    private Boolean isAnonymous;
    @Column(name = "donor_message", columnDefinition = "TEXT")
    private String donorMessage;
    // Receipt
    @Column(name = "receipt_number", length = 100)
    private String receiptNumber;

    @Column(name = "receipt_url", length = 1000)
    private String receiptUrl;

    @Column(name = "created", columnDefinition = "datetime default current_timestamp", updatable = false, insertable = false)
    @Generated(value = GenerationTime.INSERT)
    private Date created;

    @Column(name = "updated", columnDefinition = "datetime default current_timestamp on update current_timestamp", updatable = false, insertable = false)
    @Generated(value = GenerationTime.ALWAYS)
    private Date updated;
}
