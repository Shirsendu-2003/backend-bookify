package com.bookingplatform.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="provider_availability")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String day;

    private String startTime;

    private String endTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="provider_id")
    private Provider provider;
}