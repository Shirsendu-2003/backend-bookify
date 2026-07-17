package com.bookingplatform.controller;

import com.bookingplatform.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileService fileService;

    // ==========================================
    // SINGLE FILE UPLOAD
    // ==========================================

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadFile(
            @RequestParam("file")
            MultipartFile file
    ) {

        String uploadedFile =
                fileService.uploadFile(file);

        Map<String, Object> response =
                new HashMap<>();

        response.put("success", true);
        response.put("message",
                "File uploaded successfully");
        response.put("filename",
                uploadedFile);

        log.info(
                "Single file uploaded: {}",
                uploadedFile
        );

        return ResponseEntity.ok(response);
    }

    // ==========================================
    // MULTIPLE FILE UPLOAD
    // ==========================================

    @PostMapping(
            value = "/multiple-upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadMultipleFiles(
            @RequestParam("files")
            MultipartFile[] files
    ) {

        List<String> uploadedFiles =
                fileService.uploadMultipleFiles(
                        files
                );

        Map<String, Object> response =
                new HashMap<>();

        response.put("success", true);
        response.put("message",
                "Files uploaded successfully");
        response.put("files",
                uploadedFiles);

        log.info(
                "Multiple files uploaded: {}",
                uploadedFiles.size()
        );

        return ResponseEntity.ok(response);
    }

    // ==========================================
    // DOWNLOAD FILE
    // ==========================================

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable
            String filename
    ) {

        Resource resource =
                fileService.downloadFile(
                        filename
                );

        return ResponseEntity.ok()
                .contentType(
                        MediaType.APPLICATION_OCTET_STREAM
                )
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition
                                .attachment()
                                .filename(
                                        resource.getFilename()
                                )
                                .build()
                                .toString()
                )
                .body(resource);
    }

    // ==========================================
    // DELETE FILE
    // ==========================================

    @DeleteMapping("/{filename:.+}")
    public ResponseEntity<?> deleteFile(
            @PathVariable
            String filename
    ) {

        fileService.deleteFile(
                filename
        );

        Map<String, Object> response =
                new HashMap<>();

        response.put("success", true);
        response.put("message",
                "File deleted successfully");
        response.put("filename",
                filename);

        log.info(
                "File deleted: {}",
                filename
        );

        return ResponseEntity.ok(response);
    }

    // ==========================================
    // FILE EXISTS CHECK
    // ==========================================

    @GetMapping("/exists/{filename:.+}")
    public ResponseEntity<?> fileExists(
            @PathVariable
            String filename
    ) {

        boolean exists =
                fileService.fileExists(
                        filename
                );

        Map<String, Object> response =
                new HashMap<>();

        response.put("filename",
                filename);

        response.put("exists",
                exists);

        return ResponseEntity.ok(
                response
        );
    }

}