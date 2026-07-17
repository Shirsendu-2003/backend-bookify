package com.bookingplatform.service;

import com.bookingplatform.dto.contact.ContactRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ContactService {

    public Object sendMessage(
            ContactRequest request
    ){

        return Map.of(

                "success",
                true,

                "message",
                "Message sent successfully.",

                "data",
                request

        );
    }
}