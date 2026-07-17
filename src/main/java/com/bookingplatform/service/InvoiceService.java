package com.bookingplatform.service;

import com.bookingplatform.entity.Invoice;
import com.bookingplatform.entity.Payment;
import com.bookingplatform.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.nio.file.Files;

import java.nio.file.Path;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public Invoice generateInvoice(
            Payment payment
    ) {

        try {

            String invoiceNo =
                    "INV-" +
                            System.currentTimeMillis();

            Path path =
                    Path.of(
                            "uploads/invoices/"
                                    + invoiceNo
                                    + ".pdf"
                    );

            Files.createDirectories(
                    path.getParent()
            );

            InvoicePdfGenerator.generate(
                    path,
                    payment
            );

            Invoice invoice =
                    Invoice.builder()

                            .invoiceNumber(
                                    invoiceNo
                            )

                            .booking(
                                    payment.getBooking()
                            )

                            .payment(
                                    payment
                            )

                            .amount(
                                    payment.getAmount()
                            )

                            .generatedAt(
                                    LocalDateTime.now()
                            )

                            .pdfPath(
                                    path.toString()
                            )

                            .build();

            return invoiceRepository.save(
                    invoice
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Invoice generation failed"
            );
        }
    }
}