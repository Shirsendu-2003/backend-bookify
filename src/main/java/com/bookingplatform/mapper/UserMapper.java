package com.bookingplatform.mapper;

import com.bookingplatform.dto.user.*;
import com.bookingplatform.entity.Role;
import com.bookingplatform.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    private UserMapper() {}

    /*
     * ==========================
     * DTO → ENTITY
     * ==========================
     */

    public static User toEntity(
            UserRequest request
    ) {

        if (request == null) {
            return null;
        }

        User user = new User();

        user.setFirstName(
                request.getFirstName()
        );

        user.setLastName(
                request.getLastName()
        );

        user.setEmail(
                request.getEmail()
        );

        user.setPassword(
                request.getPassword()
        );

        user.setPhone(
                request.getPhone()
        );

        user.setProfileImage(
                request.getProfileImage()
        );

        return user;
    }

    /*
     * ==========================
     * ENTITY → DTO
     * ==========================
     */

    public static UserResponse toResponse(
            User user
    ) {

        if (user == null) {
            return null;
        }

        Set<String> roles =
                user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .map(Enum::name)
                        .collect(Collectors.toSet());

        return UserResponse.builder()

                .id(user.getId())

                .name(
                        user.getFirstName()
                                + " "
                                + user.getLastName()
                )

                .email(
                        user.getEmail()
                )

                .phone(
                        user.getPhone()
                )

                .profileImage(
                        user.getProfileImage()
                )

                .emailVerified(
                        user.getEmailVerified()
                )

                .phoneVerified(
                        user.getPhoneVerified()
                )

                .status(
                        user.getStatus()
                )

                .roles(roles)

                .createdAt(
                        user.getCreatedAt()
                )

                .updatedAt(
                        user.getUpdatedAt()
                )

                .build();
    }

}