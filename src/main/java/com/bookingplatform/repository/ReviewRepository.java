package com.bookingplatform.repository;

import com.bookingplatform.entity.Review;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository
        extends JpaRepository<Review, Long> {

    /*
     * ==========================
     * PROVIDER REVIEWS
     * ==========================
     */

    List<Review>
    findByProviderId(Long providerId);

    Page<Review> findByProviderId(
            Long providerId,
            Pageable pageable
    );

    /*
     * ==========================
     * CUSTOMER REVIEWS
     * ==========================
     */

    Long countByCustomerId(
            Long customerId
    );

    Page<Review> findByCustomerId(
            Long customerId,
            Pageable pageable
    );

    /*
     * ==========================
     * BOOKING REVIEW
     * ==========================
     */

    Optional<Review> findByBookingId(
            Long bookingId
    );

    /*
     * ==========================
     * APPROVAL FILTER
     * ==========================
     */

    Page<Review> findByApproved(
            Boolean approved,
            Pageable pageable
    );

    /*
     * ==========================
     * PROVIDER RATING
     * ==========================
     */

    @Query("""
        SELECT COALESCE(AVG(r.rating),0)
        FROM Review r
        WHERE
            r.provider.id=:providerId
            AND
            r.approved=true
    """)
    Double getAverageRating(
            @Param("providerId") Long providerId
    );

    /*
     * ==========================
     * TOP REVIEWS
     * ==========================
     */

    List<Review>
    findTop10ByApprovedTrueOrderByCreatedAtDesc();

    long countByProviderId(Long providerId);

}