package com.bookingplatform.controller;

import com.bookingplatform.dto.contact.ContactRequest;
import com.bookingplatform.service.ContactService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService
            contactService;

    @PostMapping
    public ResponseEntity<?> sendContact(

            @RequestBody
            ContactRequest request

    ){

        return ResponseEntity.ok(

                contactService
                        .sendMessage(request)

        );
    }
}