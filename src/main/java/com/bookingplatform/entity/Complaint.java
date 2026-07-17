package com.bookingplatform.entity;

import com.bookingplatform.enums.ComplaintPriority;
import com.bookingplatform.enums.ComplaintStatus;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "complaints")

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Complaint {

    @Id
    @GeneratedValue(
            strategy =
                    GenerationType.IDENTITY
    )
    private Long id;

    /*
     * ==========================
     * USER
     * ==========================
     */

    @ManyToOne(fetch = FetchType.EAGER)

    @JoinColumn(
            name="customer_id",
            nullable=false
    )

    private User customer;

    @ManyToOne(fetch = FetchType.EAGER)

    @JoinColumn(
            name="provider_id",
            nullable=false
    )
    private Provider provider;

    /*
     * ==========================
     * BOOKING
     * ==========================
     */

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(
            name = "booking_id",
            nullable = true
    )
    private Booking booking;

    /*
     * ==========================
     * COMPLAINT DETAILS
     * ==========================
     */

    @Column(
            nullable = false,
            length = 200
    )
    private String subject;

    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String description;

    /*
     * ==========================
     * PRIORITY
     * ==========================
     */

    @Enumerated(
            EnumType.STRING
    )

    @Column(
            nullable = false
    )

    private ComplaintPriority priority;

    /*
     * ==========================
     * STATUS
     * ==========================
     */

    @Enumerated(
            EnumType.STRING
    )

    @Column(
            nullable = false
    )

    private ComplaintStatus status;

    /*
     * ==========================
     * TIMESTAMPS
     * ==========================
     */

    @Column(
            updatable = false
    )

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate(){

        createdAt =
                LocalDateTime.now();

        updatedAt =
                LocalDateTime.now();

        if(status==null){

            status =
                    ComplaintStatus.OPEN;
        }

        if(priority==null){

            priority =
                    ComplaintPriority.MEDIUM;
        }
    }

    @PreUpdate
    public void onUpdate(){

        updatedAt =
                LocalDateTime.now();
    }
}