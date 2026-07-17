package com.bookingplatform.dto.report;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardReportResponse {

    /*
     * ==========================
     * USERS
     * ==========================
     */

    private Long totalUsers;

    private Long totalCustomers;

    private Long totalProviders;

    /*
     * ==========================
     * BOOKINGS
     * ==========================
     */

    private Long totalBookings;

    private Long completedBookings;

    /*
     * ==========================
     * PAYMENTS
     * ==========================
     */

    private BigDecimal totalRevenue;

    /*
     * ==========================
     * REVIEWS
     * ==========================
     */

    private Double averageRating;

    /*
     * ==========================
     * COMPLAINTS
     * ==========================
     */

    private Long totalComplaints;

}