package com.bookingplatform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "availabilities",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "provider_id",
                                "day_of_week",
                                "start_time",
                                "end_time"
                        }
                )
        }
)
public class Availability extends BaseEntity {

    /*
     * ==========================
     * PROVIDER → AVAILABILITY
     * ==========================
     */

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "provider_id",
            nullable = false
    )
    @JsonIgnore
    private Provider provider;

    /*
     * ==========================
     * WORKING DAY
     * ==========================
     */

    @Enumerated(EnumType.STRING)
    @Column(
            name = "day_of_week",
            nullable = false,
            length = 20
    )
    private DayOfWeek dayOfWeek;

    /*
     * ==========================
     * TIME SLOT
     * ==========================
     */

    @Column(
            name = "start_time",
            nullable = false
    )
    private LocalTime startTime;

    @Column(
            name = "end_time",
            nullable = false
    )
    private LocalTime endTime;

    /*
     * ==========================
     * AVAILABILITY STATUS
     * ==========================
     */

    @Builder.Default
    @Column(nullable = false)
    private Boolean available = true;

    /*
     * ==========================
     * OPTIONAL BREAK TIME
     * ==========================
     */

    private LocalTime breakStart;

    private LocalTime breakEnd;

    /*
     * ==========================
     * OPTIONAL NOTES
     * ==========================
     */

    @Column(length = 300)
    private String notes;

    private String day;

}