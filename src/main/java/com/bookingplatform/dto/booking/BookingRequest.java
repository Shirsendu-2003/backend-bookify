package com.bookingplatform.dto.booking;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    @NotNull
    private Long providerId;

    @NotBlank
    private String serviceType;

    @NotBlank
    @Size(max = 1000)
    private String description;

    /*
     * ADDRESS
     */

    @NotBlank
    private String address;

    private String city;

    private String state;

    private String country;

    private String zipCode;

    /*
     * DATE & TIME
     */

    @NotNull
    private LocalDate bookingDate;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    /*
     * PRICE
     */


    @DecimalMin("0.0")
    private BigDecimal amount;

}