package com.bookingplatform.dto.booking;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpVerificationRequest {

    @NotBlank(message = "OTP is required")
    private String otp;

}