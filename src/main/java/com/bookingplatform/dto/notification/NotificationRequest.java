package com.bookingplatform.dto.notification;

import com.bookingplatform.enums.NotificationType;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    /*
     * ==========================
     * USER
     * ==========================
     */

    @NotNull
    private Long userId;

    /*
     * ==========================
     * TYPE
     * ==========================
     */

    @NotNull
    private NotificationType type;

    /*
     * ==========================
     * CONTENT
     * ==========================
     */

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 1000)
    private String message;

    /*
     * ==========================
     * OPTIONAL LINKS
     * ==========================
     */

    private Long bookingId;

    private Long paymentId;

    private String actionUrl;

    /*
     * ==========================
     * DELIVERY FLAGS
     * ==========================
     */

    private Boolean emailSent;

    private Boolean smsSent;

    private Boolean pushSent;

}