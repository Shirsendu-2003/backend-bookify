package com.bookingplatform.controller;

import com.bookingplatform.dto.common.ApiResponse;
import com.bookingplatform.dto.complaint.ComplaintRequest;
import com.bookingplatform.dto.complaint.ComplaintResponse;

import com.bookingplatform.service.ComplaintService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService
            complaintService;

    @PostMapping
    public ResponseEntity<

            ApiResponse
                    <ComplaintResponse>

            >

    create(

            @RequestBody
            ComplaintRequest request,

            Authentication auth

    ){

        ComplaintResponse response =

                complaintService
                        .createComplaint(

                                request,

                                auth.getName()

                        );

        return ResponseEntity.ok(

                ApiResponse
                        .<ComplaintResponse>
                                builder()

                        .success(true)

                        .message(
                                "Complaint submitted successfully."
                        )

                        .data(
                                response
                        )

                        .build()

        );
    }
}