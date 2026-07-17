package com.bookingplatform.dto.provider;

import com.bookingplatform.enums.ProviderType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderRequest {

    @NotBlank
    private String serviceType;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Provider type is required")
    private ProviderType providerType;

    private String bio;

    private String experience;

    private String location;

    private String avatar;

    @DecimalMin("0.0")
    @NotNull
    private BigDecimal hourlyRate;

    private Boolean available;



    private String city;

    private String state;

    private String country;

    private String zipCode;

    private String profileImageUrl;

    private String governmentIdUrl;

    private String certificateUrl;

    private List<AvailabilityRequest> availabilities;

    private String skills;
}