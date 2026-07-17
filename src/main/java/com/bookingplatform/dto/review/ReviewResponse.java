package com.bookingplatform.dto.review;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {

    private Long id;

    /*
     * BOOKING
     */

    private Long bookingId;

    /*
     * CUSTOMER
     */

    private Long customerId;

    private String customerName;

    /*
     * PROVIDER
     */

    private Long providerId;

    private String providerName;

    /*
     * REVIEW DETAILS
     */

    private Integer rating;

    private String comment;

    /*
     * MODERATION
     */

    private Boolean approved;

    private String adminRemark;

    /*
     * AUDIT
     */

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}