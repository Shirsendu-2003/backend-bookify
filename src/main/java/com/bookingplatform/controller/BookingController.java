package com.bookingplatform.controller;

import com.bookingplatform.dto.booking.*;
import com.bookingplatform.dto.common.ApiResponse;
import com.bookingplatform.dto.common.PaginationResponse;
import com.bookingplatform.entity.Booking;
import com.bookingplatform.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    /*
     * ==========================
     * CREATE BOOKING
     * ==========================
     */

    @GetMapping
    public ApiResponse<
            PaginationResponse<BookingResponse>
            > getAllBookings(

            @RequestParam(
                    defaultValue="0"
            )
            int page,

            @RequestParam(
                    defaultValue="10"
            )
            int size
    ){

        return ApiResponse
                .<PaginationResponse<
                        BookingResponse
                        >>builder()

                .success(true)

                .message(
                        "Bookings fetched."
                )

                .data(

                        bookingService
                                .getAllBookings(
                                        page,
                                        size
                                )

                )

                .build();
    }

    @DeleteMapping("/{bookingId}")
    public ApiResponse<Void>
    deleteBooking(

            @PathVariable
            Long bookingId

    ){

        bookingService
                .deleteBooking(
                        bookingId
                );

        return ApiResponse
                .<Void>builder()
                .success(true)
                .message(
                        "Booking deleted."
                )
                .build();
    }
    @PutMapping("/{bookingId}")
    public ApiResponse<
            BookingResponse
            > updateBooking(

            @PathVariable
            Long bookingId,

            @RequestBody
            BookingRequest request

    ){

        return ApiResponse
                .<BookingResponse>
                        builder()

                .success(true)

                .message(
                        "Booking updated."
                )

                .data(

                        bookingService
                                .updateBooking(
                                        bookingId,
                                        request
                                )

                )

                .build();
    }

    @PostMapping("/customer/{customerId}")
    public ApiResponse<BookingResponse>
    createBooking(

            @PathVariable
            Long customerId,

            @Valid
            @RequestBody
            BookingRequest request

    ) {

        return ApiResponse
                .<BookingResponse>builder()

                .success(true)

                .message(
                        "Booking created successfully."
                )

                .data(

                        bookingService
                                .createBooking(

                                        customerId,

                                        request

                                )

                )

                .build();
    }

    /*
     * ==========================
     * GET BOOKING BY ID
     * ==========================
     */

    @GetMapping("/{bookingId}")
    public ApiResponse<BookingResponse>
    getBookingById(

            @PathVariable
            Long bookingId

    ) {

        return ApiResponse
                .<BookingResponse>builder()

                .success(true)

                .message(
                        "Booking fetched successfully."
                )

                .data(

                        bookingService
                                .getBookingById(
                                        bookingId
                                )

                )

                .build();
    }

    /*
     * ==========================
     * CUSTOMER BOOKINGS
     * ==========================
     */

    @GetMapping("/customer/{customerId}")
    public ApiResponse
            <PaginationResponse<BookingResponse>>

    getCustomerBookings(

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

                .<PaginationResponse<BookingResponse>>
                        builder()

                .success(true)

                .message(
                        "Customer bookings fetched."
                )

                .data(

                        bookingService
                                .getCustomerBookings(

                                        customerId,

                                        page,

                                        size

                                )

                )

                .build();
    }

    /*
     * ==========================
     * PROVIDER BOOKINGS
     * ==========================
     */

    @GetMapping("/provider/{providerId}")
    public ApiResponse
            <PaginationResponse<BookingResponse>>

    getProviderBookings(

            @PathVariable
            Long providerId,

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

                .<PaginationResponse<BookingResponse>>
                        builder()

                .success(true)

                .message(
                        "Provider bookings fetched."
                )

                .data(

                        bookingService
                                .getProviderBookings(

                                        providerId,

                                        page,

                                        size

                                )

                )

                .build();
    }

    /*
     * ==========================
     * SEARCH BOOKINGS
     * ==========================
     */

    @GetMapping("/search")
    public ApiResponse
            <PaginationResponse<BookingResponse>>

    searchBookings(

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

                .<PaginationResponse<BookingResponse>>
                        builder()

                .success(true)

                .message(
                        "Booking search completed."
                )

                .data(

                        bookingService
                                .searchBookings(

                                        keyword,

                                        page,

                                        size

                                )

                )

                .build();
    }

    /*
     * ==========================
     * UPDATE BOOKING STATUS
     * ==========================
     */

    @PutMapping("/{bookingId}/status")
    public ApiResponse<BookingResponse>
    updateBookingStatus(

            @PathVariable
            Long bookingId,

            @Valid
            @RequestBody
            UpdateBookingStatusRequest request

    ) {

        return ApiResponse
                .<BookingResponse>builder()

                .success(true)

                .message(
                        "Booking status updated."
                )

                .data(

                        bookingService
                                .updateBookingStatus(

                                        bookingId,

                                        request

                                )

                )

                .build();
    }

    @PutMapping("/{bookingId}/accept")
    public ApiResponse<BookingResponse> acceptBooking(
            @PathVariable Long bookingId
    ) {

        return ApiResponse
                .<BookingResponse>builder()
                .success(true)
                .message("Booking accepted.")
                .data(
                        bookingService.acceptBooking(
                                bookingId
                        )
                )
                .build();
    }

    @PutMapping("/{bookingId}/reject")
    public ApiResponse<BookingResponse> rejectBooking(
            @PathVariable Long bookingId
    ) {

        return ApiResponse
                .<BookingResponse>builder()
                .success(true)
                .message("Booking rejected.")
                .data(
                        bookingService.rejectBooking(
                                bookingId
                        )
                )
                .build();
    }

    @PostMapping("/{bookingId}/verify-start-otp")
    public ApiResponse<BookingResponse> verifyStartOtp(

            @PathVariable Long bookingId,

            @Valid
            @RequestBody OtpVerificationRequest request
    ) {

        return ApiResponse
                .<BookingResponse>builder()
                .success(true)
                .message("Start OTP verified.")
                .data(
                        bookingService.verifyStartOtp(
                                bookingId,
                                request.getOtp()
                        )
                )
                .build();
    }



    @PostMapping("/{bookingId}/verify-completion-otp")
    public ApiResponse<BookingResponse> verifyCompletionOtp(

            @PathVariable Long bookingId,

            @Valid
            @RequestBody OtpVerificationRequest request
    ) {

        return ApiResponse
                .<BookingResponse>builder()
                .success(true)
                .message("Booking completed successfully.")
                .data(
                        bookingService.verifyCompletionOtp(
                                bookingId,
                                request.getOtp()
                        )
                )
                .build();
    }

    @PostMapping(
            value = "/{bookingId}/upload-proof",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiResponse<Void> uploadWorkProof(

            @PathVariable
            Long bookingId,

            @RequestPart("photo")
            MultipartFile photo,

            @RequestParam
            Double latitude,

            @RequestParam
            Double longitude,

            @RequestParam(required = false)
            String address

    ) throws IOException {

        bookingService.uploadWorkProof(
                bookingId,
                photo,
                latitude,
                longitude,
                address
        );

        return ApiResponse
                .<Void>builder()
                .success(true)
                .message("Work proof uploaded successfully.")
                .build();
    }


    @PostMapping("/{bookingId}/finish-work")
    public ApiResponse<BookingResponse> finishWork(

            @PathVariable
            Long bookingId

    ){

        return ApiResponse
                .<BookingResponse>builder()

                .success(true)

                .message(
                        "Work finished successfully."
                )

                .data(
                        bookingService.finishWork(
                                bookingId
                        )
                )

                .build();
    }

    @PostMapping("/instant")
    public ApiResponse<InstantBookingResponse>
    createInstantBooking(

            @RequestBody
            InstantBookingRequest request
    ) {

        return ApiResponse
                .<InstantBookingResponse>builder()
                .success(true)
                .message(
                        "Instant booking created."
                )
                .data(
                        bookingService
                                .createInstantBooking(
                                        request
                                )
                )
                .build();
    }




}