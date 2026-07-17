package com.bookingplatform.repository;

import com.bookingplatform.entity.Booking;
import com.bookingplatform.entity.Provider;
import com.bookingplatform.enums.BookingStatus;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository
        extends JpaRepository<Booking,Long>{

    /*
     * CUSTOMER BOOKINGS
     */

    @EntityGraph(
            attributePaths={
                    "customer",
                    "provider",
                    "provider.user"
            }
    )
    Page<Booking> findByCustomerId(
            Long customerId,
            Pageable pageable
    );

    boolean existsByProviderIdAndStatusIn(
            Long providerId,
            List<BookingStatus> statuses
    );

    long countByProviderIdAndBookingDate(
            Long providerId,
            LocalDate bookingDate
    );

    /*
     * PROVIDER BOOKINGS
     */

    @EntityGraph(
            attributePaths={
                    "customer",
                    "provider",
                    "provider.user"
            }
    )
    Page<Booking> findByProviderId(
            Long providerId,
            Pageable pageable
    );

    List<Booking>
    findByStatusAndInstantBooking(
            BookingStatus status,
            Boolean instantBooking
    );



    /*
     * STATUS FILTER
     */

    Page<Booking> findByStatus(
            BookingStatus status,
            Pageable pageable
    );

    /*
     * CUSTOMER + STATUS
     */

    Page<Booking> findByCustomerIdAndStatus(
            Long customerId,
            BookingStatus status,
            Pageable pageable
    );

    /*
     * PROVIDER + STATUS
     */

    Page<Booking> findByProviderIdAndStatus(
            Long providerId,
            BookingStatus status,
            Pageable pageable
    );

    /*
     * DATE FILTER
     */

    List<Booking> findByBookingDate(
            LocalDate bookingDate
    );

    /*
     * SLOT CONFLICT CHECK
     */

    @Query("""
        SELECT b
        FROM Booking b
        WHERE
            b.provider.id = :providerId
            AND
            b.bookingDate = :bookingDate
            AND
            b.status <> com.bookingplatform.enums.BookingStatus.CANCELLED
            AND
            (
                b.startTime < :endTime
                AND
                b.endTime > :startTime
            )
    """)
    List<Booking> findConflictingBookings(

            @Param("providerId")
            Long providerId,

            @Param("bookingDate")
            LocalDate bookingDate,

            @Param("startTime")
            LocalTime startTime,

            @Param("endTime")
            LocalTime endTime
    );

    /*
     * SEARCH
     */

    @Query("""
        SELECT b
        FROM Booking b
        WHERE LOWER(
            b.serviceType
        )
        LIKE LOWER(
            CONCAT(
                '%',
                :keyword,
                '%'
            )
        )
    """)
    Page<Booking> searchBookings(

            @Param("keyword")
            String keyword,

            Pageable pageable
    );

    /*
     * DASHBOARD COUNTS
     */

    long countByStatus(
            BookingStatus status
    );

    Long countByCustomerId(
            Long customerId
    );

    Long countByCustomerIdAndStatus(
            Long customerId,
            BookingStatus status
    );



    Page<Booking>
    findByCustomerIdOrderByCreatedAtDesc(

            Long customerId,

            Pageable pageable
    );




    Page<Booking>
    findByProviderIdOrderByCreatedAtDesc(
            Long providerId,
            Pageable pageable
    );

    Long countByProviderId(
            Long providerId
    );
    Long countByProviderIdAndStatus(
            Long providerId,
            BookingStatus status
    );



}