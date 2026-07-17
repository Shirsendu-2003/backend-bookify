package com.bookingplatform.service;

import java.nio.file.Files;
import java.nio.file.Path;

public class CsvReportGenerator {

    public static void generate(
            Path file,
            Object dto
    ) throws Exception {

        Files.writeString(
                file,
                dto.toString()
        );
    }
}