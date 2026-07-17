package com.bookingplatform.dto.auth;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotBlank
    private String phone;

    private String role;


    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;

    private String governmentIdType;
    private String governmentIdNumber;

    private MultipartFile governmentIdFile;
    private MultipartFile certificateFile;

    public String ProviderType;



}