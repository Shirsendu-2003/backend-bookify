package com.bookingplatform.entity;

import com.bookingplatform.enums.BookingStatus;
import com.bookingplatform.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking extends BaseEntity {

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "provider_id",
            nullable = false
    )
    private Provider provider;

    /*
     * ==========================
     * BOOKING DETAILS
     * ==========================
     */

    @Column(
            nullable = false,
            length = 100
    )
    private String serviceType;

    @Column(
            nullable = false,
            length = 500
    )
    private String description;

    /*
     * ==========================
     * SERVICE ADDRESS
     * ==========================
     */

    @Column(nullable = false, length = 255)
    private String address;

    @Column(length = 120)
    private String city;

    @Column(length = 120)
    private String state;

    @Column(length = 120)
    private String country;

    @Column(length = 20)
    private String zipCode;

    /*
     * ==========================
     * BOOKING DATE & TIME
     * ==========================
     */

    @Column(
            nullable = false,
            name = "booking_date"
    )
    private LocalDate bookingDate;

    @Column(
            nullable = false,
            name = "start_time"
    )
    private LocalTime startTime;

    @Column(
            nullable = true,
            name = "end_time"
    )
    private LocalTime endTime;

    /*
     * ==========================
     * PRICE
     * ==========================
     */

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal amount;

    /*
     * ==========================
     * STATUS
     * ==========================
     */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    /*
     * ==========================
     * EXTRA INFORMATION
     * ==========================
     */

    @Column(length = 500)
    private String cancellationReason;

    @Column(length = 500)
    private String adminNotes;

    /*
     * ==========================
     * PAYMENTS
     * ==========================
     */

    @OneToMany(
            mappedBy = "booking",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Payment> payments;

    /*
     * ==========================
     * REVIEW
     * ==========================
     */

    @OneToOne(
            mappedBy = "booking",
            cascade = CascadeType.ALL
    )
    private Review review;

    /*
     * ==========================
     * COMPLAINTS
     * ==========================
     */

    @OneToMany(
            mappedBy = "booking",
            cascade = CascadeType.ALL
    )
    private List<Complaint> complaints;

    @Enumerated(
            EnumType.STRING
    )
    @Column(
            name = "payment_status"
    )
    private PaymentStatus paymentStatus;


    @Column(length = 10)
    private String startOtp;

    @Column(length = 10)
    private String completionOtp;

    private Boolean startOtpVerified = false;

    private Boolean completionOtpVerified = false;

    private LocalDateTime startOtpGeneratedAt;

    private LocalDateTime completionOtpGeneratedAt;

    private LocalDateTime serviceStartTime;

    private LocalDateTime serviceEndTime;

    private Long totalMinutes;

    @Builder.Default
    private Boolean instantBooking = false;

    private Integer providerIndex;

    private LocalDateTime offerSentAt;

    private Double customerLatitude;

    private Double customerLongitude;

    /*
     * TIMESTAMPS
     */
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        createdAt =
                LocalDateTime.now();

        updatedAt =
                LocalDateTime.now();

        if (status == null) {
            status = BookingStatus.PENDING;
        }

        if (paymentStatus == null) {
            paymentStatus = PaymentStatus.PENDING;
        }

        if (amount == null) {
            amount = BigDecimal.ZERO;
        }

        if (instantBooking == null) {
            instantBooking = false;
        }

        if (providerIndex == null) {
            providerIndex = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {

        updatedAt =
                LocalDateTime.now();
    }

}