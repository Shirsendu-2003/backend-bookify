package com.bookingplatform.repository;

import com.bookingplatform.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookingplatform.entity.ProviderAvailability;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityRepository
        extends JpaRepository<Availability, Long> {

    /*
     * ==========================
     * PROVIDER AVAILABILITY
     * ==========================
     */



    List<Availability> findByProviderId(
            Long providerId
    );

    List<Availability> findByProviderIdAndDayOfWeek(
            Long providerId,
            DayOfWeek dayOfWeek
    );

    Optional<Availability>
    findByProvider_IdAndDayOfWeek(
            Long providerId,
            DayOfWeek dayOfWeek
    );

    /*
     * ==========================
     * SLOT VALIDATION
     * ==========================
     */

    List<Availability>
    findByProviderIdAndDayOfWeekAndAvailableTrue(
            Long providerId,
            DayOfWeek dayOfWeek
    );

    /*
     * ==========================
     * TIME FILTER
     * ==========================
     */

    List<Availability>
    findByProviderIdAndDayOfWeekAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            Long providerId,
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime
    );

}