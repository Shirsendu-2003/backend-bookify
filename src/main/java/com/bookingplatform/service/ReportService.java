
package com.bookingplatform.service;

import com.bookingplatform.dto.dashboard.DashboardResponse;
import com.bookingplatform.dto.report.*;
import com.bookingplatform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final DashboardService dashboardService;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final ComplaintRepository complaintRepository;
    private final ReviewRepository reviewRepository;

    @Value("${app.report.directory:reports}")
    private String reportDirectory;

    @PostConstruct
    public void init() throws Exception {
        Files.createDirectories(Paths.get(reportDirectory));
    }

    public ReportResponse generateDashboardReport(String format, String generatedBy) throws Exception {

        DashboardResponse dashboard =
                dashboardService.getAdminDashboard();

        DashboardReportResponse dto =
                DashboardReportResponse.builder()

                        .totalUsers(
                                dashboard.getTotalUsers()
                        )

                        .totalCustomers(
                                dashboard.getTotalCustomers()
                        )

                        .totalProviders(
                                dashboard.getTotalProviders()
                        )

                        .totalBookings(
                                dashboard.getTotalBookings()
                        )

                        .completedBookings(
                                dashboard.getCompletedBookings()
                        )

                        .totalRevenue(
                                dashboard.getTotalRevenue()
                        )

                        .averageRating(
                                dashboard.getAverageRating()
                        )

                        .totalComplaints(
                                dashboard.getTotalComplaints()
                        )

                        .build();

        String fileName = "dashboard_" + System.currentTimeMillis() + "." + format.toLowerCase();
        Path file = Paths.get(reportDirectory, fileName);

        // simplified writer (replace with OpenPDF/POI/OpenCSV implementation)
        switch (format.toLowerCase()) {

            case "pdf" ->
                    PdfReportGenerator.generate(
                            file,
                            dto
                    );

            case "xlsx" ->
                    ExcelReportGenerator.generate(
                            file,
                            dto
                    );

            case "csv" ->
                    CsvReportGenerator.generate(
                            file,
                            dto
                    );

            default ->
                    throw new IllegalArgumentException(
                            "Unsupported format"
                    );
        }

        return ReportResponse.builder()
                .reportName("Dashboard Report")
                .reportType("DASHBOARD")
                .format(format.toUpperCase())
                .fileName(fileName)
                .downloadUrl("/api/report/download/" + fileName)
                .generatedAt(LocalDateTime.now())
                .generatedBy(generatedBy)
                .build();
    }

    public ReportResponse generateBookingReport(String format, String generatedBy) throws Exception {

        BookingReportResponse dto =
                BookingReportResponse.builder()
                        .totalBookings(bookingRepository.count())
                        .pendingBookings(bookingRepository.countByStatus(
                                com.bookingplatform.enums.BookingStatus.PENDING))
                        .acceptedBookings(bookingRepository.countByStatus(
                                com.bookingplatform.enums.BookingStatus.ACCEPTED))
                        .completedBookings(bookingRepository.countByStatus(
                                com.bookingplatform.enums.BookingStatus.COMPLETED))
                        .cancelledBookings(bookingRepository.countByStatus(
                                com.bookingplatform.enums.BookingStatus.CANCELLED))
                        .estimatedRevenue(paymentRepository.getTotalRevenue())
                        .bookingChart(Map.of("TOTAL", bookingRepository.count()))
                        .build();

        String fileName = "booking_" + System.currentTimeMillis() + "." + format.toLowerCase();
        Files.writeString(Paths.get(reportDirectory, fileName), dto.toString());

        return ReportResponse.builder()
                .reportName("Booking Report")
                .reportType("BOOKING")
                .format(format)
                .fileName(fileName)
                .downloadUrl("/api/report/download/" + fileName)
                .generatedAt(LocalDateTime.now())
                .generatedBy(generatedBy)
                .build();
    }

    public ReportResponse generateRevenueReport(String format, String generatedBy) throws Exception {

        BigDecimal revenue = paymentRepository.getTotalRevenue();
        BigDecimal refunded = paymentRepository.getTotalRefundedAmount();
        if (revenue == null) revenue = BigDecimal.ZERO;
        if (refunded == null) refunded = BigDecimal.ZERO;

        RevenueReportResponse dto =
                RevenueReportResponse.builder()
                        .totalPayments(paymentRepository.count())
                        .totalRevenue(revenue)
                        .refundedAmount(refunded)
                        .netRevenue(revenue.subtract(refunded))
                        .build();

        String fileName = "revenue_" + System.currentTimeMillis() + "." + format.toLowerCase();
        Files.writeString(Paths.get(reportDirectory, fileName), dto.toString());

        return ReportResponse.builder()
                .reportName("Revenue Report")
                .reportType("REVENUE")
                .format(format)
                .fileName(fileName)
                .downloadUrl("/api/report/download/" + fileName)
                .generatedAt(LocalDateTime.now())
                .generatedBy(generatedBy)
                .build();
    }
}

