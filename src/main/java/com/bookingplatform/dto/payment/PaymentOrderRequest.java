package com.bookingplatform.dto.payment;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentOrderRequest {

    private Long bookingId;

    private BigDecimal amount;
}