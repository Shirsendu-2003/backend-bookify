package com.bookingplatform.service;

import com.bookingplatform.dto.common.PaginationResponse;
import com.bookingplatform.dto.payment.PaymentRequest;
import com.bookingplatform.dto.payment.PaymentResponse;
import com.bookingplatform.dto.payment.RazorpayVerifyRequest;
import com.bookingplatform.entity.Booking;
import com.bookingplatform.entity.Invoice;
import com.bookingplatform.entity.Payment;
import com.bookingplatform.entity.User;
import com.bookingplatform.enums.BookingStatus;
import com.bookingplatform.enums.PaymentStatus;
import com.bookingplatform.exception.BusinessException;
import com.bookingplatform.exception.ResourceNotFoundException;
import com.bookingplatform.repository.BookingRepository;
import com.bookingplatform.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.razorpay.Utils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final InvoiceService invoiceService;
    private final EmailService emailService;

    /*
     * ==========================
     * CREATE PAYMENT
     * ==========================
     */

    public PaymentResponse createPayment(
            PaymentRequest request
    ){

        Booking booking =
                bookingRepository
                        .findById(
                                request.getBookingId()
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Booking not found."
                                )
                        );

        if(
                booking.getStatus()
                        != BookingStatus.COMPLETED
        ){
            throw new BusinessException(
                    "Booking must be completed before payment."
            );
        }

        if(
                booking.getPaymentStatus()
                        == PaymentStatus.SUCCESS
        ){
            throw new BusinessException(
                    "Payment already completed."
            );
        }

        if(
                request.getTransactionId() != null
                        &&
                        paymentRepository
                                .findByTransactionId(
                                        request.getTransactionId()
                                )
                                .isPresent()
        ){
            throw new BusinessException(
                    "Transaction ID already exists."
            );
        }

        Payment payment =
                Payment.builder()

                        .booking(
                                booking
                        )

                        .customer(
                                booking.getCustomer()
                        )

                        .amount(
                                booking.getAmount()
                        )

                        .paymentMethod(
                                request.getPaymentMethod()
                        )

                        .paymentGateway(
                                request.getPaymentGateway()
                        )

                        .currency(
                                request.getCurrency()
                        )

                        .transactionId(
                                request.getTransactionId()
                        )
                        .paymentIntentId(
                                request.getPaymentIntentId()
                        )

                        .status(
                                PaymentStatus.SUCCESS
                        )

                        .paidAt(
                                LocalDateTime.now()
                        )

                        .build();

        Payment saved =
                paymentRepository.save(
                        payment
                );

        booking.setPaymentStatus(
                PaymentStatus.SUCCESS
        );

        bookingRepository.save(
                booking
        );

        Invoice invoice =
                invoiceService.generateInvoice(
                        saved
                );

        emailService.sendInvoiceEmail(

                booking.getCustomer()
                        .getEmail(),

                booking.getCustomer()
                        .getFirstName(),

                invoice.getInvoiceNumber(),

                invoice.getPdfPath()
        );

        return mapPayment(saved);
    }

    /*
     * ==========================
     * GET PAYMENT BY ID
     * ==========================
     */

    public PaymentResponse getPaymentById(
            Long paymentId
    ) {

        Payment payment =

                paymentRepository
                        .findById(paymentId)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Payment not found."
                                )
                        );

        return mapPayment(
                payment
        );
    }

    /*
     * ==========================
     * BOOKING PAYMENTS
     * ==========================
     */

    public PaginationResponse<PaymentResponse>
    getBookingPayments(

            Long bookingId,

            int page,

            int size

    ) {

        Pageable pageable =

                PageRequest.of(
                        page,
                        size,
                        Sort.by("id")
                                .descending()
                );

        Page<Payment> payments =

                paymentRepository
                        .findByBookingId(
                                bookingId,
                                pageable
                        );

        return mapPage(
                payments
        );
    }

    /*
     * ==========================
     * CUSTOMER PAYMENTS
     * ==========================
     */

    public PaginationResponse<PaymentResponse> getCustomerPayments(
            Long customerId,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("id").descending()
        );

        Page<Payment> payments =
                paymentRepository.findByCustomerId(
                        customerId,
                        pageable
                );

        System.out.println("Payments found = " + payments.getTotalElements());

        for (Payment payment : payments.getContent()) {

            System.out.println("==========");
            System.out.println("Payment ID = " + payment.getId());

            try {

                System.out.println("Booking = " +
                        payment.getBooking().getId());

                System.out.println("Customer = " +
                        payment.getCustomer().getId());

                PaymentResponse response =
                        mapPayment(payment);

                System.out.println("Mapped = " + response.getId());

            } catch (Exception e) {

                e.printStackTrace();

                throw e;
            }
        }

        return mapPage(payments);
    }

    /*
     * ==========================
     * SEARCH PAYMENTS
     * ==========================
     */

    public PaginationResponse<PaymentResponse>
    searchPayments(

            String keyword,

            int page,

            int size

    ) {

        Pageable pageable =

                PageRequest.of(
                        page,
                        size
                );

        Page<Payment> payments =

                paymentRepository
                        .searchPayments(
                                keyword,
                                pageable
                        );

        return mapPage(
                payments
        );
    }
    /*
     * ==========================
     * REFUND PAYMENT
     * ==========================
     */

    public PaymentResponse refundPayment(

            Long paymentId,

            String reason

    ) {

        Payment payment =

                paymentRepository
                        .findById(paymentId)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Payment not found."
                                )
                        );

        validateRefund(payment);

        payment.setRefunded(
                true
        );

        payment.setRefundReason(
                reason
        );

        payment.setRefundAmount(
                payment.getAmount()
        );

        payment.setRefundedAt(
                LocalDateTime.now()
        );

        payment.setStatus(
                PaymentStatus.REFUNDED
        );

        Payment savedPayment =

                paymentRepository.save(
                        payment
                );

        return mapPayment(
                savedPayment
        );
    }

    /*
     * ==========================
     * REFUND VALIDATION
     * ==========================
     */

    private void validateRefund(
            Payment payment
    ) {

        if (payment.getRefunded()) {

            throw new BusinessException(
                    "Payment already refunded."
            );
        }

        if (payment.getStatus()
                != PaymentStatus.SUCCESS) {

            throw new BusinessException(
                    "Only successful payments can be refunded."
            );
        }
    }

    /*
     * ==========================
     * ENTITY → DTO MAPPER
     * ==========================
     */

    private PaymentResponse mapPayment(
            Payment payment
    ) {

        String customerName =

                payment.getCustomer()
                        .getFirstName()

                        +

                        " "

                        +

                        payment.getCustomer()
                                .getLastName();

        return PaymentResponse
                .builder()

                .id(
                        payment.getId()
                )

                .bookingId(
                        payment.getBooking()
                                .getId()
                )

                .customerId(
                        payment.getCustomer()
                                .getId()
                )

                .customerName(
                        customerName
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

    /*
     * ==========================
     * PAGINATION HELPER
     * ==========================
     */

    private PaginationResponse
            <PaymentResponse>

    mapPage(

            Page<Payment> page

    ) {

        return PaginationResponse

                .<PaymentResponse>builder()

                .content(

                        page.getContent()

                                .stream()

                                .map(
                                        this::mapPayment
                                )

                                .toList()

                )

                .page(
                        page.getNumber()
                )

                .size(
                        page.getSize()
                )

                .totalElements(
                        page.getTotalElements()
                )

                .totalPages(
                        page.getTotalPages()
                )

                .first(
                        page.isFirst()
                )

                .last(
                        page.isLast()
                )

                .build();
    }
    public PaginationResponse<PaymentResponse>
    getAllPayments(

            int page,
            int size

    ) {

        Pageable pageable =

                PageRequest.of(
                        page,
                        size
                );

        Page<Payment> paymentPage =

                paymentRepository
                        .findAll(pageable);

        List<PaymentResponse> content =

                paymentPage
                        .getContent()
                        .stream()
                        .map(this::mapPayment)
                        .toList();

        return PaginationResponse
                .<PaymentResponse>builder()

                .content(content)

                .page(
                        paymentPage
                                .getNumber()
                )

                .size(
                        paymentPage
                                .getSize()
                )

                .totalElements(
                        paymentPage
                                .getTotalElements()
                )

                .totalPages(
                        paymentPage
                                .getTotalPages()
                )

                .first(
                        paymentPage
                                .isFirst()
                )

                .last(
                        paymentPage
                                .isLast()
                )

                .build();
    }


    public PaymentResponse verifyPayment(
            RazorpayVerifyRequest request,
            String razorpaySecret
    ) throws Exception {

        String payload =
                request.getRazorpayOrderId()
                        + "|"
                        + request.getRazorpayPaymentId();

        boolean valid =
                Utils.verifySignature(
                        payload,
                        request.getRazorpaySignature(),
                        razorpaySecret
                );

        if (!valid) {
            throw new BusinessException(
                    "Invalid Razorpay Signature"
            );
        }

        PaymentRequest paymentRequest =
                new PaymentRequest();

        paymentRequest.setBookingId(
                request.getBookingId()
        );

        paymentRequest.setPaymentMethod(
                request.getPaymentMethod()
        );

        paymentRequest.setPaymentGateway(
                "RAZORPAY"
        );

        paymentRequest.setCurrency(
                request.getCurrency()
        );

        paymentRequest.setTransactionId(
                request.getRazorpayPaymentId()
        );

        paymentRequest.setPaymentIntentId(
                request.getRazorpayOrderId()
        );

        return createPayment(
                paymentRequest
        );
    }
}