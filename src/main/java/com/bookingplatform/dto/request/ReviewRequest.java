package com.bookingplatform.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequest {

    private Integer rating;

    private String comment;

    private String customerName;
}