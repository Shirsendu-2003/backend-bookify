package com.bookingplatform.service;

import com.bookingplatform.entity.Booking;
import com.bookingplatform.entity.Payment;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InvoicePdfGenerator {

    public static void generate(
            Path file,
            Payment payment
    ) throws Exception {

        Document document =
                new Document(
                        PageSize.A4,
                        36,
                        36,
                        50,
                        50
                );

        PdfWriter.getInstance(
                document,
                Files.newOutputStream(file)
        );

        document.open();

        Booking booking =
                payment.getBooking();

        Font titleFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD,
                        22
                );

        Font headingFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD,
                        12
                );

        Font normalFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA,
                        10
                );

        Font boldFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD,
                        10
                );

        String invoiceNumber =
                "INV-" +
                        System.currentTimeMillis();

        /* ==========================
           HEADER
           ========================== */

        Paragraph title =
                new Paragraph(
                        "BOOKIFY INVOICE",
                        titleFont
                );

        title.setAlignment(
                Element.ALIGN_CENTER
        );

        document.add(title);

        document.add(
                new Paragraph(" ")
        );

        PdfPTable header =
                new PdfPTable(2);

        header.setWidthPercentage(100);

        header.addCell(
                createCell(
                        "Invoice No",
                        boldFont
                )
        );

        header.addCell(
                createCell(
                        invoiceNumber,
                        normalFont
                )
        );

        header.addCell(
                createCell(
                        "Invoice Date",
                        boldFont
                )
        );

        header.addCell(
                createCell(
                        LocalDateTime.now()
                                .format(
                                        DateTimeFormatter.ofPattern(
                                                "dd-MM-yyyy HH:mm"
                                        )
                                ),
                        normalFont
                )
        );

        document.add(header);

        document.add(
                new Paragraph(" ")
        );

        /* ==========================
           CUSTOMER DETAILS
           ========================== */

        document.add(
                new Paragraph(
                        "CUSTOMER DETAILS",
                        headingFont
                )
        );

        document.add(
                new Paragraph(
                        "Name : "
                                + booking.getCustomer()
                                .getFirstName()
                                + " "
                                + booking.getCustomer()
                                .getLastName(),
                        normalFont
                )
        );

        document.add(
                new Paragraph(
                        "Email : "
                                + booking.getCustomer()
                                .getEmail(),
                        normalFont
                )
        );

        document.add(
                new Paragraph(
                        "Phone : "
                                + booking.getCustomer()
                                .getPhone(),
                        normalFont
                )
        );

        document.add(
                new Paragraph(" ")
        );

        /* ==========================
           PROVIDER DETAILS
           ========================== */

        document.add(
                new Paragraph(
                        "TECHNICIAN DETAILS",
                        headingFont
                )
        );

        document.add(
                new Paragraph(
                        "Name : "
                                + booking.getProvider()
                                .getUser()
                                .getFirstName()
                                + " "
                                + booking.getProvider()
                                .getUser()
                                .getLastName(),
                        normalFont
                )
        );

        document.add(
                new Paragraph(
                        "Service : "
                                + booking.getServiceType(),
                        normalFont
                )
        );

        document.add(
                new Paragraph(" ")
        );

        /* ==========================
           BOOKING DETAILS
           ========================== */

        document.add(
                new Paragraph(
                        "BOOKING DETAILS",
                        headingFont
                )
        );

        document.add(
                new Paragraph(
                        "Booking ID : "
                                + booking.getId(),
                        normalFont
                )
        );

        document.add(
                new Paragraph(
                        "Booking Date : "
                                + booking.getBookingDate(),
                        normalFont
                )
        );

        document.add(
                new Paragraph(
                        "Address : "
                                + booking.getAddress(),
                        normalFont
                )
        );

        document.add(
                new Paragraph(" ")
        );

        /* ==========================
           INVOICE TABLE
           ========================== */

        PdfPTable table =
                new PdfPTable(4);

        table.setWidthPercentage(100);

        table.setWidths(
                new float[]{
                        5,
                        1,
                        2,
                        2
                }
        );

        addHeader(table,"Description");
        addHeader(table,"Qty");
        addHeader(table,"Rate");
        addHeader(table,"Amount");

        table.addCell(
                booking.getServiceType()
                        .toString()
        );

        table.addCell("1");

        table.addCell(
                payment.getAmount()
                        .toString()
        );

        table.addCell(
                payment.getAmount()
                        .toString()
        );

        document.add(table);

        document.add(
                new Paragraph(" ")
        );

        /* ==========================
           GST CALCULATION
           ========================== */

        BigDecimal subtotal =
                payment.getAmount();

        BigDecimal gst =
                subtotal.multiply(
                        new BigDecimal("0.18")
                );

        BigDecimal total =
                subtotal.add(gst);

        PdfPTable totals =
                new PdfPTable(2);

        totals.setWidthPercentage(50);

        totals.setHorizontalAlignment(
                Element.ALIGN_RIGHT
        );

        totals.addCell(
                createCell(
                        "Subtotal",
                        boldFont
                )
        );

        totals.addCell(
                createCell(
                        "₹" + subtotal,
                        normalFont
                )
        );

        totals.addCell(
                createCell(
                        "GST (18%)",
                        boldFont
                )
        );

        totals.addCell(
                createCell(
                        "₹" + gst,
                        normalFont
                )
        );

        totals.addCell(
                createCell(
                        "Grand Total",
                        boldFont
                )
        );

        totals.addCell(
                createCell(
                        "₹" + total,
                        boldFont
                )
        );

        document.add(totals);

        document.add(
                new Paragraph(" ")
        );

        /* ==========================
           PAYMENT DETAILS
           ========================== */

        document.add(
                new Paragraph(
                        "PAYMENT DETAILS",
                        headingFont
                )
        );

        document.add(
                new Paragraph(
                        "Payment Method : "
                                + payment.getPaymentMethod(),
                        normalFont
                )
        );

        document.add(
                new Paragraph(
                        "Gateway : "
                                + payment.getPaymentGateway(),
                        normalFont
                )
        );

        document.add(
                new Paragraph(
                        "Transaction ID : "
                                + payment.getTransactionId(),
                        normalFont
                )
        );

        document.add(
                new Paragraph(
                        "Status : "
                                + payment.getStatus(),
                        normalFont
                )
        );

        document.add(
                new Paragraph(
                        "Paid At : "
                                + payment.getPaidAt(),
                        normalFont
                )
        );

        document.add(
                new Paragraph(" ")
        );

        /* ==========================
           FOOTER
           ========================== */

        Paragraph footer =
                new Paragraph(
                        "\nThank you for choosing Bookify.\n" +
                                "support@bookify.com\n" +
                                "www.bookify.com",
                        normalFont
                );

        footer.setAlignment(
                Element.ALIGN_CENTER
        );

        document.add(footer);

        document.close();
    }

    private static PdfPCell createCell(
            String text,
            Font font
    ) {

        PdfPCell cell =
                new PdfPCell(
                        new Phrase(
                                text,
                                font
                        )
                );

        cell.setPadding(5);

        return cell;
    }

    private static void addHeader(
            PdfPTable table,
            String text
    ) {

        PdfPCell cell =
                new PdfPCell(
                        new Phrase(
                                text,
                                FontFactory.getFont(
                                        FontFactory.HELVETICA_BOLD
                                )
                        )
                );

        cell.setHorizontalAlignment(
                Element.ALIGN_CENTER
        );

        table.addCell(cell);
    }
}