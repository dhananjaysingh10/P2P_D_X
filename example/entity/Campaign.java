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
@Table(name = "campaigns")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "beneficiary_id", nullable = false)
    private Long beneficiaryId;

    @Column(name = "institution_id", nullable = false)
    private Long institutionId;

    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "fund_raised", precision = 15, scale = 2)
    private BigDecimal fundRaised;

    @Column(name = "report_file_id", length = 255)
    private String reportFileId;

    @Column(name = "is_live", nullable = false)
    private Boolean isLive;

    @Column(name = "is_approved")
    private Boolean isApproved;

    @Column(name = "is_fulfilled")
    private Boolean isFulfilled;

    @Column(name = "verified_by")
    private Long verifiedBy;

    @Column(name = "donor_count")
    private Integer donorCount;

    @Column(name = "priority_score")
    private Integer priorityScore;

    @Column(name = "created", columnDefinition = "datetime default current_timestamp", updatable = false, insertable = false)
    @Generated(value = GenerationTime.INSERT)
    private Date created;

    @Column(name = "updated", columnDefinition = "datetime default current_timestamp on update current_timestamp", updatable = false, insertable = false)
    @Generated(value = GenerationTime.ALWAYS)
    private Date updated;
}
