package com.bookingplatform.dto.request;

import com.bookingplatform.enums.ProviderType;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderRequest {

    private String name;

    private ProviderType providerType;

    private String experience;

    private String location;

    private String bio;

    private String skills;

    @DecimalMin("0.0")
    private BigDecimal hourlyRate;

    private String avatar;
}