package com.bookingplatform.dto.payment;

import lombok.Data;

@Data
public class CompletePaymentRequest {

    private String transactionId;

    private String paymentIntentId;

    private String signature;
}
