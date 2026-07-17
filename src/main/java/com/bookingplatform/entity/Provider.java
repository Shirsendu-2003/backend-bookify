package com.bookingplatform.entity;

import com.bookingplatform.entity.ProviderService;
import com.bookingplatform.enums.VerificationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@JsonIgnoreProperties({
        "hibernateLazyInitializer",
        "handler"
})
@Table(name="providers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Provider extends BaseEntity {

    private String name;

    private String providerType;

    private String experience;

    private String location;

    @Column(length = 3000)
    private String bio;

    private String skills;

    private String avatar;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({
            "roles",
            "password",
            "provider",
            "bookings",
            "notifications",
            "reviews"
    })
    private User user;

    @Builder.Default
    @OneToMany(
            mappedBy = "provider",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JsonIgnoreProperties("provider")
    private List<ProviderService> services =
            new ArrayList<>();

    @Builder.Default
    @OneToMany(
            mappedBy="provider",
            cascade=CascadeType.ALL,
            orphanRemoval=true,
            fetch = FetchType.EAGER

    )
    @JsonIgnoreProperties("provider")
    private List<Review> reviews =
            new ArrayList<>();

    @Builder.Default
    @OneToMany(
            mappedBy="provider",
            cascade=CascadeType.ALL,
            orphanRemoval=true,
            fetch = FetchType.EAGER
    )
    @JsonIgnoreProperties("provider")
    private List<Availability> availabilities =
            new ArrayList<>();

    @Builder.Default
    private Boolean available = true;

    @Builder.Default
    private Double averageRating = 0.0;

    @Builder.Default
    private Integer totalReviews = 0;

    @Transient
    private Long completedBookings = 0L;

    @Builder.Default
    private BigDecimal hourlyRate =
            BigDecimal.ZERO;

    private String city;
    private String state;
    private String country;
    private String zipCode;

    private String profileImageUrl;
    private String governmentIdUrl;
    private String certificateUrl;

    @Column(name="service_type")
    private String serviceType;

    private String governmentIdType;

    private String governmentIdNumber;

    private String governmentIdPath;

    private String certificatePath;

    @Enumerated(EnumType.STRING)
    private VerificationStatus
            verificationStatus =
            VerificationStatus.PENDING;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    private Boolean online = true;


}