package com.bookingplatform.dto.payment;

import com.bookingplatform.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;

    /*
     * BOOKING INFO
     */

    private Long bookingId;

    /*
     * CUSTOMER INFO
     */

    private Long customerId;

    private String customerName;

    /*
     * PAYMENT DETAILS
     */

    private BigDecimal amount;

    private String paymentMethod;

    private String paymentGateway;

    private String currency;

    private String transactionId;

    private String paymentIntentId;

    /*
     * STATUS
     */

    private PaymentStatus status;

    /*
     * PAYMENT TIMESTAMPS
     */

    private LocalDateTime paidAt;

    private LocalDateTime refundedAt;

    /*
     * REFUND DETAILS
     */

    private Boolean refunded;

    private BigDecimal refundAmount;

    private String refundReason;

    /*
     * EXTRA INFO
     */

    private String notes;

    /*
     * AUDIT
     */

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}