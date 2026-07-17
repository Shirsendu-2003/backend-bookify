package com.bookingplatform.controller;

import com.bookingplatform.dto.common.ApiResponse;
import com.bookingplatform.dto.common.PaginationResponse;
import com.bookingplatform.dto.review.ReviewRequest;
import com.bookingplatform.dto.review.ReviewResponse;
import com.bookingplatform.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /*
     * ==========================
     * CREATE REVIEW
     * ==========================
     */

    @PostMapping("/customer/{customerId}")
    public ApiResponse<ReviewResponse>
    createReview(

            @PathVariable
            Long customerId,

            @Valid
            @RequestBody
            ReviewRequest request

    ) {

        return ApiResponse
                .<ReviewResponse>builder()

                .success(true)

                .message(
                        "Review created successfully."
                )

                .data(

                        reviewService
                                .createReview(
                                        customerId,
                                        request
                                )

                )

                .build();
    }

    /*
     * ==========================
     * GET REVIEW BY ID
     * ==========================
     */

    @GetMapping("/{reviewId}")
    public ApiResponse<ReviewResponse>
    getReviewById(

            @PathVariable
            Long reviewId

    ) {

        return ApiResponse
                .<ReviewResponse>builder()

                .success(true)

                .message(
                        "Review fetched successfully."
                )

                .data(

                        reviewService
                                .getReviewById(
                                        reviewId
                                )

                )

                .build();
    }

    /*
     * ==========================
     * PROVIDER REVIEWS
     * ==========================
     */

    @GetMapping("/provider/{providerId}")
    public ApiResponse
            <PaginationResponse<ReviewResponse>>

    getProviderReviews(

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

                .<PaginationResponse<ReviewResponse>>
                        builder()

                .success(true)

                .message(
                        "Provider reviews fetched."
                )

                .data(

                        reviewService
                                .getProviderReviews(

                                        providerId,

                                        page,

                                        size

                                )

                )

                .build();
    }

    /*
     * ==========================
     * CUSTOMER REVIEWS
     * ==========================
     */

    @GetMapping("/customer/{customerId}")
    public ApiResponse
            <PaginationResponse<ReviewResponse>>

    getCustomerReviews(

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

                .<PaginationResponse<ReviewResponse>>
                        builder()

                .success(true)

                .message(
                        "Customer reviews fetched."
                )

                .data(

                        reviewService
                                .getCustomerReviews(

                                        customerId,

                                        page,

                                        size

                                )

                )

                .build();
    }

    /*
     * ==========================
     * MODERATE REVIEW
     * ==========================
     */

    @PutMapping("/{reviewId}/moderate")
    public ApiResponse<ReviewResponse>
    moderateReview(

            @PathVariable
            Long reviewId,

            @RequestParam
            Boolean approved,

            @RequestParam(
                    required = false
            )
            String adminRemark

    ) {

        return ApiResponse
                .<ReviewResponse>builder()

                .success(true)

                .message(
                        "Review moderated successfully."
                )

                .data(

                        reviewService
                                .moderateReview(

                                        reviewId,

                                        approved,

                                        adminRemark

                                )

                )

                .build();
    }

}