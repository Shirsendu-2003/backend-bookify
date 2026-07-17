package com.bookingplatform.dto.auth;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String token;

    private String password;
}