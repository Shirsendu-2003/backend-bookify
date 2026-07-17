package com.bookingplatform.service;

import com.bookingplatform.dto.notification.NotificationResponse;
import com.bookingplatform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final
    UserRepository userRepository;

    private final
    NotificationRepository
            notificationRepository;

    public List<NotificationResponse>
    getMyNotifications(){

        Authentication auth=

                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email=
                auth.getName();

        var user=

                userRepository
                        .findByEmail(email)
                        .orElseThrow();

        return notificationRepository

                .findByUserIdOrderByCreatedAtDesc(
                        user.getId()
                )

                .stream()

                .map(n->

                        NotificationResponse
                                .builder()

                                .id(
                                        n.getId()
                                )

                                .title(
                                        n.getTitle()
                                )

                                .message(
                                        n.getMessage()
                                )

                                .readStatus(
                                        n.getReadStatus()
                                )

                                .createdAt(
                                        n.getCreatedAt()
                                )

                                .build()

                )

                .toList();

    }

}