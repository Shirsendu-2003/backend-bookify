package com.bookingplatform.dto.complaint;

import com.bookingplatform.enums.ComplaintStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintResponse {

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
     * COMPLAINT DETAILS
     */

    private String subject;

    private String description;

    private String priority;

    private String attachmentUrl;

    /*
     * STATUS
     */

    private ComplaintStatus status;

    /*
     * ADMIN RESOLUTION
     */

    private String resolution;

    private String adminRemark;

    /*
     * AUDIT
     */

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}