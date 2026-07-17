package com.bookingplatform.dto.user;

import com.bookingplatform.enums.UserStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String name;;

    private String email;

    private String phone;

    private String profileImage;

    private Boolean emailVerified;

    private Boolean phoneVerified;

    private UserStatus status;

    private Set<String> roles;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}