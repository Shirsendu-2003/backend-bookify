package com.bookingplatform.controller;

import com.bookingplatform.entity.Invoice;
import com.bookingplatform.exception.ResourceNotFoundException;
import com.bookingplatform.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceRepository invoiceRepository;

    @GetMapping(
            "/booking/{bookingId}/download"
    )
    public ResponseEntity<Resource> downloadInvoice(
            @PathVariable Long bookingId
    ) throws Exception {

        Invoice invoice =
                invoiceRepository
                        .findByBookingId(
                                bookingId
                        )
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Invoice not found"
                                        )
                        );

        Path path =
                Path.of(
                        invoice.getPdfPath()
                );

        Resource resource =
                new UrlResource(
                        path.toUri()
                );

        if (!resource.exists()) {

            throw new ResourceNotFoundException(
                    "Invoice file not found."
            );
        }

        return ResponseEntity.ok()

                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + path.getFileName()
                                + "\""
                )

                .contentType(
                        MediaType.APPLICATION_PDF
                )

                .body(
                        resource
                );
    }
}