package com.bookingplatform.scheduler;

import com.bookingplatform.dto.notification.NotificationRequest;
import com.bookingplatform.entity.Booking;
import com.bookingplatform.enums.BookingStatus;
import com.bookingplatform.repository.BookingRepository;
import com.bookingplatform.service.EmailService;
import com.bookingplatform.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReminderScheduler {

    private final BookingRepository bookingRepository;
    private final NotificationService notificationService;


    @Scheduled(cron = "0 0 8 * * *")
    public void sendBookingReminders() {

        log.info("Running booking reminder job...");

        LocalDate tomorrow =
                LocalDate.now().plusDays(1);

        List<Booking> bookings =
                bookingRepository
                        .findByBookingDate(tomorrow);

        bookings.stream()

                .filter(b ->
                        b.getStatus()
                                == BookingStatus.CONFIRMED
                )

                .forEach(booking -> {

                    String message =
                            "Reminder: You have a booking tomorrow for "
                                    + booking.getServiceType();

                    NotificationRequest request =
                            NotificationRequest.builder()

                                    .userId(
                                            booking.getCustomer()
                                                    .getId()
                                    )

                                    .bookingId(
                                            booking.getId()
                                    )

                                    .title(
                                            "Booking Reminder"
                                    )

                                    .message(
                                            message
                                    )

                                    .build();





                    log.info(
                            "Reminder sent for booking {}",
                            booking.getId()
                    );

                });
    }
}