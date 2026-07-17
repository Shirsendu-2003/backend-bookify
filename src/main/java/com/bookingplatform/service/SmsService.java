package com.bookingplatform.service;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromNumber;

    // ==========================================
    // INITIALIZE TWILIO
    // ==========================================

    private void initializeTwilio() {

        try {

            Twilio.init(
                    accountSid,
                    authToken
            );

        } catch (Exception ex) {

            log.error(
                    "Twilio initialization failed",
                    ex
            );

            throw new RuntimeException(
                    "Twilio initialization failed"
            );
        }
    }

    // ==========================================
    // SEND GENERIC SMS
    // ==========================================

    @Async
    public String sendSms(
            String to,
            String messageBody
    ) {

        try {

            initializeTwilio();

            Message message =
                    Message.creator(
                            new com.twilio.type.PhoneNumber(to),
                            new com.twilio.type.PhoneNumber(fromNumber),
                            messageBody
                    ).create();

            log.info(
                    "SMS sent successfully -> To: {} | SID: {}",
                    to,
                    message.getSid()
            );

            return message.getSid();

        } catch (ApiException ex) {

            log.error(
                    "Twilio API error while sending SMS",
                    ex
            );

            throw new RuntimeException(
                    "Failed to send SMS via Twilio"
            );

        } catch (Exception ex) {

            log.error(
                    "Unexpected SMS sending failure",
                    ex
            );

            throw new RuntimeException(
                    "SMS send operation failed"
            );
        }
    }

    // ==========================================
    // SEND OTP SMS
    // ==========================================

    @Async
    public String sendOtpSms(
            String to,
            String otp
    ) {

        String message = String.format(
                "Your Booking Platform OTP is %s. Valid for 10 minutes. Do not share it with anyone.",
                otp
        );

        return sendSms(
                to,
                message
        );
    }

    // ==========================================
    // SEND BOOKING CONFIRMATION SMS
    // ==========================================

    @Async
    public String sendBookingConfirmationSms(
            String to,
            String bookingId
    ) {

        String message = String.format(
                "Your booking has been confirmed successfully. Booking ID: %s. Thank you for using Booking Platform.",
                bookingId
        );

        return sendSms(
                to,
                message
        );
    }

    // ==========================================
    // SEND PAYMENT SUCCESS SMS
    // ==========================================

    @Async
    public String sendPaymentSuccessSms(
            String to,
            String paymentId
    ) {

        String message = String.format(
                "Payment successful. Payment ID: %s. Thank you for your transaction.",
                paymentId
        );

        return sendSms(
                to,
                message
        );
    }

    // ==========================================
    // HEALTH CHECK
    // ==========================================

    public boolean isConfigured() {

        return accountSid != null &&
                !accountSid.isBlank() &&
                authToken != null &&
                !authToken.isBlank() &&
                fromNumber != null &&
                !fromNumber.isBlank();
    }

}