package com.bookingplatform.dto.dashboard;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettingsRequest {

    private String platformName;

    private String supportEmail;

    private Boolean notifications;

    private Boolean maintenance;

    private String defaultTheme;
}