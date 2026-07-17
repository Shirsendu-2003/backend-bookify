package com.bookingplatform.dto.auth;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String firstName;

    private String lastName;

    private String phone;

    private String accessToken;

    private String refreshToken;

    private String tokenType;

    private Long userId;

    private Long providerId;

    private String email;

    private String role;

    private Set<String> roles;

    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String message;

}