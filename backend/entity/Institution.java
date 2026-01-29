package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "institutions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 500)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "registered_gst", nullable = false, length = 15)
    private String registeredGst;

    @Column(name = "company_pan", nullable = false, length = 10)
    private String companyPan;

    @Column(name = "address", length = 1000)
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "pincode", length = 10)
    private String pincode;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "institution_type", nullable = false, length = 100)
    private String institutionType;

    @Column(name = "registration_number", length = 100)
    private String registrationNumber;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    // Bank details
    @Column(name = "bank_account_number", length = 50)
    private String bankAccountNumber;

    @Column(name = "bank_name", length = 200)
    private String bankName;

    @Column(name = "ifsc_code", length
            = 11)
    private String ifscCode;

    @Column(name = "account_holder_name", length = 255)
    private String accountHolderName;

    @Column(name = "active_campaigns")
    private Integer activeCampaigns;

    @Column(name = "created", columnDefinition = "datetime default current_timestamp", updatable = false, insertable = false)
    @Generated(value = GenerationTime.INSERT)
    private Date created;

    @Column(name = "updated", columnDefinition = "datetime default current_timestamp on update current_timestamp", updatable = false, insertable = false)
    @Generated(value = GenerationTime.ALWAYS)
    private Date updated;
}
