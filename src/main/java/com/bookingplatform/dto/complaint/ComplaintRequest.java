package com.bookingplatform.dto.complaint;

import com.bookingplatform.enums.ComplaintPriority;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintRequest {

    private String subject;

    private String description;

    private ComplaintPriority priority;

    private Long bookingId;
}