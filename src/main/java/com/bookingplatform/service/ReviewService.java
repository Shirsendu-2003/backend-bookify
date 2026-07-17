package com.bookingplatform.service;

import com.bookingplatform.dto.common.PaginationResponse;
import com.bookingplatform.dto.review.ReviewRequest;
import com.bookingplatform.dto.review.ReviewResponse;
import com.bookingplatform.entity.*;
import com.bookingplatform.enums.BookingStatus;
import com.bookingplatform.enums.PaymentStatus;
import com.bookingplatform.exception.*;
import com.bookingplatform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final ProviderRepository providerRepository;

    /*
     * ==========================
     * CREATE REVIEW
     * ==========================
     */

    public ReviewResponse createReview(

            Long customerId,

            ReviewRequest request

    ) {

        Booking booking =

                bookingRepository
                        .findById(
                                request.getBookingId()
                        )

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Booking not found."
                                )
                        );

        /*
         * VALIDATE CUSTOMER
         */

        if (!booking.getCustomer()
                .getId()
                .equals(customerId)) {

            throw new BusinessException(
                    "Booking does not belong to customer."
            );
        }

        /*
         * ONE BOOKING → ONE REVIEW
         */

        if (reviewRepository
                .findByBookingId(
                        request.getBookingId()
                )
                .isPresent()) {

            throw new BusinessException(
                    "Review already exists."
            );
        }

        /*
         * BOOKING MUST BE COMPLETED
         */

        if (booking.getStatus() != BookingStatus.COMPLETED) {

            throw new BusinessException(
                    "Review can only be submitted after booking completion."
            );

        }

        /*
         * PAYMENT MUST BE SUCCESS
         */

        if (booking.getPaymentStatus() != PaymentStatus.SUCCESS) {

            throw new BusinessException(
                    "Payment must be completed before submitting a review."
            );

        }

        Provider provider =
                booking.getProvider();

        Review review =
                Review.builder()

                        .booking(
                                booking
                        )

                        .customer(
                                booking.getCustomer()
                        )

                        .provider(
                                provider
                        )

                        .rating(
                                request.getRating()
                        )

                        .comment(
                                request.getComment()
                        )

                        .approved(
                                true
                        )

                        .build();

        Review savedReview =

                reviewRepository.save(
                        review
                );

        /*
         * UPDATE PROVIDER STATS
         */

        updateProviderRating(
                provider.getId()
        );

        return mapReview(
                savedReview
        );
    }

    /*
     * ==========================
     * GET REVIEW BY ID
     * ==========================
     */

    public ReviewResponse getReviewById(
            Long reviewId
    ) {

        Review review =

                reviewRepository
                        .findById(reviewId)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Review not found."
                                )
                        );

        return mapPageSingle(
                review
        );
    }

    /*
     * ==========================
     * PROVIDER REVIEWS
     * ==========================
     */

    @Transactional(readOnly = true)
    public PaginationResponse<ReviewResponse>
    getProviderReviews(

            Long providerId,

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

        Page<Review> reviews =

                reviewRepository
                        .findByProviderId(
                                providerId,
                                pageable
                        );

        return mapPage(
                reviews
        );
    }

    /*
     * ==========================
     * CUSTOMER REVIEWS
     * ==========================
     */

    public PaginationResponse<ReviewResponse>
    getCustomerReviews(

            Long customerId,

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

        Page<Review> reviews =

                reviewRepository
                        .findByCustomerId(
                                customerId,
                                pageable
                        );

        return mapPage(
                reviews
        );
    }
    /*
     * ==========================
     * MODERATE REVIEW
     * ==========================
     */

    public ReviewResponse moderateReview(

            Long reviewId,

            Boolean approved,

            String adminRemark

    ) {

        Review review =

                reviewRepository
                        .findById(reviewId)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Review not found."
                                )
                        );

        review.setApproved(
                approved
        );

        review.setAdminRemark(
                adminRemark
        );

        Review updatedReview =

                reviewRepository.save(
                        review
                );

        /*
         * RECALCULATE PROVIDER STATS
         */

        updateProviderRating(
                review.getProvider()
                        .getId()
        );

        return mapReview(
                updatedReview
        );
    }

    /*
     * ==========================
     * PROVIDER RATING UPDATE
     * ==========================
     */

    private void updateProviderRating(
            Long providerId
    ) {

        Provider provider =

                providerRepository
                        .findById(providerId)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Provider not found."
                                )
                        );

        Double avgRating =

                reviewRepository
                        .getAverageRating(
                                providerId
                        );

        Long totalReviews =

                reviewRepository
                        .countByProviderId(
                                providerId
                        );

        provider.setAverageRating(
                avgRating != null
                        ? avgRating
                        : 0.0
        );

        provider.setTotalReviews(
                totalReviews.intValue()
        );

        providerRepository.save(
                provider
        );
    }

    /*
     * ==========================
     * ENTITY → DTO MAPPER
     * ==========================
     */

    private ReviewResponse mapReview(
            Review review
    ) {

        String customerName =

                review.getCustomer()
                        .getFirstName()

                        +

                        " "

                        +

                        review.getCustomer()
                                .getLastName();

        String providerName =

                review.getProvider()
                        .getUser()
                        .getFirstName()

                        +

                        " "

                        +

                        review.getProvider()
                                .getUser()
                                .getLastName();

        return ReviewResponse
                .builder()

                .id(
                        review.getId()
                )

                .bookingId(
                        review.getBooking()
                                .getId()
                )

                .customerId(
                        review.getCustomer()
                                .getId()
                )

                .customerName(
                        customerName
                )

                .providerId(
                        review.getProvider()
                                .getId()
                )

                .providerName(
                        providerName
                )

                .rating(
                        review.getRating()
                )

                .comment(
                        review.getComment()
                )

                .approved(
                        review.getApproved()
                )

                .adminRemark(
                        review.getAdminRemark()
                )

                .createdAt(
                        review.getCreatedAt()
                )

                .updatedAt(
                        review.getUpdatedAt()
                )

                .build();
    }

    /*
     * ==========================
     * PAGINATION HELPER
     * ==========================
     */

    private PaginationResponse
            <ReviewResponse>

    mapPage(

            Page<Review> page

    ) {

        return PaginationResponse

                .<ReviewResponse>builder()

                .content(

                        page.getContent()

                                .stream()

                                .map(
                                        this::mapReview
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

    /*
     * ==========================
     * SINGLE REVIEW HELPER
     * ==========================
     */

    private ReviewResponse mapPageSingle(
            Review review
    ) {

        return mapReview(
                review
        );
    }
}