package com.bookingplatform.controller;



import com.bookingplatform.dto.common.ApiResponse;
import com.bookingplatform.dto.provider.ProviderAdminResponse;
import com.bookingplatform.dto.provider.ProviderUpdateResponse;
import com.bookingplatform.dto.provider.ReviewProviderUpdateRequest;
import com.bookingplatform.entity.Provider;
import com.bookingplatform.repository.ProviderRepository;
import com.bookingplatform.service.ProviderManagementService;
import com.bookingplatform.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/providers")
@RequiredArgsConstructor
public class AdminProviderController {

    private final ProviderService providerService;

    private  final ProviderRepository providerRepository;
    private final ProviderManagementService providerManagementService;

    @GetMapping
    public ResponseEntity<List<Provider>> getAllProviders() {
        return ResponseEntity.ok(
                providerRepository.findAll()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/provider-update-requests")
    public ApiResponse<List<ProviderUpdateResponse>>
    getPendingProviderUpdates() {

        return ApiResponse
                .<List<ProviderUpdateResponse>>builder()
                .success(true)
                .message("Pending provider update requests.")
                .data(
                        providerManagementService
                                .getPendingUpdateRequests()
                )
                .build();
    }

    @PutMapping("/provider-update-requests/{requestId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProviderUpdateResponse> approveUpdate(

            @PathVariable
            Long requestId,

            @RequestBody
            ReviewProviderUpdateRequest request

    ) {

        return ApiResponse
                .<ProviderUpdateResponse>builder()
                .success(true)
                .message("Provider profile approved.")
                .data(
                        providerManagementService
                                .approveUpdateRequest(
                                        requestId,
                                        request
                                )
                )
                .build();
    }

    @PutMapping("/provider-update-requests/{requestId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProviderUpdateResponse> rejectUpdate(

            @PathVariable
            Long requestId,

            @RequestBody
            ReviewProviderUpdateRequest request

    ) {

        return ApiResponse
                .<ProviderUpdateResponse>builder()
                .success(true)
                .message("Provider profile rejected.")
                .data(
                        providerManagementService
                                .rejectUpdateRequest(
                                        requestId,
                                        request
                                )
                )
                .build();
    }
}