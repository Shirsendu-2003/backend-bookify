package com.bookingplatform.util;

import com.bookingplatform.exception.ValidationException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public final class FileUtil {

    private FileUtil(){}

    private static final List<String>
            ALLOWED_TYPES =
            List.of(

                    "image/png",

                    "image/jpeg",

                    "image/jpg",

                    "application/pdf"

            );

    public static void validateFile(
            MultipartFile file
    ) {

        if (file == null ||
                file.isEmpty()) {

            throw new ValidationException(
                    "File is empty"
            );
        }

        if (file.getSize()
                > Constants.MAX_FILE_SIZE) {

            throw new ValidationException(
                    "File size exceeded"
            );
        }

        if (!ALLOWED_TYPES.contains(
                file.getContentType()
        )) {

            throw new ValidationException(
                    "Invalid file type"
            );
        }

    }

    public static String generateFileName(
            MultipartFile file
    ) {

        String original =
                file.getOriginalFilename();

        String extension = "";

        if (original != null &&
                original.contains(".")) {

            extension =
                    original.substring(
                            original
                                    .lastIndexOf(".")
                    );
        }

        return UUID.randomUUID()
                + extension;
    }

}