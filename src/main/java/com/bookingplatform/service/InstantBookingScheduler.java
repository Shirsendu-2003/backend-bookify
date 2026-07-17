package com.bookingplatform.service;

import com.bookingplatform.entity.Booking;
import com.bookingplatform.entity.Notification;
import com.bookingplatform.entity.Provider;
import com.bookingplatform.enums.BookingStatus;
import com.bookingplatform.repository.BookingRepository;
import com.bookingplatform.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstantBookingScheduler {

    private final BookingRepository bookingRepository;
    private final NotificationRepository notificationRepository;
    private final BookingService bookingService;
    private final EmailService emailService;

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void processInstantBookings() {

        List<Booking> bookings =
                bookingRepository.findByStatusAndInstantBooking(
                        BookingStatus.PENDING,
                        true
                );

        for (Booking booking : bookings) {

            if (booking.getOfferSentAt() == null) {
                continue;
            }

            if (LocalDateTime.now().isBefore(
                    booking.getOfferSentAt().plusMinutes(1)
            )) {
                continue;
            }

            List<Provider> providers =
                    bookingService.getEligibleProviders(
                            booking.getCustomerLatitude(),
                            booking.getCustomerLongitude(),
                            booking.getServiceType()
                    );

            providers.removeIf(
                    p -> p.getId().equals(
                            booking.getProvider().getId()
                    )
            );

            if (providers.isEmpty()) {

                booking.setStatus(BookingStatus.REJECTED);
                booking.setInstantBooking(false);

                bookingRepository.save(booking);

                notificationRepository.save(
                        Notification.builder()
                                .user(booking.getCustomer())
                                .title("Booking Failed")
                                .message("No provider accepted your booking.")
                                .build()
                );

                return;
            }

            Provider nextProvider = providers.get(0);

            booking.setProvider(nextProvider);

            booking.setOfferSentAt(LocalDateTime.now());

            bookingRepository.save(booking);

            booking.setOfferSentAt(
                    LocalDateTime.now()
            );

            bookingRepository.save(booking);

            notificationRepository.save(
                    Notification.builder()
                            .user(nextProvider.getUser())
                            .title("Instant Booking")
                            .message(
                                    "Accept within 1 minute"
                            )
                            .build()
            );

            emailService.sendEmail(
                    nextProvider.getUser().getEmail(),
                    "Instant Booking Request",
                    """
                    Hello %s,

                    An instant booking request has been reassigned to you.

                    Booking ID: %d

                    Please accept within 1 minute.

                    Thank You
                    """
                            .formatted(
                                    nextProvider.getUser()
                                            .getFirstName(),
                                    booking.getId()
                            )
            );

            System.out.println(
                    "REASSIGNED TO PROVIDER="
                            + nextProvider.getId()
            );
        }
    }
}