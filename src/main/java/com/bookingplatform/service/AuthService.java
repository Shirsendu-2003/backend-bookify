package com.bookingplatform.service;

import com.bookingplatform.dto.auth.*;
import com.bookingplatform.entity.Provider;
import com.bookingplatform.entity.ProviderService;
import com.bookingplatform.entity.Role;
import com.bookingplatform.entity.User;
import com.bookingplatform.enums.ProviderType;
import com.bookingplatform.enums.RoleType;
import com.bookingplatform.enums.UserStatus;
import com.bookingplatform.enums.VerificationStatus;
import com.bookingplatform.exception.BusinessException;
import com.bookingplatform.repository.ProviderRepository;
import com.bookingplatform.repository.RoleRepository;
import com.bookingplatform.repository.UserRepository;
import com.bookingplatform.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bookingplatform.entity.Notification;
import com.bookingplatform.repository.NotificationRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ProviderRepository providerRepository;
    private final
    MailService mailService;
    private final
    NotificationRepository
            notificationRepository;
    private final FileStorageService fileStorageService;


    /*
     * REGISTER
     */

    public AuthResponse register(
            RegisterRequest request
    ) {

        if (userRepository.existsByEmail(
                request.getEmail()
        )) {

            throw new BusinessException(
                    "Email already exists."
            );
        }

        if (userRepository.existsByPhone(
                request.getPhone()
        )) {

            throw new BusinessException(
                    "Phone already exists."
            );
        }

        RoleType roleType;

        if (request.getRole() == null ||
                request.getRole().isBlank()) {

            roleType = RoleType.ROLE_CUSTOMER;

        } else {

            try {

                roleType =
                        RoleType.valueOf(
                                request.getRole()
                        );

            } catch (Exception ex) {

                throw new BusinessException(
                        "Invalid role."
                );
            }
        }

        if (roleType != RoleType.ROLE_CUSTOMER &&
                roleType != RoleType.ROLE_PROVIDER) {

            throw new BusinessException(
                    "Only CUSTOMER or PROVIDER allowed."
            );
        }

        Role role = roleRepository

                .findByName(roleType)

                .orElseThrow(() ->

                        new BusinessException(
                                "Role not found."
                        )
                );

        User user = User.builder()

                .firstName(
                        request.getFirstName()
                )

                .lastName(
                        request.getLastName()
                )

                .email(
                        request.getEmail()
                )

                .password(

                        passwordEncoder.encode(
                                request.getPassword()
                        )

                )

                .phone(
                        request.getPhone()
                )

                .status(
                        UserStatus.ACTIVE
                )

                .roles(
                        Set.of(role)
                )
                .address(
                        request.getAddress()
                )

                .city(
                        request.getCity()
                )

                .state(
                        request.getState()
                )

                .country(
                        request.getCountry()
                )

                .zipCode(
                        request.getZipCode()
                )

                .build();

        User savedUser =
                userRepository.save(user);

        notificationRepository.save(

                Notification.builder()

                        .title(
                                "Registration Successful"
                        )

                        .message(

                                roleType ==
                                        RoleType.ROLE_PROVIDER

                                        ?

                                        "Provider account created and waiting for admin verification."

                                        :

                                        "Customer account created successfully."

                        )

                        .user(
                                savedUser
                        )

                        .build()

        );

        if (roleType == RoleType.ROLE_PROVIDER) {

            String governmentIdFilePath = "";

            String certificateFilePath = "";

            if (
                    request.getGovernmentIdFile() != null &&
                            !request.getGovernmentIdFile().isEmpty()
            ) {

                governmentIdFilePath =
                        fileStorageService.upload(
                                request.getGovernmentIdFile()
                        );
            }

            if (
                    request.getCertificateFile() != null &&
                            !request.getCertificateFile().isEmpty()
            ) {

                certificateFilePath =
                        fileStorageService.upload(
                                request.getCertificateFile()
                        );
            }

            if (request.getProviderType() == null || request.getProviderType().isBlank()) {
                throw new BusinessException("Provider type is required.");
            }

            try {
                ProviderType.valueOf(request.getProviderType());
            } catch (IllegalArgumentException ex) {
                throw new BusinessException("Invalid provider type.");
            }

            Provider provider =

                    Provider.builder()

                            .user(savedUser)

                            .name(
                                    savedUser.getFirstName()
                                            + " "
                                            + savedUser.getLastName()
                            )

                            .providerType(request.getProviderType())

                            .serviceType(request.getProviderType())

                            .experience("0 Years")

                            .location("Not Set")

                            .bio("Provider Profile")

                            .skills("")

                            .avatar("")

                            .hourlyRate(BigDecimal.ZERO)

                            .online(true)

                            .available(true)

                            .averageRating(0.0)

                            .totalReviews(0)

                            .city("")
                            .state("")
                            .country("")
                            .zipCode("")

                            .profileImageUrl("")
                            .governmentIdType(
                                    request.getGovernmentIdType()
                            )

                            .governmentIdNumber(
                                    request.getGovernmentIdNumber()
                            )

                            .governmentIdUrl(
                                    governmentIdFilePath.isBlank()
                                            ? ""
                                            : "/uploads/" + governmentIdFilePath
                            )

                            .certificateUrl(
                                    certificateFilePath.isBlank()
                                            ? ""
                                            : "/uploads/" + certificateFilePath
                            )

                            .verificationStatus(
                                    VerificationStatus.PENDING
                            )

                            .build();

            String serviceName = request.getProviderType()
                    .replace("_", " ")
                    .toLowerCase();

            serviceName =
                    Character.toUpperCase(serviceName.charAt(0))
                            + serviceName.substring(1);

            ProviderService service1 = ProviderService.builder()
                    .title(serviceName)
                    .description("Standard " + serviceName + " service")
                    .price(0.0)
                    .provider(provider)
                    .build();

            ProviderService service2 = ProviderService.builder()
                    .title("Premium " + serviceName)
                    .description("Premium " + serviceName + " service")
                    .price(100.0)
                    .provider(provider)
                    .build();



            provider.getServices()
                    .add(service1);

            provider.getServices()
                    .add(service2);


            providerRepository.save(
                    provider
            );

            notificationRepository.save(

                    Notification.builder()

                            .title(
                                    "Provider Profile Created"
                            )

                            .message(
                                    "Your provider profile is ready."
                            )

                            .user(
                                    savedUser
                            )

                            .build()

            );
        }

        if (roleType == RoleType.ROLE_PROVIDER) {

            return AuthResponse.builder()
                    .message(
                            "Provider registration submitted successfully. Waiting for admin approval."
                    )
                    .build();
        }

        UserDetails userDetails =
                buildUserDetails(savedUser);

        return buildAuthResponse(
                savedUser,
                userDetails
        );
    }

    /*
     * LOGIN
     */

    public AuthResponse login(
            LoginRequest request
    ) {

        User user = userRepository

                .findByEmail(
                        request.getEmail()
                )

                .orElseThrow(() ->

                        new BusinessException(
                                "Invalid email or password."
                        )
                );

        /*
         * USER STATUS CHECK
         */

        if (user.getStatus() != UserStatus.ACTIVE) {

            switch (user.getStatus()) {

                case INACTIVE ->
                        throw new BusinessException(
                                "Your account is inactive. Please contact support."
                        );

                case BLOCKED ->
                        throw new BusinessException(
                                "Your account has been blocked by admin."
                        );

                case SUSPENDED ->
                        throw new BusinessException(
                                "Your account has been suspended."
                        );

                default ->
                        throw new BusinessException(
                                "Your account is not active."
                        );
            }
        }

        Authentication authentication =

                authenticationManager
                        .authenticate(

                                new UsernamePasswordAuthenticationToken(

                                        request.getEmail(),

                                        request.getPassword()

                                )

                        );

        UserDetails userDetails =

                (UserDetails)
                        authentication.getPrincipal();



        if (
                user.getRoles()
                        .stream()
                        .anyMatch(
                                role ->
                                        role.getName()
                                                == RoleType.ROLE_PROVIDER
                        )
        ) {

            Provider provider =
                    providerRepository
                            .findByUser(user)
                            .orElseThrow(
                                    () ->
                                            new BusinessException(
                                                    "Provider profile not found."
                                            )
                            );

            if (
                    provider.getVerificationStatus()
                            == VerificationStatus.PENDING
            ) {

                throw new BusinessException(
                        "Your provider account is pending admin verification."
                );
            }

            if (
                    provider.getVerificationStatus()
                            == VerificationStatus.REJECTED
            ) {

                throw new BusinessException(
                        "Your provider account has been rejected by admin."
                );
            }
        }

        notificationRepository.save(

                Notification.builder()

                        .title(
                                "Login Successful"
                        )

                        .message(
                                "Welcome back to Bookify."
                        )

                        .user(
                                user
                        )

                        .build()

        );

        return buildAuthResponse(
                user,
                userDetails
        );
    }

    /*
     * REFRESH TOKEN
     */

    public AuthResponse refreshToken(
            RefreshTokenRequest request
    ) {

        String username =

                jwtService.extractUsername(
                        request.getRefreshToken()
                );

        User user = userRepository

                .findByEmail(username)

                .orElseThrow(() ->

                        new BusinessException(
                                "User not found."
                        )
                );

        UserDetails userDetails =
                buildUserDetails(user);

        if (!jwtService.validateToken(

                request.getRefreshToken(),

                userDetails

        )) {

            throw new BusinessException(
                    "Invalid refresh token."
            );
        }

        return buildAuthResponse(
                user,
                userDetails
        );
    }

    /*
     * HELPERS
     */

    public AuthResponse buildAuthResponse(

            User user,
            UserDetails userDetails

    ) {

        String accessToken =
                jwtService.generateAccessToken(
                        userDetails
                );

        String refreshToken =
                jwtService.generateRefreshToken(
                        userDetails
                );

        Set<String> roles =

                user.getRoles()
                        .stream()
                        .map(r ->
                                r.getName().name()
                        )
                        .collect(
                                Collectors.toSet()
                        );

        String role =
                roles.stream()
                        .findFirst()
                        .orElse(
                                "ROLE_CUSTOMER"
                        );

        Long providerId = null;

        if (
                roles.contains(
                        "ROLE_PROVIDER"
                )
        ) {

            providerId =

                    providerRepository

                            .findByUser(user)

                            .map(
                                    Provider::getId
                            )

                            .orElse(null);
        }

        return AuthResponse.builder()

                .accessToken(
                        accessToken
                )

                .refreshToken(
                        refreshToken
                )

                .tokenType(
                        "Bearer"
                )

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
                        roles
                )

                .build();
    }

    private UserDetails buildUserDetails(
            User user
    ) {

        return new org.springframework.security
                .core.userdetails.User(

                user.getEmail(),

                user.getPassword(),

                user.getRoles()

                        .stream()

                        .map(role ->

                                new SimpleGrantedAuthority(

                                        role.getName()
                                                .name()

                                )

                        )

                        .collect(
                                Collectors.toList()
                        )

        );
    }

    public void forgotPassword(

            ForgotPasswordRequest request

    ){

        User user =
                userRepository

                        .findByEmail(
                                request.getEmail()
                        )

                        .orElseThrow(
                                ()->new BusinessException(
                                        "User not found"
                                )
                        );

        String token =

                UUID.randomUUID()
                        .toString();

        user.setResetToken(
                token
        );

        user.setResetTokenExpiry(

                LocalDateTime.now()
                        .plusMinutes(30)

        );

        userRepository.save(
                user
        );

        mailService
                .sendResetEmail(

                        user.getEmail(),
                        token

                );
    }

    /* RESET PASSWORD */

    public void resetPassword(

            ResetPasswordRequest request

    ){

        User user =
                userRepository

                        .findByResetToken(
                                request.getToken()
                        )

                        .orElseThrow(
                                ()->new BusinessException(
                                        "Invalid token"
                                )
                        );

        if(

                user
                        .getResetTokenExpiry()

                        .isBefore(
                                LocalDateTime.now()
                        )

        ){

            throw new
                    BusinessException(
                    "Token expired"
            );
        }

        user.setPassword(

                passwordEncoder
                        .encode(
                                request
                                        .getPassword()
                        )

        );

        user.setResetToken(
                null
        );

        user.setResetTokenExpiry(
                null
        );

        userRepository.save(
                user
        );
    }

}