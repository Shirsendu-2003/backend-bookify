package com.bookingplatform.dto.contact;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequest {

    private String name;

    private String email;

    private String subject;

    private String message;
}