package com.bookingplatform.repository;

import com.bookingplatform.entity.Payment;
import com.bookingplatform.enums.PaymentStatus;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    /*
     * ==========================
     * BOOKING PAYMENTS
     * ==========================
     */

    Page<Payment> findByBookingId(
            Long bookingId,
            Pageable pageable
    );

    /*
     * ==========================
     * CUSTOMER PAYMENTS
     * ==========================
     */

    Page<Payment> findByCustomerId(
            Long customerId,
            Pageable pageable
    );

    /*
     * ==========================
     * STATUS FILTER
     * ==========================
     */

    Page<Payment> findByStatus(
            PaymentStatus status,
            Pageable pageable
    );

    /*
     * ==========================
     * TRANSACTION LOOKUP
     * ==========================
     */

    Optional<Payment> findByTransactionId(
            String transactionId
    );

    Optional<Payment> findByPaymentIntentId(
            String paymentIntentId
    );

    /*
     * ==========================
     * DASHBOARD REVENUE
     * ==========================
     */

    @Query("""
        SELECT COALESCE(SUM(p.amount),0)
        FROM Payment p
        WHERE p.status='SUCCESS'
    """)
    BigDecimal getTotalRevenue();

    @Query("""
        SELECT COALESCE(SUM(p.refundAmount),0)
        FROM Payment p
        WHERE p.refunded=true
    """)
    BigDecimal getTotalRefundedAmount();

    /*
     * ==========================
     * SEARCH
     * ==========================
     */

    @Query("""
        SELECT p
        FROM Payment p
        WHERE
            LOWER(p.paymentMethod)
            LIKE LOWER(CONCAT('%',:keyword,'%'))
            OR
            LOWER(p.transactionId)
            LIKE LOWER(CONCAT('%',:keyword,'%'))
    """)
    Page<Payment> searchPayments(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    /*
     * ==========================
     * DASHBOARD COUNTS
     * ==========================
     */

    long countByStatus(
            PaymentStatus status
    );

    Long countByCustomerId(
            Long customerId
    );

    @Query("""
SELECT COALESCE(
SUM(p.amount),0)

FROM Payment p

WHERE p.customer.id=:customerId
""")
    BigDecimal getRevenueByCustomerId(
            @Param("customerId")
            Long customerId
    );

    @Query("""

SELECT COALESCE(
SUM(p.amount),0)

FROM Payment p

WHERE p.booking.provider.id
      = :providerId

""")
    BigDecimal getRevenueByProviderId(

            @Param("providerId")
            Long providerId
    );

    @Query("""

SELECT COUNT(p)

FROM Payment p

WHERE p.booking.provider.id
      = :providerId

""")
    Long countByProviderId(

            @Param("providerId")
            Long providerId
    );

    @Query("""

SELECT p

FROM Payment p

WHERE p.booking.provider.id
      = :providerId

ORDER BY p.createdAt DESC

""")
    List<Payment> findByProviderId(

            @Param("providerId")
            Long providerId
    );

    boolean existsByBookingIdAndStatus(
            Long bookingId,
            PaymentStatus status
    );



}