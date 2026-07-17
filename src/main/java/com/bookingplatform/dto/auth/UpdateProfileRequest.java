package com.bookingplatform.dto.auth;

import lombok.Data;

@Data
public class UpdateProfileRequest {

    private String firstName;
    private String lastName;
    private String phone;

    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;

}