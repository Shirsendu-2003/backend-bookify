package com.bookingplatform.service;

import com.bookingplatform.dto.provider.ProviderUpdateRequestDto;
import com.bookingplatform.dto.provider.ProviderUpdateResponse;
import com.bookingplatform.dto.provider.ReviewProviderUpdateRequest;
import com.bookingplatform.dto.request.*;
import com.bookingplatform.entity.*;

import java.util.List;

public interface ProviderManagementService {

    List<Provider> getAllProviders();

    List<Provider> searchProviders(String keyword);

    Provider getProviderById(Long id);

    Provider createProvider(
            Long userId,
            ProviderRequest request
    );

    Provider updateProvider(
            Long id,
            ProviderRequest request
    );

    void deleteProvider(Long id);

    Provider getProfile(String email);

    List<Availability>
    getAvailability(Long providerId);

    Availability addAvailability(
            Long providerId,
            AvailabilityRequest request
    );

    List<com.bookingplatform.entity.ProviderService>
    getServices(Long providerId);

    com.bookingplatform.entity.ProviderService
    addService(
            Long providerId,
            ServiceRequest request
    );

    com.bookingplatform.entity.ProviderService
    updateService(
            Long providerId,
            Long serviceId,
            ServiceRequest request
    );

    void deleteService(
            Long providerId,
            Long serviceId
    );

    List<Review> getReviews(
            Long providerId
    );

    Review addReview(
            Long providerId,
            ReviewRequest request
    );


    Provider approveProvider(
            Long id
    );

    Provider rejectProvider(
            Long id
    );

    List<Provider> getNearbyProviders(
            String location,
            String city
    );

    ProviderUpdateResponse submitProfileUpdateRequest(
            Long providerId,
            ProviderUpdateRequestDto request
    );

    List<ProviderUpdateResponse> getMyUpdateRequests(
            Long providerId
    );

    ProviderUpdateResponse getPendingUpdateRequest(
            Long providerId
    );

    ProviderUpdateResponse approveUpdateRequest(
            Long requestId,
            ReviewProviderUpdateRequest request
    );

    ProviderUpdateResponse rejectUpdateRequest(
            Long requestId,
            ReviewProviderUpdateRequest request
    );

    List<ProviderUpdateResponse> getPendingUpdateRequests();


}