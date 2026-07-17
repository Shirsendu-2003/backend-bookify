package com.bookingplatform.websocket;

import com.bookingplatform.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationSocket {

    private final SimpMessagingTemplate
            messagingTemplate;

    /*
     * ==========================
     * USER NOTIFICATION
     * ==========================
     */

    public void sendToUser(
            Long userId,
            Notification notification
    ) {

        messagingTemplate
                .convertAndSendToUser(

                        userId.toString(),

                        "/queue/notifications",

                        notification
                );
    }

    /*
     * ==========================
     * BROADCAST
     * ==========================
     */

    public void broadcast(
            Notification notification
    ) {

        messagingTemplate
                .convertAndSend(

                        "/topic/notifications",

                        notification
                );
    }

}