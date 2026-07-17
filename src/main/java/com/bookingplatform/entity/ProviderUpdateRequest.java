package com.bookingplatform.entity;

import com.bookingplatform.enums.ProviderType;
import com.bookingplatform.enums.UpdateRequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "provider_update_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderUpdateRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Provider requesting the update
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    /**
     * Requested values
     */
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    private String experience;

    private String location;

    @Column(length = 1000)
    private String skills;

    private BigDecimal hourlyRate;

    @Column(length = 3000)
    private String bio;

    /**
     * Approval status
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UpdateRequestStatus status;

    /**
     * Admin remarks
     */
    @Column(length = 1000)
    private String remarks;

    /**
     * Audit
     */
    private LocalDateTime requestedAt;

    private LocalDateTime reviewedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    @PrePersist
    public void prePersist() {

        if (requestedAt == null) {
            requestedAt = LocalDateTime.now();
        }

        if (status == null) {
            status = UpdateRequestStatus.PENDING;
        }

    }

}