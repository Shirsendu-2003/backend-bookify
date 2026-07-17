package com.bookingplatform.controller;

import com.bookingplatform.dto.auth.*;
import com.bookingplatform.dto.common.ApiResponse;
import com.bookingplatform.entity.User;
import com.bookingplatform.exception.BusinessException;
import com.bookingplatform.repository.ProviderRepository;
import com.bookingplatform.repository.UserRepository;
import com.bookingplatform.service.AuthService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.bookingplatform.service.UserService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final ProviderRepository
            providerRepository;
    private final UserService userService;

    /*
     * REGISTER
     */

    @PostMapping(
            value = "/register",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiResponse<AuthResponse> register(

            @ModelAttribute
            RegisterRequest request

    ) {

        return ApiResponse
                .<AuthResponse>builder()

                .success(true)

                .message(
                        "Registration successful."
                )

                .data(
                        authService.register(
                                request
                        )
                )

                .build();
    }

    /*
     * LOGIN
     */

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(

            @Valid
            @RequestBody
            LoginRequest request

    ) {

        return ApiResponse
                .<AuthResponse>builder()

                .success(true)

                .message(
                        "Login successful."
                )

                .data(
                        authService.login(
                                request
                        )
                )

                .build();
    }

    /*
     * REFRESH TOKEN
     */

    @PostMapping("/refresh-token")
    public ApiResponse<AuthResponse> refreshToken(

            @Valid
            @RequestBody
            RefreshTokenRequest request

    ) {

        return ApiResponse
                .<AuthResponse>builder()

                .success(true)

                .message(
                        "Token refreshed."
                )

                .data(
                        authService.refreshToken(
                                request
                        )
                )

                .build();
    }

    /*
     * CURRENT USER
     */

    @GetMapping("/me")
    public ApiResponse<AuthResponse> me(

            Authentication authentication

    ) {

        String email =
                authentication.getName();

        User user =

                userRepository
                        .findByEmail(email)

                        .orElseThrow(() ->

                                new BusinessException(
                                        "User not found."
                                )
                        );

        String role =

                user.getRoles()

                        .stream()

                        .map(r ->

                                r.getName()
                                        .name()

                        )

                        .findFirst()

                        .orElse(
                                "ROLE_CUSTOMER"
                        );

        Long providerId = null;

        if(
                role.equals(
                        "ROLE_PROVIDER"
                )
        ){

            providerId =

                    providerRepository

                            .findByUser(user)

                            .map(
                                    p -> p.getId()
                            )

                            .orElse(null);
        }

        AuthResponse response =

                AuthResponse.builder()

                        .userId(
                                user.getId()
                        )

                        .providerId(
                                providerId
                        )

                        .firstName(
                                user.getFirstName()
                        )

                        .lastName(
                                user.getLastName()
                        )

                        .phone(
                                user.getPhone()
                        )

                        .email(
                                user.getEmail()
                        )

                        .address(
                                user.getAddress()
                        )

                        .city(
                                user.getCity()
                        )

                        .state(
                                user.getState()
                        )

                        .country(
                                user.getCountry()
                        )

                        .zipCode(
                                user.getZipCode()
                        )

                        .role(
                                role
                        )

                        .roles(

                                user.getRoles()

                                        .stream()

                                        .map(r ->
                                                r.getName().name()
                                        )

                                        .collect(
                                                java.util.stream.Collectors.toSet()
                                        )

                        )

                        .build();

        return ApiResponse
                .<AuthResponse>builder()

                .success(true)

                .message(
                        "Current user fetched."
                )

                .data(
                        response
                )

                .build();
    }

    @PutMapping("/profile")
    public ApiResponse<AuthResponse> updateProfile(
            Authentication authentication,
            @RequestBody UpdateProfileRequest request
    ) {

        AuthResponse response = userService.updateProfile(
                authentication.getName(),
                request
        );

        return ApiResponse
                .<AuthResponse>builder()
                .success(true)
                .message("Profile updated successfully.")
                .data(response)
                .build();
    }
    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPassword(

            @RequestBody
            ForgotPasswordRequest request

    ){

        try{

            authService
                    .forgotPassword(request);

            return ApiResponse.success(

                    "Reset email sent successfully."

            );

        }catch(Exception e){

            return ApiResponse.error(

                    e.getMessage()

            );

        }

    }

    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(

            @RequestBody
            ResetPasswordRequest request

    ){

        try{

            authService
                    .resetPassword(request);

            return ApiResponse.success(

                    "Password updated successfully."

            );

        }catch(Exception e){

            return ApiResponse.error(

                    e.getMessage()

            );

        }

    }

}