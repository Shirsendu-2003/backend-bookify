package com.bookingplatform.dto.provider;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProviderAdminResponse {

    private Long id;
    private String name;
    private String email;
    private String providerType;
    private String verificationStatus;
    private String governmentIdUrl;
    private String certificateUrl;
    private LocalDateTime createdAt;
}
