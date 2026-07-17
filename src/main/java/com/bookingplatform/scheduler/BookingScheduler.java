package com.bookingplatform.scheduler;

import com.bookingplatform.entity.Booking;
import com.bookingplatform.enums.BookingStatus;
import com.bookingplatform.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingScheduler {

    private final BookingRepository bookingRepository;

    /*
     * Runs every 30 minutes
     */

    @Scheduled(cron = "0 */30 * * * *")
    public void autoCompleteBookings() {

        log.info("Running booking auto-completion job...");

        List<Booking> bookings =
                bookingRepository.findAll();

        LocalDate today =
                LocalDate.now();

        LocalTime now =
                LocalTime.now();

        bookings.stream()

                .filter(b ->
                        b.getStatus()
                                == BookingStatus.IN_PROGRESS
                )

                .filter(b ->
                        b.getBookingDate()
                                .isEqual(today)
                )

                .filter(b ->
                        b.getEndTime()
                                .isBefore(now)
                )

                .forEach(b -> {

                    b.setStatus(
                            BookingStatus.COMPLETED
                    );

                    bookingRepository.save(b);

                    log.info(
                            "Booking {} marked COMPLETED",
                            b.getId()
                    );

                });

    }

}