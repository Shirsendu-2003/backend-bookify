package com.bookingplatform.dto.provider;

import com.bookingplatform.enums.ProviderType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderUpdateRequestDto {

    @NotBlank
    private String name;

    @NotNull
    private ProviderType providerType;

    @NotBlank
    private String experience;

    @NotBlank
    private String location;

    @NotBlank
    private String skills;

    @NotNull
    @DecimalMin("0")
    private BigDecimal hourlyRate;

    @NotBlank
    private String bio;

}