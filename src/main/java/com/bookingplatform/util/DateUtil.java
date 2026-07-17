package com.bookingplatform.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

public final class DateUtil {

    private DateUtil() {}

    public static final String DATE_PATTERN =
            "yyyy-MM-dd";

    public static final String DATETIME_PATTERN =
            "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter
            DATE_FORMATTER =
            DateTimeFormatter.ofPattern(
                    DATE_PATTERN
            );

    public static final DateTimeFormatter
            DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern(
                    DATETIME_PATTERN
            );

    public static String formatDate(
            LocalDate date
    ) {

        return date == null
                ? null
                : date.format(
                DATE_FORMATTER
        );
    }

    public static String formatDateTime(
            LocalDateTime dateTime
    ) {

        return dateTime == null
                ? null
                : dateTime.format(
                DATETIME_FORMATTER
        );
    }

    public static LocalDate parseDate(
            String value
    ) {

        return LocalDate.parse(
                value,
                DATE_FORMATTER
        );
    }

    public static LocalDateTime parseDateTime(
            String value
    ) {

        return LocalDateTime.parse(
                value,
                DATETIME_FORMATTER
        );
    }

    public static boolean isPast(
            LocalDate date
    ) {

        return date.isBefore(
                LocalDate.now()
        );
    }

}