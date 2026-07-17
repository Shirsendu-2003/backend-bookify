package com.bookingplatform.mapper;

import com.bookingplatform.dto.payment.*;
import com.bookingplatform.entity.Payment;

public class PaymentMapper {

    private PaymentMapper(){}

    public static Payment toEntity(
            PaymentRequest request
    ) {

        if (request == null) {
            return null;
        }

        Payment payment =
                new Payment();

        payment.setAmount(
                request.getAmount()
        );

        payment.setPaymentMethod(
                request.getPaymentMethod()
        );

        payment.setPaymentGateway(
                request.getPaymentGateway()
        );

        payment.setCurrency(
                request.getCurrency()
        );

        payment.setTransactionId(
                request.getTransactionId()
        );

        return payment;
    }

    public static PaymentResponse toResponse(
            Payment payment
    ) {

        if (payment == null) {
            return null;
        }

        return PaymentResponse
                .builder()

                .id(payment.getId())

                .bookingId(
                        payment.getBooking().getId()
                )

                .customerId(
                        payment.getCustomer().getId()
                )

                .customerName(
                        payment.getCustomer().getFirstName()
                                + " "
                                + payment.getCustomer().getLastName()
                )

                .amount(
                        payment.getAmount()
                )

                .paymentMethod(
                        payment.getPaymentMethod()
                )

                .paymentGateway(
                        payment.getPaymentGateway()
                )

                .currency(
                        payment.getCurrency()
                )

                .transactionId(
                        payment.getTransactionId()
                )

                .paymentIntentId(
                        payment.getPaymentIntentId()
                )

                .status(
                        payment.getStatus()
                )

                .paidAt(
                        payment.getPaidAt()
                )

                .refundedAt(
                        payment.getRefundedAt()
                )

                .refunded(
                        payment.getRefunded()
                )

                .refundAmount(
                        payment.getRefundAmount()
                )

                .refundReason(
                        payment.getRefundReason()
                )

                .notes(
                        payment.getNotes()
                )

                .createdAt(
                        payment.getCreatedAt()
                )

                .updatedAt(
                        payment.getUpdatedAt()
                )

                .build();
    }

}