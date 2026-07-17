package com.bookingplatform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.File;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(
            String to,
            String subject,
            String body
    ) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendInvoiceEmail(

            String email,

            String customerName,

            String invoiceNumber,

            String pdfPath

    ) {

        try {

            MimeMessage mimeMessage =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            mimeMessage,
                            true
                    );

            helper.setTo(email);

            helper.setSubject(
                    "Invoice - " + invoiceNumber
            );

            helper.setText(
                    """
                    Hello %s,
    
                    Thank you for using Bookify.
    
                    Your payment has been successfully completed.
    
                    Invoice Number: %s
    
                    Please find the invoice attached.
    
                    Regards,
                    Bookify Team
                    """
                            .formatted(
                                    customerName,
                                    invoiceNumber
                            )
            );

            FileSystemResource file =
                    new FileSystemResource(
                            new File(pdfPath)
                    );

            helper.addAttachment(
                    invoiceNumber + ".pdf",
                    file
            );

            mailSender.send(
                    mimeMessage
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to send invoice email",
                    e
            );
        }
    }

}