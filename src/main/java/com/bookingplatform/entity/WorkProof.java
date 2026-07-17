package com.bookingplatform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "work_proofs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkProof {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * One proof for one booking
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "booking_id",
            nullable = false,
            unique = true
    )
    private Booking booking;

    /**
     * Uploaded image path
     */
    @Column(nullable = false)
    private String imageUrl;

    /**
     * Provider current GPS
     */
    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    /**
     * Human readable address
     */
    @Column(length = 500)
    private String address;

    /**
     * Device timestamp
     */
    @Column(nullable = false)
    private LocalDateTime capturedAt;

    /**
     * Optional metadata
     */
    private String deviceName;

    private String networkType;

    private Boolean geoVerified;

    @PrePersist
    public void prePersist() {
        if (capturedAt == null) {
            capturedAt = LocalDateTime.now();
        }

        if (geoVerified == null) {
            geoVerified = true;
        }
    }
}