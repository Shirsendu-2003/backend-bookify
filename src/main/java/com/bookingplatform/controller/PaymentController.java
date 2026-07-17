package com.bookingplatform.controller;

import com.bookingplatform.dto.common.ApiResponse;
import com.bookingplatform.dto.common.PaginationResponse;
import com.bookingplatform.dto.payment.PaymentOrderRequest;
import com.bookingplatform.dto.payment.PaymentRequest;
import com.bookingplatform.dto.payment.PaymentResponse;
import com.bookingplatform.dto.payment.RazorpayVerifyRequest;
import com.bookingplatform.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Value("${razorpay.key}")
    private String razorpayKey;

    @Value("${razorpay.secret}")
    private String razorpaySecret;

    /*
     * ==========================
     * CREATE PAYMENT
     * ==========================
     */

    @GetMapping
    public ApiResponse<PaginationResponse<PaymentResponse>>
    getAllPayments(

            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "10"
            )
            int size

    ) {

        return ApiResponse
                .<PaginationResponse<PaymentResponse>>
                        builder()

                .success(true)

                .message(
                        "Payments fetched successfully."
                )

                .data(

                        paymentService
                                .getAllPayments(
                                        page,
                                        size
                                )

                )

                .build();
    }

    @PostMapping
    public ApiResponse<PaymentResponse>
    createPayment(

            @Valid
            @RequestBody
            PaymentRequest request

    ) {

        return ApiResponse
                .<PaymentResponse>builder()

                .success(true)

                .message(
                        "Payment created successfully."
                )

                .data(

                        paymentService
                                .createPayment(
                                        request
                                )

                )

                .build();
    }

    /*
     * ==========================
     * GET PAYMENT BY ID
     * ==========================
     */

    @GetMapping("/{paymentId}")
    public ApiResponse<PaymentResponse>
    getPaymentById(

            @PathVariable
            Long paymentId

    ) {

        return ApiResponse
                .<PaymentResponse>builder()

                .success(true)

                .message(
                        "Payment fetched successfully."
                )

                .data(

                        paymentService
                                .getPaymentById(
                                        paymentId
                                )

                )

                .build();
    }

    /*
     * ==========================
     * BOOKING PAYMENTS
     * ==========================
     */

    @GetMapping("/booking/{bookingId}")
    public ApiResponse
            <PaginationResponse<PaymentResponse>>

    getBookingPayments(

            @PathVariable
            Long bookingId,

            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "10"
            )
            int size

    ) {

        return ApiResponse

                .<PaginationResponse<PaymentResponse>>
                        builder()

                .success(true)

                .message(
                        "Booking payments fetched."
                )

                .data(

                        paymentService
                                .getBookingPayments(

                                        bookingId,

                                        page,

                                        size

                                )

                )

                .build();
    }

    /*
     * ==========================
     * CUSTOMER PAYMENTS
     * ==========================
     */

    @GetMapping("/customer/{customerId}")
    public ApiResponse
            <PaginationResponse<PaymentResponse>>

    getCustomerPayments(

            @PathVariable
            Long customerId,

            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "10"
            )
            int size

    ) {

        return ApiResponse

                .<PaginationResponse<PaymentResponse>>
                        builder()

                .success(true)

                .message(
                        "Customer payments fetched."
                )

                .data(

                        paymentService
                                .getCustomerPayments(

                                        customerId,

                                        page,

                                        size

                                )

                )

                .build();
    }

    /*
     * ==========================
     * SEARCH PAYMENTS
     * ==========================
     */

    @GetMapping("/search")
    public ApiResponse
            <PaginationResponse<PaymentResponse>>

    searchPayments(

            @RequestParam
            String keyword,

            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "10"
            )
            int size

    ) {

        return ApiResponse

                .<PaginationResponse<PaymentResponse>>
                        builder()

                .success(true)

                .message(
                        "Payment search completed."
                )

                .data(

                        paymentService
                                .searchPayments(

                                        keyword,

                                        page,

                                        size

                                )

                )

                .build();
    }

    /*
     * ==========================
     * REFUND PAYMENT
     * ==========================
     */

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(
            @RequestBody PaymentOrderRequest request
    ) throws Exception {

        System.out.println(
                "Booking Amount = "
                        + request.getAmount()
        );

        RazorpayClient razorpayClient =
                new RazorpayClient(
                        razorpayKey,
                        razorpaySecret
                );

        JSONObject options =
                new JSONObject();

        options.put(
                "amount",
                request.getAmount()
                        .multiply(
                                new BigDecimal("100")
                        )
                        .intValue()
        );

        options.put(
                "currency",
                "INR"
        );

        options.put(
                "receipt",
                "BOOKING_"
                        + request.getBookingId()
        );

        System.out.println(
                "RAZORPAY REQUEST = "
                        + options
        );

        Order order =
                razorpayClient.orders.create(
                        options
                );

        System.out.println(
                "RAZORPAY ORDER = "
                        + order
        );

        return ResponseEntity.ok(
                order.toJson().toMap()
        );
    }

    @PostMapping("/verify-payment")
    public ApiResponse<PaymentResponse> verifyPayment(
            @RequestBody RazorpayVerifyRequest request
    ) throws Exception {

        return ApiResponse
                .<PaymentResponse>builder()
                .success(true)
                .message("Payment verified successfully.")
                .data(
                        paymentService.verifyPayment(
                                request,
                                razorpaySecret
                        )
                )
                .build();
    }

}