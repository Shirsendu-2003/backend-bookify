package com.bookingplatform.controller;

import com.bookingplatform.dto.common.ApiResponse;
import com.bookingplatform.dto.report.ReportResponse;
import com.bookingplatform.service.ReportService;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @Value("${app.report.directory:reports}")
    private String reportDirectory;

    /*
     * ==========================
     * DASHBOARD REPORT
     * ==========================
     */

    @PostMapping("/dashboard")
    public ApiResponse<ReportResponse>
    generateDashboardReport(

            @RequestParam
            String format,

            @RequestParam
            String generatedBy

    ) throws Exception {

        return ApiResponse
                .<ReportResponse>builder()

                .success(true)

                .message(
                        "Dashboard report generated successfully."
                )

                .data(

                        reportService
                                .generateDashboardReport(

                                        format,

                                        generatedBy

                                )

                )

                .build();
    }

    /*
     * ==========================
     * BOOKING REPORT
     * ==========================
     */

    @PostMapping("/booking")
    public ApiResponse<ReportResponse>
    generateBookingReport(

            @RequestParam
            String format,

            @RequestParam
            String generatedBy

    ) throws Exception {

        return ApiResponse
                .<ReportResponse>builder()

                .success(true)

                .message(
                        "Booking report generated successfully."
                )

                .data(

                        reportService
                                .generateBookingReport(

                                        format,

                                        generatedBy

                                )

                )

                .build();
    }

    /*
     * ==========================
     * REVENUE REPORT
     * ==========================
     */

    @PostMapping("/revenue")
    public ApiResponse<ReportResponse>
    generateRevenueReport(

            @RequestParam
            String format,

            @RequestParam
            String generatedBy

    ) throws Exception {

        return ApiResponse
                .<ReportResponse>builder()

                .success(true)

                .message(
                        "Revenue report generated successfully."
                )

                .data(

                        reportService
                                .generateRevenueReport(

                                        format,

                                        generatedBy

                                )

                )

                .build();
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadReport(
            @PathVariable String fileName
    ) throws Exception {

        Path filePath =
                Paths.get(
                        reportDirectory,
                        fileName
                );

        Resource resource =
                new UrlResource(
                        filePath.toUri()
                );

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                fileName + "\""
                )
                .contentType(
                        MediaType.APPLICATION_OCTET_STREAM
                )
                .body(resource);
    }



}