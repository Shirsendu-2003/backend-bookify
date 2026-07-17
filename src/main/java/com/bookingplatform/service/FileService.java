package com.bookingplatform.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class FileService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Value("${file.max-size:10485760}")
    private long maxFileSize;

    private Path rootPath;

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/jpg",
            "image/webp",
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    @PostConstruct
    public void init() {

        try {

            rootPath = Paths.get(uploadDir)
                    .toAbsolutePath()
                    .normalize();

            Files.createDirectories(rootPath);

            log.info("Upload directory initialized: {}",
                    rootPath);

        } catch (IOException ex) {

            log.error("Failed to initialize upload folder", ex);

            throw new RuntimeException(
                    "Could not initialize upload directory"
            );
        }
    }

    // ==========================================
    // SINGLE FILE UPLOAD
    // ==========================================

    public String uploadFile(
            MultipartFile file
    ) {

        validateFile(file);

        try {

            String originalName =
                    StringUtils.cleanPath(
                            file.getOriginalFilename()
                    );

            String extension = "";

            int dotIndex =
                    originalName.lastIndexOf('.');

            if (dotIndex >= 0) {
                extension =
                        originalName.substring(dotIndex);
            }

            String uniqueName =
                    UUID.randomUUID() + extension;

            Path target =
                    rootPath.resolve(uniqueName);

            Files.copy(
                    file.getInputStream(),
                    target,
                    StandardCopyOption.REPLACE_EXISTING
            );

            log.info(
                    "File uploaded successfully: {}",
                    uniqueName
            );

            return uniqueName;

        } catch (IOException ex) {

            log.error(
                    "Upload failed",
                    ex
            );

            throw new RuntimeException(
                    "File upload failed"
            );
        }
    }

    // ==========================================
    // MULTIPLE FILE UPLOAD
    // ==========================================

    public List<String> uploadMultipleFiles(
            MultipartFile[] files
    ) {

        return java.util.Arrays.stream(files)
                .map(this::uploadFile)
                .toList();
    }

    // ==========================================
    // DOWNLOAD FILE
    // ==========================================

    public Resource downloadFile(
            String filename
    ) {

        try {

            Path filePath =
                    rootPath.resolve(filename)
                            .normalize();

            Resource resource =
                    new UrlResource(
                            filePath.toUri()
                    );

            if (resource.exists() &&
                    resource.isReadable()) {

                log.info(
                        "File download: {}",
                        filename
                );

                return resource;
            }

            throw new RuntimeException(
                    "File not found"
            );

        } catch (MalformedURLException ex) {

            log.error(
                    "Download error",
                    ex
            );

            throw new RuntimeException(
                    "Download failed"
            );
        }
    }

    // ==========================================
    // DELETE FILE
    // ==========================================

    public void deleteFile(
            String filename
    ) {

        try {

            Path filePath =
                    rootPath.resolve(filename)
                            .normalize();

            boolean deleted =
                    Files.deleteIfExists(filePath);

            if (!deleted) {
                throw new RuntimeException(
                        "File not found"
                );
            }

            log.info(
                    "File deleted: {}",
                    filename
            );

        } catch (IOException ex) {

            log.error(
                    "Delete failed",
                    ex
            );

            throw new RuntimeException(
                    "Delete operation failed"
            );
        }
    }

    // ==========================================
    // FILE EXISTS
    // ==========================================

    public boolean fileExists(
            String filename
    ) {

        Path filePath =
                rootPath.resolve(filename)
                        .normalize();

        return Files.exists(filePath);
    }

    // ==========================================
    // FILE VALIDATION
    // ==========================================

    private void validateFile(
            MultipartFile file
    ) {

        if (file == null ||
                file.isEmpty()) {

            throw new RuntimeException(
                    "File cannot be empty"
            );
        }

        if (file.getSize() >
                maxFileSize) {

            throw new RuntimeException(
                    "File exceeds maximum size limit"
            );
        }

        String contentType =
                file.getContentType();

        if (contentType == null ||
                !ALLOWED_TYPES.contains(
                        contentType
                )) {

            throw new RuntimeException(
                    "Unsupported file type"
            );
        }
    }

}