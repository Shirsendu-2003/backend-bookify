package com.bookingplatform.service.impl;

import com.bookingplatform.dto.provider.ProviderAdminResponse;
import com.bookingplatform.dto.provider.ProviderUpdateRequestDto;
import com.bookingplatform.dto.provider.ProviderUpdateResponse;
import com.bookingplatform.dto.provider.ReviewProviderUpdateRequest;
import com.bookingplatform.dto.request.*;
import com.bookingplatform.entity.*;
import com.bookingplatform.enums.UpdateRequestStatus;
import com.bookingplatform.enums.VerificationStatus;
import com.bookingplatform.exception.BusinessException;
import com.bookingplatform.exception.ResourceNotFoundException;
import com.bookingplatform.repository.*;
import com.bookingplatform.service.ProviderManagementService;
import com.bookingplatform.enums.BookingStatus;
import com.bookingplatform.entity.Notification;
import com.bookingplatform.repository.NotificationRepository;


import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderManagementServiceImpl
        implements ProviderManagementService {

    private final ProviderRepository providerRepository;

    private final ProviderUpdateRequestRepository updateRequestRepository;

    private final UserRepository userRepository;

    private final AvailabilityRepository
            availabilityRepository;

    private final ProviderServiceRepository
            providerServiceRepository;

    private final ReviewRepository
            reviewRepository;

    private final BookingRepository
            bookingRepository;

    private final
    NotificationRepository
            notificationRepository;

    @Override
    public List<Provider> getAllProviders() {

        return providerRepository
                .findByVerificationStatus(
                        VerificationStatus.APPROVED
                );
    }

    public List<Provider> searchProviders(
            String keyword
    ) {

        return providerRepository
                .findByNameContainingIgnoreCaseAndVerificationStatus(
                        keyword,
                        VerificationStatus.APPROVED
                );
    }

    @Override
    public Provider getProviderById(
            Long id
    ) {

        Provider provider =
                providerRepository.findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Provider not found"
                                )
                        );

        if (provider.getVerificationStatus()
                != VerificationStatus.APPROVED) {

            throw new RuntimeException(
                    "Provider not approved"
            );
        }

        return provider;
    }

    @Override
    public Provider createProvider(
            Long userId,
            ProviderRequest request
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );

        Provider provider =
                Provider.builder()
                        .name(
                                request.getName()
                        )
                        .providerType(
                                request.getProviderType()
                                        .name()
                        )
                        .experience(
                                request.getExperience()
                        )
                        .location(
                                request.getLocation()
                        )
                        .bio(
                                request.getBio()
                        )
                        .skills(
                                request.getSkills()
                        )
                        .hourlyRate(
                                request.getHourlyRate()
                        )
                        .avatar(
                                request.getAvatar()
                        )
                        .user(user)
                        .build();

        return providerRepository
                .save(provider);
    }

    @Override
    public Provider updateProvider(
            Long id,
            ProviderRequest request
    ) {

        Provider provider =
                getProviderById(id);

        provider.setName(
                request.getName()
        );

        provider.setProviderType(
                request.getProviderType()
                        .name()
        );

        provider.setExperience(
                request.getExperience()
        );

        provider.setLocation(
                request.getLocation()
        );

        provider.setBio(
                request.getBio()
        );

        provider.setSkills(
                request.getSkills()
        );

        provider.setHourlyRate(
                request.getHourlyRate()
        );


        provider.setAvatar(
                request.getAvatar()
        );

        return providerRepository
                .save(provider);
    }

    @Override
    public void deleteProvider(Long id) {

        providerRepository.deleteById(
                id
        );
    }

    @Override
    public Provider getProfile(
            String email
    ){

        System.out.println(
                "SERVICE EMAIL = " + email
        );

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->

                                new RuntimeException(
                                        "USER NOT FOUND: "
                                                + email
                                )
                        );

        System.out.println(
                "USER ID = "
                        + user.getId()
        );

        Provider provider =
                providerRepository
                        .findByUserId(user.getId())
                        .orElseThrow(() ->

                                new RuntimeException(
                                        "PROVIDER NOT FOUND FOR USER="
                                                + user.getId()
                                )
                        );

        System.out.println(
                "PROVIDER ID = "
                        + provider.getId()
        );

        Long completedBookings =

                bookingRepository
                        .countByProviderIdAndStatus(

                                provider.getId(),

                                BookingStatus.COMPLETED

                        );

        System.out.println(
                "COMPLETED BOOKINGS = "
                        + completedBookings
        );

        provider.setCompletedBookings(
                completedBookings
        );

        List<ProviderService> services =

                providerServiceRepository
                        .findByProviderId(
                                provider.getId()
                        );

        System.out.println(
                "SERVICES SIZE = "
                        + services.size()
        );

        provider.setServices(
                services
        );

        List<Review> reviews =

                reviewRepository
                        .findByProviderId(
                                provider.getId()
                        );

        System.out.println(
                "REVIEWS SIZE = "
                        + reviews.size()
        );

        provider.setReviews(
                reviews
        );

        return provider;
    }

    @Override
    public List<Availability>
    getAvailability(
            Long providerId
    ) {

        return availabilityRepository
                .findByProviderId(
                        providerId
                );
    }

    @Override
    public Availability addAvailability(
            Long providerId,
            AvailabilityRequest request
    ) {

        Provider provider =
                getProviderById(providerId);

        if (provider.getVerificationStatus()
                != VerificationStatus.APPROVED) {

            throw new RuntimeException(
                    "Provider must be approved before setting availability"
            );
        }

        DayOfWeek dayOfWeek =
                DayOfWeek.valueOf(
                        request.getDay().toUpperCase()
                );

        Availability availability =
                availabilityRepository
                        .findByProvider_IdAndDayOfWeek(
                                providerId,
                                dayOfWeek
                        )
                        .orElse(
                                new Availability()
                        );

        availability.setProvider(provider);

        availability.setDayOfWeek(dayOfWeek);

        availability.setStartTime(
                java.time.LocalTime.parse(
                        request.getStartTime()
                )
        );

        availability.setEndTime(
                java.time.LocalTime.parse(
                        request.getEndTime()
                )
        );

        availability.setAvailable(true);

        return availabilityRepository.save(
                availability
        );
    }

    @Override
    public List<
            com.bookingplatform.entity.ProviderService>
    getServices(Long providerId) {

        return providerServiceRepository
                .findByProviderId(
                        providerId
                );
    }

    @Override
    public com.bookingplatform.entity.ProviderService
    addService(
            Long providerId,
            ServiceRequest request
    ) {

        Provider provider =
                getProviderById(
                        providerId
                );

        com.bookingplatform.entity.ProviderService
                service =
                com.bookingplatform.entity
                        .ProviderService
                        .builder()
                        .title(
                                request.getTitle()
                        )
                        .description(
                                request.getDescription()
                        )
                        .price(
                                request.getPrice()
                        )
                        .provider(provider)
                        .build();

        return providerServiceRepository
                .save(service);
    }

    @Override
    public com.bookingplatform.entity.ProviderService
    updateService(
            Long providerId,
            Long serviceId,
            ServiceRequest request
    ) {

        com.bookingplatform.entity.ProviderService
                service =
                providerServiceRepository
                        .findById(serviceId)
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Service not found"
                                        )
                        );

        service.setTitle(
                request.getTitle()
        );

        service.setDescription(
                request.getDescription()
        );

        service.setPrice(
                request.getPrice()
        );

        return providerServiceRepository
                .save(service);
    }

    @Override
    public void deleteService(
            Long providerId,
            Long serviceId
    ) {

        providerServiceRepository
                .deleteById(serviceId);
    }

    @Override
    public List<Review>
    getReviews(
            Long providerId
    ) {

        return reviewRepository
                .findByProviderId(
                        providerId
                );
    }

    @Override
    public Review addReview(
            Long providerId,
            ReviewRequest request
    ) {

        Provider provider =
                getProviderById(
                        providerId
                );

        Review review =
                Review.builder()
                        .rating(
                                request.getRating()
                        )
                        .comment(
                                request.getComment()
                        )
                        .customerName(
                                request.getCustomerName()
                        )
                        .provider(provider)
                        .build();

        return reviewRepository
                .save(review);
    }

    @Override
    public Provider approveProvider(Long id){

        Provider provider =
                providerRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Provider not found"
                                )
                        );

        provider.setVerificationStatus(
                VerificationStatus.APPROVED
        );

        Provider saved =
                providerRepository.save(provider);

        notificationRepository.save(
                Notification.builder()
                        .title("Provider Approved")
                        .message("Your provider account has been approved.")
                        .user(saved.getUser())
                        .build()
        );

        return saved;
    }

    @Override
    public Provider rejectProvider(Long id){

        Provider provider =
                providerRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Provider not found"
                                )
                        );

        provider.setVerificationStatus(
                VerificationStatus.REJECTED
        );

        Provider saved =
                providerRepository.save(provider);

        notificationRepository.save(
                Notification.builder()
                        .title("Provider Rejected")
                        .message("Your provider account was rejected.")
                        .user(saved.getUser())
                        .build()
        );

        return saved;
    }

    @Override
    public List<Provider>
    getNearbyProviders(

            String location,

            String city

    ){

        return providerRepository
                .findNearbyProviders(

                        location,

                        city

                );

    }

    @Transactional
    public void updateLocation(
            String email,
            Double latitude,
            Double longitude,
            Boolean online) {

        Provider provider =
                providerRepository
                        .findByUserEmail(email)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Provider not found"
                                )
                        );

        provider.setLatitude(latitude);
        provider.setLongitude(longitude);

        providerRepository.save(provider);
    }

    @Override
    @Transactional
    public ProviderUpdateResponse submitProfileUpdateRequest(
            Long providerId,
            ProviderUpdateRequestDto request
    ) {

        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Provider not found."));

        if (updateRequestRepository.existsByProviderIdAndStatus(
                providerId,
                UpdateRequestStatus.PENDING
        )) {

            throw new BusinessException(
                    "You already have a pending update request."
            );

        }

        ProviderUpdateRequest updateRequest =
                ProviderUpdateRequest.builder()

                        .provider(provider)

                        .name(request.getName())

                        .providerType(request.getProviderType())

                        .experience(request.getExperience())

                        .location(request.getLocation())

                        .skills(request.getSkills())

                        .hourlyRate(request.getHourlyRate())

                        .bio(request.getBio())

                        .status(UpdateRequestStatus.PENDING)

                        .build();

        updateRequestRepository.save(updateRequest);

        return mapUpdateRequest(updateRequest);

    }

    @Override
    public List<ProviderUpdateResponse>
    getMyUpdateRequests(Long providerId) {

        return updateRequestRepository

                .findByProviderIdOrderByRequestedAtDesc(
                        providerId
                )

                .stream()

                .map(this::mapUpdateRequest)

                .toList();

    }

    @Override
    public ProviderUpdateResponse getPendingUpdateRequest(
            Long providerId
    ) {

        ProviderUpdateRequest request =
                updateRequestRepository

                        .findFirstByProviderIdAndStatus(

                                providerId,

                                UpdateRequestStatus.PENDING

                        )

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "No pending request found."
                                ));

        return mapUpdateRequest(request);

    }

    private ProviderUpdateResponse mapUpdateRequest(
            ProviderUpdateRequest request
    ) {

        Provider provider = request.getProvider();

        return ProviderUpdateResponse.builder()

                .id(request.getId())

                .providerId(provider.getId())

                .providerName(provider.getName())

                /* Current Profile */

                .currentName(provider.getName())

                .currentProviderType(provider.getProviderType())

                .currentExperience(provider.getExperience())

                .currentLocation(provider.getLocation())

                .currentSkills(provider.getSkills())

                .currentHourlyRate(provider.getHourlyRate())

                .currentBio(provider.getBio())

                /* Requested Changes */

                .name(request.getName())

                .providerType(request.getProviderType())

                .experience(request.getExperience())

                .location(request.getLocation())

                .skills(request.getSkills())

                .hourlyRate(request.getHourlyRate())

                .bio(request.getBio())

                .status(request.getStatus())

                .remarks(request.getRemarks())

                .requestedAt(request.getRequestedAt())

                .reviewedAt(request.getReviewedAt())

                .reviewedBy(

                        request.getReviewedBy() != null

                                ? request.getReviewedBy().getFirstName()

                                : null

                )

                .build();

    }

    @Override
    public List<ProviderUpdateResponse> getPendingUpdateRequests() {

        return updateRequestRepository

                .findByStatus(UpdateRequestStatus.PENDING)

                .stream()

                .map(this::mapUpdateRequest)

                .toList();

    }
    @Override
    @Transactional
    public ProviderUpdateResponse approveUpdateRequest(
            Long requestId,
            ReviewProviderUpdateRequest review
    ) {

        ProviderUpdateRequest request =
                updateRequestRepository.findById(requestId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Update request not found."
                                ));

        if (request.getStatus() != UpdateRequestStatus.PENDING) {
            throw new BusinessException(
                    "Request already processed."
            );
        }

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User admin = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Admin not found."
                        ));

        Provider provider = request.getProvider();

        provider.setName(request.getName());

        provider.setProviderType(String.valueOf(request.getProviderType()));

        provider.setExperience(request.getExperience());

        provider.setLocation(request.getLocation());

        provider.setSkills(request.getSkills());

        provider.setHourlyRate(request.getHourlyRate());

        provider.setBio(request.getBio());

        providerRepository.save(provider);

        request.setStatus(UpdateRequestStatus.APPROVED);

        request.setRemarks(review.getRemarks());

        request.setReviewedAt(LocalDateTime.now());

        request.setReviewedBy(admin);

        updateRequestRepository.save(request);

        return mapUpdateRequest(request);
    }

    @Override
    @Transactional
    public ProviderUpdateResponse rejectUpdateRequest(
            Long requestId,
            ReviewProviderUpdateRequest review
    ) {

        ProviderUpdateRequest request =
                updateRequestRepository.findById(requestId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Update request not found."
                                ));

        if (request.getStatus() != UpdateRequestStatus.PENDING) {
            throw new BusinessException(
                    "Request already processed."
            );
        }

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User admin = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Admin not found."
                        ));

        request.setStatus(UpdateRequestStatus.REJECTED);

        request.setRemarks(review.getRemarks());

        request.setReviewedAt(LocalDateTime.now());

        request.setReviewedBy(admin);

        updateRequestRepository.save(request);

        return mapUpdateRequest(request);
    }


}