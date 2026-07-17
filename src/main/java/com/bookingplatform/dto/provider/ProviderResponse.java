package com.bookingplatform.dto.provider;

import com.bookingplatform.enums.ProviderType;
import lombok.*;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderResponse {

    private Long id;

    /*
     * USER INFO
     */

    private Long userId;

    private String name;

    private ProviderType providerType;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String location;

    private String skills;

    private String avatar;

    /*
     * PROVIDER INFO
     */

    private String serviceType;

    private String bio;

    private String experience;

    private BigDecimal hourlyRate;

    private Double averageRating;

    private Integer totalReviews;

    private Boolean available;

    /*
     * LOCATION
     */

    private String city;

    private String state;

    private String country;

    private String zipCode;

    /*
     * DOCUMENTS
     */

    private String profileImageUrl;

    private String governmentIdUrl;

    private String certificateUrl;

    /*
     * AVAILABILITY
     */

    private List<AvailabilityDto> availabilities;

    /*
     * AUDIT
     */

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AvailabilityDto {

        private Long id;

        private DayOfWeek dayOfWeek;

        private LocalTime startTime;

        private LocalTime endTime;

        private Boolean available;

        private LocalTime breakStart;

        private LocalTime breakEnd;

        private String notes;

    }

}