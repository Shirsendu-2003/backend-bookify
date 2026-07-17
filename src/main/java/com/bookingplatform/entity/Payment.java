package com.bookingplatform.entity;

import com.bookingplatform.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {

    /*
     * ==========================
     * BOOKING
     * ==========================
     */

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "booking_id",
            nullable = false
    )
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
     * PAYMENT DETAILS
     * ==========================
     */

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal amount;

    @Column(
            nullable = false,
            length = 100
    )
    private String paymentMethod;

    @Column(
            unique = true,
            length = 150
    )
    private String transactionId;

    @Column(
            unique = true,
            length = 150
    )
    private String paymentIntentId;

    /*
     * ==========================
     * PAYMENT STATUS
     * ==========================
     */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    /*
     * ==========================
     * PROVIDER / GATEWAY INFO
     * ==========================
     */

    @Column(length = 100)
    private String paymentGateway;

    @Column(length = 100)
    private String currency;

    /*
     * ==========================
     * TIMESTAMPS
     * ==========================
     */

    private LocalDateTime paidAt;

    private LocalDateTime refundedAt;

    /*
     * ==========================
     * REFUND DETAILS
     * ==========================
     */

    @Builder.Default
    @Column(nullable = false)
    private Boolean refunded = false;

    @Column(
            precision = 12,
            scale = 2
    )
    private BigDecimal refundAmount;

    @Column(length = 500)
    private String refundReason;

    /*
     * ==========================
     * EXTRA DETAILS
     * ==========================
     */

    @Column(length = 500)
    private String notes;

}