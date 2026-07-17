package com.bookingplatform.controller;

import com.bookingplatform.dto.common.ApiResponse;
import com.bookingplatform.dto.provider.ProviderResponse;
import com.bookingplatform.dto.provider.ProviderUpdateRequestDto;
import com.bookingplatform.dto.provider.ProviderUpdateResponse;
import com.bookingplatform.dto.request.*;
import com.bookingplatform.entity.*;
import com.bookingplatform.exception.ResourceNotFoundException;
import com.bookingplatform.mapper.ProviderMapper;
import com.bookingplatform.repository.BookingRepository;
import com.bookingplatform.repository.ProviderRepository;
import com.bookingplatform.service.ProviderManagementService;

import com.bookingplatform.service.impl.ProviderManagementServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProviderController {

    private final ProviderManagementService
            providerService;
    private final ProviderRepository providerRepository;
    private final ProviderManagementServiceImpl providerManagementService;
    private  final BookingRepository bookingRepository;

    /* ========================= */
    /* GET ALL PROVIDERS */
    /* ========================= */

    @GetMapping
    public List<ProviderResponse> getAllProviders() {

        return providerService
                .getAllProviders()
                .stream()
                .map(ProviderMapper::toResponse)
                .toList();
    }

    /* ========================= */
    /* SEARCH */
    /* ========================= */

    @GetMapping("/search")
    public List<Provider>
    searchProviders(

            @RequestParam
            String keyword

    ) {

        return providerService
                .searchProviders(
                        keyword
                );
    }

    /* ========================= */
    /* GET BY ID */
    /* ========================= */

    @GetMapping("/{id}")
    public Provider
    getProvider(

            @PathVariable
            Long id

    ) {

        return providerService
                .getProviderById(id);
    }

    /* ========================= */
    /* CREATE */
    /* ========================= */

    @PostMapping(
            "/user/{userId}"
    )
    public Provider
    createProvider(

            @PathVariable
            Long userId,

            @RequestBody
            ProviderRequest request

    ) {

        return providerService
                .createProvider(
                        userId,
                        request
                );
    }

    /* ========================= */
    /* UPDATE */
    /* ========================= */

    @PutMapping("/{id}")
    public Provider
    updateProvider(

            @PathVariable
            Long id,

            @RequestBody
            ProviderRequest request

    ) {

        return providerService
                .updateProvider(
                        id,
                        request
                );
    }

    /* ========================= */
    /* DELETE */
    /* ========================= */

    @DeleteMapping("/{id}")
    public void deleteProvider(

            @PathVariable
            Long id

    ) {

        providerService
                .deleteProvider(id);
    }

    /* ========================= */
    /* PROFILE (JWT USER) */
    /* ========================= */

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(
            Authentication authentication
    ){

        try{

            System.out.println(
                    "===== PROFILE API ====="
            );

            System.out.println(
                    "AUTH = " + authentication
            );

            System.out.println(
                    "EMAIL = " +
                            authentication.getName()
            );

            Provider provider =
                    providerService.getProfile(
                            authentication.getName()
                    );

            System.out.println(
                    "PROVIDER = " +
                            provider.getId()
            );
            System.out.println(
                    "RETURNING PROFILE SUCCESS"
            );

            return ResponseEntity.ok(
                    provider
            );

        }catch(Exception ex){

            ex.printStackTrace();

            return ResponseEntity
                    .status(500)
                    .body(
                            ex.getMessage()
                    );
        }
    }

    /* ========================= */
    /* AVAILABILITY */
    /* ========================= */

    @GetMapping("/{providerId}/availability")
    public ApiResponse<List<Availability>> getAvailability(
            @PathVariable Long providerId
    ) {

        List<Availability> availability =
                providerService.getAvailability(
                        providerId
                );

        return ApiResponse.<List<Availability>>builder()
                .success(true)
                .message("Availability fetched")
                .data(availability)
                .build();
    }



    @PostMapping("/{id}/availability")
    public Availability
    addAvailability(

            @PathVariable
            Long id,

            @RequestBody
            AvailabilityRequest request

    ) {

        return providerService
                .addAvailability(
                        id,
                        request
                );
    }

    /* ========================= */
    /* SERVICES */
    /* ========================= */

    @GetMapping(
            "/{id}/services"
    )
    public List<
            com.bookingplatform.entity
                    .ProviderService>

    getServices(

            @PathVariable
            Long id

    ) {

        return providerService
                .getServices(id);
    }

    @PostMapping(
            "/{id}/services"
    )
    public
    com.bookingplatform.entity
            .ProviderService

    addService(

            @PathVariable
            Long id,

            @RequestBody
            ServiceRequest request

    ) {

        return providerService
                .addService(
                        id,
                        request
                );
    }

    @PutMapping(
            "/{providerId}/services/{serviceId}"
    )
    public
    com.bookingplatform.entity
            .ProviderService

    updateService(

            @PathVariable
            Long providerId,

            @PathVariable
            Long serviceId,

            @RequestBody
            ServiceRequest request

    ) {

        return providerService
                .updateService(

                        providerId,

                        serviceId,

                        request
                );
    }

    @DeleteMapping(
            "/{providerId}/services/{serviceId}"
    )
    public void deleteService(

            @PathVariable
            Long providerId,

            @PathVariable
            Long serviceId

    ) {

        providerService
                .deleteService(

                        providerId,

                        serviceId
                );
    }

    /* ========================= */
    /* REVIEWS */
    /* ========================= */

    @GetMapping(
            "/{id}/reviews"
    )
    public List<Review>
    getReviews(

            @PathVariable
            Long id

    ) {

        return providerService
                .getReviews(id);
    }

    @PostMapping(
            "/{id}/reviews"
    )
    public Review
    addReview(

            @PathVariable
            Long id,

            @RequestBody
            ReviewRequest request

    ) {

        return providerService
                .addReview(
                        id,
                        request
                );
    }

    /* ========================= */
    /* APPROVE PROVIDER */
    /* ========================= */

    @PutMapping("/{id}/approve")
    public ResponseEntity<Provider>
    approveProvider(

            @PathVariable
            Long id

    ){

        Provider provider =
                providerService
                        .approveProvider(id);

        return ResponseEntity.ok(
                provider
        );
    }

    /* ========================= */
    /* REJECT PROVIDER */
    /* ========================= */

    @PutMapping("/{id}/reject")
    public ResponseEntity<Provider>
    rejectProvider(

            @PathVariable
            Long id

    ){

        Provider provider =
                providerService
                        .rejectProvider(id);

        return ResponseEntity.ok(
                provider
        );
    }

    @GetMapping("/nearby")
    public List<Provider>
    getNearbyProviders(

            @RequestParam
            String location,

            @RequestParam
            String city

    ){

        return providerService
                .getNearbyProviders(

                        location,

                        city

                );

    }

    @PutMapping("/location")
    public ApiResponse<?> updateLocation(
            Authentication authentication,
            @RequestBody UpdateLocationRequest request
    ) {
        providerManagementService.updateLocation(
                authentication.getName(),
                request.getLatitude(),
                request.getLongitude(),
                request.getOnline()
        );

        return ApiResponse.success(
                "Location Updated"
        );
    }
    @GetMapping("/{bookingId}/tracking")
    public ResponseEntity<?> getTracking(
            @PathVariable Long bookingId
    ) {

        Booking booking =
                bookingRepository
                        .findById(bookingId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Booking not found"
                                )
                        );

        return ResponseEntity.ok(

                Map.of(

                        "customerLatitude",
                        booking.getCustomerLatitude(),

                        "customerLongitude",
                        booking.getCustomerLongitude(),

                        "providerLatitude",
                        booking.getProvider()
                                .getLatitude(),

                        "providerLongitude",
                        booking.getProvider()
                                .getLongitude()
                )
        );
    }

    @PostMapping("/{providerId}/update-request")
    public ApiResponse<ProviderUpdateResponse>
    submitUpdateRequest(

            @PathVariable
            Long providerId,

            @Valid
            @RequestBody
            ProviderUpdateRequestDto request

    ) {

        return ApiResponse

                .<ProviderUpdateResponse>builder()

                .success(true)

                .message(
                        "Profile update request submitted successfully."
                )

                .data(

                        providerManagementService

                                .submitProfileUpdateRequest(

                                        providerId,

                                        request

                                )

                )

                .build();

    }

    @GetMapping("/{providerId}/update-request")
    public ApiResponse<ProviderUpdateResponse>
    getPendingRequest(

            @PathVariable
            Long providerId

    ) {

        return ApiResponse

                .<ProviderUpdateResponse>builder()

                .success(true)

                .message("Pending request fetched.")

                .data(

                        providerManagementService

                                .getPendingUpdateRequest(
                                        providerId
                                )

                )

                .build();

    }

    @GetMapping("/{providerId}/update-requests")
    public ApiResponse<List<ProviderUpdateResponse>>
    getUpdateRequests(

            @PathVariable
            Long providerId

    ) {

        return ApiResponse

                .<List<ProviderUpdateResponse>>builder()

                .success(true)

                .message(
                        "Update request history fetched."
                )

                .data(

                        providerManagementService

                                .getMyUpdateRequests(
                                        providerId
                                )

                )

                .build();

    }


}