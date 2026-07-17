package com.bookingplatform.dto.notification;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private Long id;

    private String title;

    private String message;

    private Boolean readStatus;

    private LocalDateTime createdAt;

}