package com.bookingplatform.entity;

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
@Table(name = "invoices")
public class Invoice extends BaseEntity {

    @Column(
            nullable = false,
            unique = true,
            length = 100
    )
    private String invoiceNumber;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "payment_id",
            nullable = false,
            unique = true
    )
    private Payment payment;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "booking_id",
            nullable = false,
            unique = true
    )
    private Booking booking;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal amount;

    @Column(
            nullable = false
    )
    private LocalDateTime generatedAt;

    @Column(
            nullable = false,
            length = 500
    )
    private String pdfPath;

    @PrePersist
    public void prePersist() {

        if (generatedAt == null) {

            generatedAt =
                    LocalDateTime.now();
        }
    }
}