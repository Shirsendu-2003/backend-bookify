package com.bookingplatform.exception;

import com.bookingplatform.dto.common.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * ==========================
     * RESOURCE NOT FOUND
     * ==========================
     */

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleResourceNotFound(
            ResourceNotFoundException ex
    ) {

        ApiResponse<Object> response =
                ApiResponse.builder()
                        .success(false)
                        .message(ex.getMessage())
                        .data(null)
                        .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    /*
     * ==========================
     * UNAUTHORIZED
     * ==========================
     */

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleUnauthorized(
            UnauthorizedException ex
    ) {

        ApiResponse<Object> response =
                ApiResponse.builder()
                        .success(false)
                        .message(ex.getMessage())
                        .data(null)
                        .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    /*
     * ==========================
     * BUSINESS EXCEPTION
     * ==========================
     */

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleBusinessException(
            BusinessException ex
    ) {

        ApiResponse<Object> response =
                ApiResponse.builder()
                        .success(false)
                        .message(ex.getMessage())
                        .data(null)
                        .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /*
     * ==========================
     * CUSTOM VALIDATION
     * ==========================
     */

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleValidationException(
            ValidationException ex
    ) {

        ApiResponse<Object> response =
                ApiResponse.builder()
                        .success(false)
                        .message(ex.getMessage())
                        .data(null)
                        .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /*
     * ==========================
     * @VALID REQUEST BODY
     * ==========================
     */

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<Map<String,Object>>
    handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex
    ) {

        Map<String,String> errors =
                new HashMap<>();

        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> {

                    String field =
                            ((FieldError) error)
                                    .getField();

                    String message =
                            error.getDefaultMessage();

                    errors.put(field, message);
                });

        Map<String,Object> response =
                new HashMap<>();

        response.put("success", false);
        response.put("timestamp", LocalDateTime.now());
        response.put("errors", errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /*
     * ==========================
     * PARAM VALIDATION
     * ==========================
     */

    @ExceptionHandler(
            ConstraintViolationException.class
    )
    public ResponseEntity<ApiResponse<Object>>
    handleConstraintViolation(
            ConstraintViolationException ex
    ) {

        ApiResponse<Object> response =
                ApiResponse.builder()
                        .success(false)
                        .message(ex.getMessage())
                        .data(null)
                        .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /*
     * ==========================
     * FALLBACK EXCEPTION
     * ==========================
     */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>>
    handleGenericException(
            Exception ex
    ) {

        ApiResponse<Object> response =
                ApiResponse.builder()
                        .success(false)
                        .message("Internal Server Error")
                        .data(ex.getMessage())
                        .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

}