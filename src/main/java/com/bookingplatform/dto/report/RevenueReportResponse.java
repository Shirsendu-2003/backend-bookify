package com.bookingplatform.dto.report;

import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueReportResponse {

    /*
     * ==========================
     * PAYMENT SUMMARY
     * ==========================
     */

    private Long totalPayments;

    private BigDecimal totalRevenue;

    private BigDecimal refundedAmount;

    private BigDecimal netRevenue;

    /*
     * ==========================
     * ANALYTICS
     * ==========================
     */

    private Map<String, BigDecimal> revenueChart;

}