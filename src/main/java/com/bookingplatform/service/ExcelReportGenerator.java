package com.bookingplatform.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ExcelReportGenerator {

    public static void generate(
            Path file,
            Object dto
    ) throws Exception {

        Workbook workbook =
                new XSSFWorkbook();

        Sheet sheet =
                workbook.createSheet(
                        "Report"
                );

        Row row =
                sheet.createRow(0);

        row.createCell(0)
                .setCellValue(
                        dto.toString()
                );

        try (
                OutputStream os =
                        Files.newOutputStream(file)
        ) {

            workbook.write(os);
        }

        workbook.close();
    }
}
