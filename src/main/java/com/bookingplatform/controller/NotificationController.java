package com.bookingplatform.controller;

import com.bookingplatform.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        "/api/notifications"
)
@RequiredArgsConstructor
public class NotificationController {

    private final
    NotificationService
            notificationService;

    @GetMapping

    public Object
    getNotifications(){

        return notificationService
                .getMyNotifications();

    }

}