package com.bookingplatform.service;

import com.bookingplatform.dto.report.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PdfReportGenerator {

    public static void generate(
            Path file,
            Object dto
    ) throws Exception {

        Document document =
                new Document(
                        PageSize.A4,
                        36,
                        36,
                        40,
                        40
                );

        PdfWriter.getInstance(
                document,
                Files.newOutputStream(file)
        );

        document.open();

        Font titleFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD,
                        24
                );

        Font sectionFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD,
                        14
                );

        Font normalFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA,
                        10
                );

        /* ==========================
           HEADER
           ========================== */

        Paragraph company =
                new Paragraph(
                        "BOOKIFY",
                        titleFont
                );

        company.setAlignment(
                Element.ALIGN_CENTER
        );

        document.add(company);

        Paragraph subtitle =
                new Paragraph(
                        "Analytics & Reporting System",
                        FontFactory.getFont(
                                FontFactory.HELVETICA,
                                11
                        )
                );

        subtitle.setAlignment(
                Element.ALIGN_CENTER
        );

        document.add(subtitle);

        document.add(
                new Paragraph(" ")
        );

        /* ==========================
           REPORT INFO
           ========================== */

        PdfPTable infoTable =
                new PdfPTable(2);

        infoTable.setWidthPercentage(100);

        infoTable.addCell(
                createInfoCell(
                        "Report ID"
                )
        );

        infoTable.addCell(
                createInfoCell(
                        "RPT-" +
                                System.currentTimeMillis()
                )
        );

        infoTable.addCell(
                createInfoCell(
                        "Generated At"
                )
        );

        infoTable.addCell(
                createInfoCell(
                        LocalDateTime.now()
                                .format(
                                        DateTimeFormatter.ofPattern(
                                                "dd-MM-yyyy HH:mm:ss"
                                        )
                                )
                )
        );

        document.add(infoTable);

        document.add(
                new Paragraph("\n")
        );

        /* ==========================
           DASHBOARD REPORT
           ========================== */

        if(dto instanceof DashboardReportResponse report){

            addSectionTitle(
                    document,
                    "Dashboard Summary",
                    sectionFont
            );

            PdfPTable table =
                    createMetricTable();

            addTableHeader(
                    table,
                    "Metric"
            );

            addTableHeader(
                    table,
                    "Value"
            );

            addRow(
                    table,
                    "Total Users",
                    String.valueOf(
                            report.getTotalUsers()
                    )
            );

            addRow(
                    table,
                    "Total Customers",
                    String.valueOf(
                            report.getTotalCustomers()
                    )
            );

            addRow(
                    table,
                    "Total Technicians",
                    String.valueOf(
                            report.getTotalProviders()
                    )
            );

            addRow(
                    table,
                    "Total Bookings",
                    String.valueOf(
                            report.getTotalBookings()
                    )
            );

            addRow(
                    table,
                    "Completed Bookings",
                    String.valueOf(
                            report.getCompletedBookings()
                    )
            );

            addRow(
                    table,
                    "Total Revenue",
                    "₹" +
                            report.getTotalRevenue()
            );

            addRow(
                    table,
                    "Average Rating",
                    String.valueOf(
                            report.getAverageRating()
                    )
            );

            addRow(
                    table,
                    "Total Complaints",
                    String.valueOf(
                            report.getTotalComplaints()
                    )
            );

            document.add(table);
        }

        /* ==========================
           BOOKING REPORT
           ========================== */

        else if(dto instanceof BookingReportResponse report){

            addSectionTitle(
                    document,
                    "Booking Summary",
                    sectionFont
            );

            PdfPTable table =
                    createMetricTable();

            addTableHeader(
                    table,
                    "Metric"
            );

            addTableHeader(
                    table,
                    "Value"
            );

            addRow(
                    table,
                    "Total Bookings",
                    String.valueOf(
                            report.getTotalBookings()
                    )
            );

            addRow(
                    table,
                    "Pending Bookings",
                    String.valueOf(
                            report.getPendingBookings()
                    )
            );

            addRow(
                    table,
                    "Accepted Bookings",
                    String.valueOf(
                            report.getAcceptedBookings()
                    )
            );

            addRow(
                    table,
                    "Completed Bookings",
                    String.valueOf(
                            report.getCompletedBookings()
                    )
            );

            addRow(
                    table,
                    "Cancelled Bookings",
                    String.valueOf(
                            report.getCancelledBookings()
                    )
            );

            addRow(
                    table,
                    "Estimated Revenue",
                    "₹" +
                            report.getEstimatedRevenue()
            );

            document.add(table);
        }

        /* ==========================
           REVENUE REPORT
           ========================== */

        else if(dto instanceof RevenueReportResponse report){

            addSectionTitle(
                    document,
                    "Revenue Summary",
                    sectionFont
            );

            PdfPTable table =
                    createMetricTable();

            addTableHeader(
                    table,
                    "Metric"
            );

            addTableHeader(
                    table,
                    "Value"
            );

            addRow(
                    table,
                    "Total Payments",
                    String.valueOf(
                            report.getTotalPayments()
                    )
            );

            addRow(
                    table,
                    "Total Revenue",
                    "₹" +
                            report.getTotalRevenue()
            );

            addRow(
                    table,
                    "Refunded Amount",
                    "₹" +
                            report.getRefundedAmount()
            );

            addRow(
                    table,
                    "Net Revenue",
                    "₹" +
                            report.getNetRevenue()
            );

            document.add(table);
        }

        document.add(
                new Paragraph("\n")
        );

        /* ==========================
           FOOTER
           ========================== */

        Paragraph footer =
                new Paragraph(
                        "Generated by Bookify Reporting Engine",
                        FontFactory.getFont(
                                FontFactory.HELVETICA_OBLIQUE,
                                9
                        )
                );

        footer.setAlignment(
                Element.ALIGN_CENTER
        );

        document.add(footer);

        document.close();
    }

    private static PdfPTable createMetricTable()
            throws DocumentException {

        PdfPTable table =
                new PdfPTable(2);

        table.setWidthPercentage(100);

        table.setWidths(
                new float[]{
                        4,
                        2
                }
        );

        return table;
    }

    private static void addSectionTitle(
            Document document,
            String title,
            Font font
    ) throws Exception {

        Paragraph p =
                new Paragraph(
                        title,
                        font
                );

        p.setSpacingAfter(10);

        document.add(p);
    }

    private static void addTableHeader(
            PdfPTable table,
            String text
    ) {

        Font font =
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD,
                        11,
                        BaseColor.WHITE
                );

        PdfPCell cell =
                new PdfPCell(
                        new Phrase(
                                text,
                                font
                        )
                );

        cell.setBackgroundColor(
                new BaseColor(
                        52,
                        73,
                        94
                )
        );

        cell.setPadding(8);

        cell.setHorizontalAlignment(
                Element.ALIGN_CENTER
        );

        table.addCell(cell);
    }

    private static void addRow(
            PdfPTable table,
            String label,
            String value
    ) {

        PdfPCell labelCell =
                new PdfPCell(
                        new Phrase(label)
                );

        PdfPCell valueCell =
                new PdfPCell(
                        new Phrase(value)
                );

        labelCell.setPadding(7);
        valueCell.setPadding(7);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private static PdfPCell createInfoCell(
            String value
    ) {

        PdfPCell cell =
                new PdfPCell(
                        new Phrase(value)
                );

        cell.setPadding(6);

        return cell;
    }
}