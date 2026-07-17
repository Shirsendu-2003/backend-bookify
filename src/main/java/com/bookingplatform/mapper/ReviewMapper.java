package com.bookingplatform.mapper;

import com.bookingplatform.dto.review.*;
import com.bookingplatform.entity.Review;

public class ReviewMapper {

    private ReviewMapper(){}

    public static Review toEntity(
            ReviewRequest request
    ) {

        if (request == null) {
            return null;
        }

        Review review =
                new Review();

        review.setRating(
                request.getRating()
        );

        review.setComment(
                request.getComment()
        );

        return review;
    }

    public static ReviewResponse toResponse(
            Review review
    ) {

        if (review == null) {
            return null;
        }

        return ReviewResponse
                .builder()

                .id(review.getId())

                .bookingId(
                        review.getBooking().getId()
                )

                .customerId(
                        review.getCustomer().getId()
                )

                .customerName(
                        review.getCustomer().getFirstName()
                                + " "
                                + review.getCustomer().getLastName()
                )

                .providerId(
                        review.getProvider().getId()
                )

                .providerName(
                        review.getProvider()
                                .getUser()
                                .getFirstName()
                                + " "
                                +
                                review.getProvider()
                                        .getUser()
                                        .getLastName()
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

}