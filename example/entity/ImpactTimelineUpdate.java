package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "impact_timeline_update")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImpactTimelineUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "campaign_id", nullable = false)
    private Long campaignId;

    @Column(name = "institution_id", nullable = false)
    private Long institutionId;

    @Column(name = "description", nullable = false, length = 1024)
    private String description;

    @Column(name = "proof_file_path", length = 255)
    private String proofFilePath;

    @Column(name = "created_at", columnDefinition = "datetime default current_timestamp", updatable = false, insertable = false)
    @Generated(value = GenerationTime.INSERT)
    private Date createdAt;
}
