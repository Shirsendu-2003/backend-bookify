package com.bookingplatform.dto.payment;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotNull
    private Long bookingId;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal amount;

    @NotBlank
    private String paymentMethod;

    /*
     * OPTIONAL
     * Stripe / Razorpay / PayPal / UPI
     */

    private String paymentGateway;

    private String currency;

    private String transactionId;

    private String paymentIntentId;
}