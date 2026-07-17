package com.bookingplatform.service;

import com.bookingplatform.dto.auth.AuthResponse;
import com.bookingplatform.dto.auth.UpdateProfileRequest;
import com.bookingplatform.dto.common.PaginationResponse;
import com.bookingplatform.dto.user.UserRequest;
import com.bookingplatform.dto.user.UserResponse;
import com.bookingplatform.entity.User;
import com.bookingplatform.exception.BusinessException;
import com.bookingplatform.exception.ResourceNotFoundException;
import com.bookingplatform.mapper.UserMapper;
import com.bookingplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /*
     * ==========================
     * GET USER BY ID
     * ==========================
     */

    public UserResponse getUserById(
            Long id
    ) {

        User user = userRepository
                .findById(id)
                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "User not found: " + id
                        )
                );

        return UserMapper.toResponse(
                user
        );
    }

    /*
     * ==========================
     * GET ALL USERS
     * ==========================
     */

    public PaginationResponse<UserResponse>
    getAllUsers(

            int page,

            int size

    ) {

        Pageable pageable =

                PageRequest.of(
                        page,
                        size,
                        Sort.by("id")
                                .descending()
                );

        Page<User> userPage =
                userRepository.findAll(
                        pageable
                );

        return PaginationResponse
                .<UserResponse>builder()

                .content(

                        userPage.getContent()

                                .stream()

                                .map(
                                        UserMapper::toResponse
                                )

                                .toList()

                )

                .page(
                        userPage.getNumber()
                )

                .size(
                        userPage.getSize()
                )

                .totalElements(
                        userPage.getTotalElements()
                )

                .totalPages(
                        userPage.getTotalPages()
                )

                .first(
                        userPage.isFirst()
                )

                .last(
                        userPage.isLast()
                )

                .build();
    }

    /*
     * ==========================
     * SEARCH USERS
     * ==========================
     */

    public PaginationResponse<UserResponse>
    searchUsers(

            String keyword,

            int page,

            int size

    ) {

        Pageable pageable =

                PageRequest.of(
                        page,
                        size
                );

        Page<User> userPage =

                userRepository.searchUsers(
                        keyword,
                        pageable
                );

        return PaginationResponse
                .<UserResponse>builder()

                .content(

                        userPage.getContent()

                                .stream()

                                .map(
                                        UserMapper::toResponse
                                )

                                .toList()

                )

                .page(
                        userPage.getNumber()
                )

                .size(
                        userPage.getSize()
                )

                .totalElements(
                        userPage.getTotalElements()
                )

                .totalPages(
                        userPage.getTotalPages()
                )

                .first(
                        userPage.isFirst()
                )

                .last(
                        userPage.isLast()
                )

                .build();
    }

    /*
     * ==========================
     * UPDATE USER
     * ==========================
     */

    public UserResponse updateUser(

            Long id,

            UserRequest request

    ) {

        User user = userRepository
                .findById(id)
                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "User not found."
                        )
                );

        if (!user.getEmail()
                .equals(request.getEmail())

                &&

                userRepository.existsByEmail(
                        request.getEmail()
                )) {

            throw new BusinessException(
                    "Email already exists."
            );
        }

        if (!user.getPhone()
                .equals(request.getPhone())

                &&

                userRepository.existsByPhone(
                        request.getPhone()
                )) {

            throw new BusinessException(
                    "Phone already exists."
            );
        }

        user.setFirstName(
                request.getFirstName()
        );

        user.setLastName(
                request.getLastName()
        );

        user.setEmail(
                request.getEmail()
        );

        user.setPhone(
                request.getPhone()
        );

        user.setProfileImage(
                request.getProfileImage()
        );

        if (request.getPassword() != null
                &&
                !request.getPassword().isBlank()) {

            user.setPassword(

                    passwordEncoder.encode(
                            request.getPassword()
                    )

            );
        }

        userRepository.save(user);

        return UserMapper.toResponse(
                user
        );
    }

    /*
     * ==========================
     * DELETE USER
     * ==========================
     */

    public void deleteUser(
            Long id
    ) {

        User user = userRepository
                .findById(id)
                .orElseThrow(() ->

                        new ResourceNotFoundException(
                                "User not found."
                        )
                );

        userRepository.delete(
                user
        );
    }

    public AuthResponse updateProfile(
            String email,
            UpdateProfileRequest request
    ) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new BusinessException(
                                "User not found"
                        )
                );

        user.setFirstName(
                request.getFirstName()
        );

        user.setLastName(
                request.getLastName()
        );

        user.setPhone(
                request.getPhone()
        );

        user.setAddress(
                request.getAddress()
        );

        user.setCity(
                request.getCity()
        );

        user.setState(
                request.getState()
        );

        user.setCountry(
                request.getCountry()
        );

        user.setZipCode(
                request.getZipCode()
        );

        userRepository.save(user);

        return AuthResponse.builder()

                .userId(user.getId())

                .firstName(user.getFirstName())

                .lastName(user.getLastName())

                .email(user.getEmail())

                .phone(user.getPhone())

                .address(user.getAddress())

                .city(user.getCity())

                .state(user.getState())

                .country(user.getCountry())

                .zipCode(user.getZipCode())

                .build();
    }

}