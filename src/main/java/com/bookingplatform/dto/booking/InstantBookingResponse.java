package com.bookingplatform.dto.booking;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InstantBookingResponse {

    private Long bookingId;

    private Long providerId;

    private String providerName;

    private String status;

    private String message;
}