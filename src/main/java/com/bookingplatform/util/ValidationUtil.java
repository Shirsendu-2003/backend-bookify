package com.bookingplatform.util;

import com.bookingplatform.exception.ValidationException;

import java.util.regex.Pattern;

public final class ValidationUtil {

    private ValidationUtil(){}

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "^[A-Za-z0-9+_.-]+@(.+)$"
            );

    private static final Pattern PHONE_PATTERN =
            Pattern.compile(
                    "^[0-9]{10,15}$"
            );

    public static void validateEmail(
            String email
    ) {

        if (email == null ||
                !EMAIL_PATTERN
                        .matcher(email)
                        .matches()) {

            throw new ValidationException(
                    "Invalid email format"
            );
        }
    }

    public static void validatePhone(
            String phone
    ) {

        if (phone == null ||
                !PHONE_PATTERN
                        .matcher(phone)
                        .matches()) {

            throw new ValidationException(
                    "Invalid phone number"
            );
        }
    }

    public static void validatePassword(
            String password
    ) {

        if (password == null ||
                password.length() < 6) {

            throw new ValidationException(
                    "Password must be at least 6 characters"
            );
        }
    }

}