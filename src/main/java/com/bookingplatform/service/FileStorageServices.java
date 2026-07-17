package com.bookingplatform.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageServices {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    /**
     * Save work proof image
     */
    public String save(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty.");
        }

        String originalFileName =
                StringUtils.cleanPath(file.getOriginalFilename());

        String extension = "";

        int index = originalFileName.lastIndexOf(".");

        if (index > 0) {
            extension = originalFileName.substring(index);
        }

        String fileName =
                UUID.randomUUID() + extension;

        Path uploadPath =
                Paths.get(uploadDir, "work-proof");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path destination =
                uploadPath.resolve(fileName);

        Files.copy(
                file.getInputStream(),
                destination,
                StandardCopyOption.REPLACE_EXISTING
        );

        return "/uploads/work-proof/" + fileName;
    }

    /**
     * Delete uploaded image
     */
    public void delete(String imageUrl) {

        try {

            if (imageUrl == null || imageUrl.isBlank()) {
                return;
            }

            String relative =
                    imageUrl.replace("/uploads/", "");

            Path file =
                    Paths.get(uploadDir)
                            .resolve(relative);

            Files.deleteIfExists(file);

        } catch (Exception ignored) {

        }
    }

    /**
     * Check if image exists
     */
    public boolean exists(String imageUrl) {

        if (imageUrl == null || imageUrl.isBlank()) {
            return false;
        }

        String relative =
                imageUrl.replace("/uploads/", "");

        Path file =
                Paths.get(uploadDir)
                        .resolve(relative);

        return Files.exists(file);
    }

}