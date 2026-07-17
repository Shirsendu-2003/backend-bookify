package com.bookingplatform.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailabilityRequest {

    private String day;

    private String startTime;

    private String endTime;
}