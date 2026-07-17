package com.bookingplatform.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String upload(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return "";
        }

        try {

            Path uploadPath =
                    Paths.get(uploadDir)
                            .toAbsolutePath()
                            .normalize();

            Files.createDirectories(uploadPath);

            String originalFileName =
                    StringUtils.cleanPath(
                            file.getOriginalFilename()
                    );

            String extension = "";

            int index =
                    originalFileName.lastIndexOf(".");

            if (index > 0) {

                extension =
                        originalFileName.substring(index);

            }

            String fileName =
                    UUID.randomUUID()
                            + extension;

            Path targetLocation =
                    uploadPath.resolve(fileName);

            Files.copy(
                    file.getInputStream(),
                    targetLocation,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return fileName;

        } catch (IOException ex) {

            throw new RuntimeException(
                    "Could not store file: "
                            + file.getOriginalFilename(),
                    ex
            );

        }
    }

    public void delete(String fileName) {

        try {

            Path filePath =
                    Paths.get(uploadDir)
                            .resolve(fileName)
                            .normalize();

            Files.deleteIfExists(filePath);

        } catch (IOException ex) {

            throw new RuntimeException(
                    "Could not delete file",
                    ex
            );

        }
    }
}