package com.bookingplatform.dto.report;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {

    /*
     * ==========================
     * REPORT INFO
     * ==========================
     */

    private String reportName;

    private String reportType;

    private String format;

    /*
     * ==========================
     * FILE INFO
     * ==========================
     */

    private String fileName;

    private String downloadUrl;

    /*
     * ==========================
     * METADATA
     * ==========================
     */

    private LocalDateTime generatedAt;

    private String generatedBy;

}