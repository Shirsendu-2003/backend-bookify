package com.bookingplatform.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderResponse {

    private Long id;

    private String name;

    private String providerType;

    private String experience;

    private String location;

    private String bio;

    private String skills;

    private BigDecimal hourlyRate;

    private String avatar;
}