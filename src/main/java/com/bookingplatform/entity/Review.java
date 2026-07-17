package com.bookingplatform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "reviews",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "booking_id",
                                "customer_id"
                        }
                )
        }
)
public class Review extends BaseEntity {



    /*
     * ==========================
     * BOOKING
     * One Booking → One Review
     * ==========================
     */

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "booking_id",
            nullable = false,
            unique = true
    )
    @JsonIgnore
    private Booking booking;

    /*
     * ==========================
     * CUSTOMER
     * ==========================
     */

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "customer_id",
            nullable = false
    )
    private User customer;

    /*
     * ==========================
     * PROVIDER
     * ==========================
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="provider_id")
    @JsonIgnore
    private Provider provider;



    /*
     * ==========================
     * REVIEW DETAILS
     * ==========================
     */

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer rating;

    @Column(
            length = 1000
    )
    private String comment;

    /*
     * ==========================
     * MODERATION
     * ==========================
     */

    @Builder.Default
    @Column(nullable = false)
    private Boolean approved = true;

    @Column(length = 500)
    private String adminRemark;

    private String customerName;

}