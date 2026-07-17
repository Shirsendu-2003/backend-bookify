package com.bookingplatform.dto.report;

import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingReportResponse {

    /*
     * ==========================
     * BOOKINGS
     * ==========================
     */

    private Long totalBookings;

    private Long pendingBookings;

    private Long acceptedBookings;

    private Long completedBookings;

    private Long cancelledBookings;

    /*
     * ==========================
     * REVENUE LINKED TO BOOKINGS
     * ==========================
     */

    private BigDecimal estimatedRevenue;

    /*
     * ==========================
     * ANALYTICS
     * ==========================
     */

    private Map<String, Long> bookingChart;

}