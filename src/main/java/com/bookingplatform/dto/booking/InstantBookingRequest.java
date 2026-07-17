package com.bookingplatform.dto.booking;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data

public class InstantBookingRequest {

    private Long customerId;

    private String serviceType;

    private String description;

    private String address;

    private String city;

    private String state;

    private String country;

    private String zipCode;

    private LocalDate bookingDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private Double latitude;

    private Double longitude;
}
