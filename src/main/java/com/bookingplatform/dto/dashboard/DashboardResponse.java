package com.bookingplatform.dto.dashboard;

import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    /*
     * ==========================
     * USERS
     * ==========================
     */

    private Long totalUsers;

    private Long totalCustomers;

    private Long totalProviders;

    private Long activeUsers;

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
     * PAYMENTS
     * ==========================
     */

    private Long totalPayments;

    private BigDecimal totalRevenue;

    private BigDecimal refundedAmount;

    /*
     * ==========================
     * REVIEWS
     * ==========================
     */

    private Long totalReviews;

    private Double averageRating;

    /*
     * ==========================
     * COMPLAINTS
     * ==========================
     */

    private Long totalComplaints;

    private Long openComplaints;

    private Long resolvedComplaints;

    /*
     * ==========================
     * PROVIDER ANALYTICS
     * ==========================
     */

    private Long availableProviders;

    private Long unavailableProviders;

    /*
     * ==========================
     * CHART DATA
     * (Frontend Dashboard Charts)
     * ==========================
     */

    private Map<String, Long> bookingChart;

    private Map<String, BigDecimal> revenueChart;

}