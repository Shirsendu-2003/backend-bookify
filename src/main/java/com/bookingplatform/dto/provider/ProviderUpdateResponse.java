package com.bookingplatform.dto.provider;

import com.bookingplatform.enums.ProviderType;
import com.bookingplatform.enums.UpdateRequestStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderUpdateResponse {

    private Long id;

    private Long providerId;

    private String providerName;

    private String name;

    private ProviderType providerType;

    private String experience;

    private String location;

    private String skills;

    private BigDecimal hourlyRate;

    private String bio;

    private UpdateRequestStatus status;

    private String remarks;

    private LocalDateTime requestedAt;

    private LocalDateTime reviewedAt;

    private String reviewedBy;

    private String currentName;

    private String currentProviderType;

    private String currentExperience;

    private String currentLocation;

    private String currentSkills;

    private BigDecimal currentHourlyRate;

    private String currentBio;

}