package com.bookingplatform.scheduler;

import com.bookingplatform.entity.Notification;
import com.bookingplatform.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CleanupScheduler {

    private final NotificationRepository
            notificationRepository;

    /*
     * Daily at 2 AM
     */

    @Scheduled(cron = "0 0 2 * * *")
    public void cleanupOldNotifications() {

        log.info(
                "Running cleanup scheduler..."
        );

        LocalDateTime cutoffDate =
                LocalDateTime.now()
                        .minusDays(90);

        List<Notification> notifications =
                notificationRepository.findAll();

        notifications.stream()

                .filter(n ->
                        n.getCreatedAt()
                                .isBefore(cutoffDate)
                )

                .forEach(notificationRepository::delete);

        log.info(
                "Old notifications cleaned."
        );

    }

}