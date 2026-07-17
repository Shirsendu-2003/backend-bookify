package com.bookingplatform.service;

import com.bookingplatform.dto.booking.*;
import com.bookingplatform.dto.common.PaginationResponse;
import com.bookingplatform.dto.provider.ProviderCandidate;
import com.bookingplatform.entity.*;
import com.bookingplatform.enums.BookingStatus;
import com.bookingplatform.exception.*;
import com.bookingplatform.repository.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import java.math.RoundingMode;
import java.time.Duration;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;
    private final
    NotificationRepository
            notificationRepository;
    private final EmailService emailService;
    private final AvailabilityRepository availabilityRepository;
    private final WorkProofRepository workProofRepository;
    private final FileStorageServices fileStorageServices;

    /*
     * ==========================
     * CREATE BOOKING
     * ==========================
     */

    /*
     * ==========================
     * GET ALL BOOKINGS
     * ==========================
     */

    public PaginationResponse<BookingResponse>
    getAllBookings(

            int page,

            int size

    ) {

        Pageable pageable =

                PageRequest.of(

                        page,

                        size,

                        Sort.by("id")
                                .descending()

                );

        Page<Booking> bookings =

                bookingRepository
                        .findAll(
                                pageable
                        );

        return mapPage(
                bookings
        );
    }
    /*
     * ==========================
     * DELETE BOOKING
     * ==========================
     */

    public void deleteBooking(Long bookingId) {

        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found."));

        workProofRepository.findByBookingId(bookingId)
                .ifPresent(proof -> {

                    fileStorageServices.delete(proof.getImageUrl());

                    workProofRepository.delete(proof);

                });

        bookingRepository.delete(booking);
    }
    /*
     * ==========================
     * UPDATE BOOKING
     * ==========================
     */

    public BookingResponse updateBooking(

            Long bookingId,

            BookingRequest request

    ) {

        Booking booking =

                bookingRepository
                        .findById(
                                bookingId
                        )

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Booking not found."
                                )
                        );

        Provider provider =

                providerRepository
                        .findById(
                                request.getProviderId()
                        )

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Provider not found."
                                )
                        );

        booking.setProvider(
                provider
        );

        booking.setServiceType(
                request.getServiceType()
        );

        booking.setDescription(
                request.getDescription()
        );

        booking.setAddress(
                request.getAddress()
        );

        booking.setCity(
                request.getCity()
        );

        booking.setState(
                request.getState()
        );

        booking.setCountry(
                request.getCountry()
        );

        booking.setZipCode(
                request.getZipCode()
        );

        booking.setBookingDate(
                request.getBookingDate()
        );

        booking.setStartTime(
                request.getStartTime()
        );

        booking.setEndTime(
                request.getEndTime()
        );

        booking.setAmount(
                request.getAmount()
        );

        Booking updated =

                bookingRepository
                        .save(
                                booking
                        );

        return mapBooking(
                updated
        );
    }

    public BookingResponse createBooking(

            Long customerId,

            BookingRequest request

    ){

        System.out.println(
                "CUSTOMER ID = " + customerId
        );

        System.out.println(
                "PROVIDER ID = "
                        + request.getProviderId()
        );

        System.out.println(
                "SERVICE TYPE = "
                        + request.getServiceType()
        );

        User customer =
                userRepository
                        .findById(customerId)
                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Customer not found."
                                )
                        );

        Provider provider =
                providerRepository
                        .findById(
                                request.getProviderId()
                        )
                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Provider not found."
                                )
                        );




        /*
         * SLOT CONFLICT CHECK
         */

        List<Booking> conflicts =

                bookingRepository
                        .findConflictingBookings(

                                provider.getId(),

                                request.getBookingDate(),

                                request.getStartTime(),

                                request.getEndTime()

                        );

        if (!conflicts.isEmpty()) {

            throw new BusinessException(
                    "Selected time slot unavailable."
            );
        }

        Booking booking =
                Booking.builder()

                        .customer(
                                customer
                        )

                        .provider(
                                provider
                        )

                        .serviceType(
                                request.getServiceType()
                        )

                        .description(
                                request.getDescription()
                        )

                        .address(
                                request.getAddress()
                        )


                        .city(
                                request.getCity()
                        )

                        .state(
                                request.getState()
                        )

                        .country(
                                request.getCountry()
                        )

                        .zipCode(
                                request.getZipCode()
                        )

                        .bookingDate(
                                request.getBookingDate()
                        )

                        .startTime(
                                request.getStartTime()
                        )

                        .endTime(null)

                        .amount(
                                provider.getHourlyRate()
                        )

                        .status(
                                BookingStatus.PENDING
                        )

                        .build();

        Booking savedBooking =

                bookingRepository.save(
                        booking
                );

        // SEND EMAIL TO PROVIDER

        User providerUser =
                provider.getUser();

        emailService.sendEmail(

                providerUser.getEmail(),

                "New Booking Request",

                """
                Hello %s,
        
                You have received a new booking request.
        
                Booking ID: %d
        
                Customer: %s %s
        
                Service Type: %s
        
                Date: %s
        
                Time: %s - %s
        
                Address: %s, %s, %s
        
                Please login and accept/reject the booking.
        
                Thank You
                """
                        .formatted(

                                providerUser.getFirstName(),

                                savedBooking.getId(),

                                customer.getFirstName(),
                                customer.getLastName(),

                                request.getServiceType(),

                                request.getBookingDate(),

                                request.getStartTime(),
                                request.getEndTime(),

                                request.getAddress(),
                                request.getCity(),
                                request.getState()
                        )
        );

        /*
         * CUSTOMER NOTIFICATION
         */

        createNotification(

                customer,

                "Booking Created",

                "Your booking request has been submitted."

        );

        createNotification(

                provider.getUser(),

                "New Booking Request",

                customer.getFirstName()
                        +
                        " booked "
                        +
                        request.getServiceType()

        );

        return mapBooking(
                savedBooking
        );
    }
    public InstantBookingResponse createInstantBooking(
            InstantBookingRequest request
    ) {

        User customer =
                userRepository.findById(
                                request.getCustomerId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found"
                                )
                        );

        List<Provider> providers =
                findNearestProviders(
                        request
                );

        if(providers.isEmpty()){

            return InstantBookingResponse
                    .builder()
                    .status("FAILED")
                    .message(
                            "No nearby provider found"
                    )
                    .build();
        }

        System.out.println(
                "LAT = " + request.getLatitude()
        );

        System.out.println(
                "LNG = " + request.getLongitude()
        );

        System.out.println(
                "SERVICE = " + request.getServiceType()
        );

        Provider firstProvider =
                providers.get(0);

        Booking booking =
                Booking.builder()

                        .customer(customer)

                        .provider(firstProvider)

                        .serviceType(
                                request.getServiceType()
                        )

                        .description(
                                request.getDescription()
                        )

                        .address(
                                request.getAddress()
                        )
                        .customerLatitude(
                                request.getLatitude()
                        )

                        .customerLongitude(
                                request.getLongitude()
                        )

                        .city(
                                request.getCity()
                        )

                        .state(
                                request.getState()
                        )

                        .country(
                                request.getCountry()
                        )

                        .zipCode(
                                request.getZipCode()
                        )

                        .bookingDate(
                                request.getBookingDate()
                        )

                        .startTime(
                                request.getStartTime()
                        )

                        .endTime(null)

                        .amount(
                                firstProvider.getHourlyRate()
                        )

                        .status(
                                BookingStatus.PENDING
                        )
                        .instantBooking(true)

                        .providerIndex(0)

                        .offerSentAt(
                                LocalDateTime.now()
                        )

                        .build();

        System.out.println("Before save");

        Booking saved =
                bookingRepository.save(
                        booking
                );

        System.out.println("After save");
        System.out.println("Booking ID = " + saved.getId());

        // SEND EMAIL TO PROVIDER

        User providerUser =
                firstProvider.getUser();

        emailService.sendEmail(

                providerUser.getEmail(),

                "Instant Booking Request",

                """
                Hello %s,
        
                An instant booking request has been assigned to you.
        
                Booking ID: %d
        
                Customer: %s %s
        
                Service Type: %s
        
                Date: %s
        
                Start Time: %s
        
                Address: %s, %s, %s
        
                Please accept within 1 minute.
        
                Thank You
                """
                        .formatted(

                                providerUser.getFirstName(),

                                saved.getId(),

                                customer.getFirstName(),
                                customer.getLastName(),

                                request.getServiceType(),

                                request.getBookingDate(),

                                request.getStartTime(),

                                request.getAddress(),
                                request.getCity(),
                                request.getState()
                        )
        );

        createNotification(
                firstProvider.getUser(),
                "Instant Booking",
                "Accept within 1 minute"
        );

        return InstantBookingResponse
                .builder()
                .bookingId(
                        saved.getId()
                )
                .providerId(
                        firstProvider.getId()
                )
                .providerName(
                        firstProvider.getUser()
                                .getFirstName()
                                + " " +
                                firstProvider.getUser()
                                        .getLastName()
                )
                .status(
                        saved.getStatus()
                                .name()
                )
                .message(
                        "Instant booking request sent to provider."
                )
                .build();
    }

    /*
     * ==========================
     * GET BOOKING BY ID
     * ==========================
     */

    public BookingResponse getBookingById(
            Long bookingId
    ) {

        Booking booking =

                bookingRepository
                        .findById(bookingId)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Booking not found."
                                )
                        );

        return mapBooking(
                booking
        );
    }

    /*
     * ==========================
     * CUSTOMER BOOKINGS
     * ==========================
     */

    public PaginationResponse<BookingResponse>
    getCustomerBookings(

            Long customerId,

            int page,

            int size

    ) {

        Pageable pageable =

                PageRequest.of(
                        page,
                        size,
                        Sort.by("id")
                                .descending()
                );

        Page<Booking> bookings =

                bookingRepository
                        .findByCustomerId(
                                customerId,
                                pageable
                        );

        return mapPage(
                bookings
        );
    }

    /*
     * ==========================
     * PROVIDER BOOKINGS
     * ==========================
     */

    public PaginationResponse<BookingResponse>
    getProviderBookings(

            Long providerId,

            int page,

            int size

    ) {

        Pageable pageable =

                PageRequest.of(
                        page,
                        size,
                        Sort.by("id")
                                .descending()
                );

        Page<Booking> bookings =

                bookingRepository
                        .findByProviderId(
                                providerId,
                                pageable
                        );

        return mapPage(
                bookings
        );
    }

    /*
     * ==========================
     * SEARCH BOOKINGS
     * ==========================
     */

    public PaginationResponse<BookingResponse>
    searchBookings(

            String keyword,

            int page,

            int size

    ) {

        Pageable pageable =

                PageRequest.of(
                        page,
                        size
                );

        Page<Booking> bookings =

                bookingRepository
                        .searchBookings(
                                keyword,
                                pageable
                        );

        return mapPage(
                bookings
        );
    }
    /*
     * ==========================
     * UPDATE BOOKING STATUS
     * ==========================
     */

    public BookingResponse updateBookingStatus(

            Long bookingId,

            UpdateBookingStatusRequest request

    ){

        Booking booking =

                bookingRepository
                        .findById(bookingId)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Booking not found."
                                )
                        );

        System.out.println(
                "BOOKING ID = "
                        + bookingId
        );

        System.out.println(
                "CURRENT STATUS = "
                        + booking.getStatus()
        );

        System.out.println(
                "NEXT STATUS = "
                        + request.getStatus()
        );

        validateStatusTransition(

                booking.getStatus(),

                request.getStatus()

        );

        booking.setStatus(
                request.getStatus()
        );

        booking.setCancellationReason(
                request.getCancellationReason()
        );

        booking.setAdminNotes(
                request.getAdminNotes()
        );

        Booking updatedBooking =

                bookingRepository
                        .save(
                                booking
                        );

        User customer =
                booking.getCustomer();

        User providerUser =
                booking.getProvider()
                        .getUser();

        switch(
                request.getStatus()
        ){

            case ACCEPTED -> {

                String otp = generateOtp();

                booking.setStartOtp(otp);

                booking.setStartOtpVerified(false);

                booking.setStartOtpGeneratedAt(
                        LocalDateTime.now()
                );

                bookingRepository.save(booking);

                createNotification(
                        customer,
                        "Booking Accepted",
                        "Your Service Start OTP: " + otp
                );
            }

            case REJECTED ->

                    createNotification(

                            customer,

                            "Booking Rejected",

                            "Provider rejected your booking."

                    );



            case IN_PROGRESS ->

                    createNotification(

                            customer,

                            "Service Started",

                            "Provider started service."

                    );

            case COMPLETED ->

                    createNotification(

                            customer,

                            "Booking Completed",

                            "Service completed successfully."

                    );

            case CANCELLED -> {

                createNotification(

                        customer,

                        "Booking Cancelled",

                        customer.getFirstName()
                                +
                                " cancelled booking."

                );

                createNotification(

                        providerUser,

                        "Booking Cancelled",

                        "Customer cancelled booking."

                );

            }

        }

        return mapBooking(
                updatedBooking
        );
    }


    private void createNotification(

            User user,

            String title,

            String message

    ){

        notificationRepository.save(

                Notification.builder()

                        .title(
                                title
                        )

                        .message(
                                message
                        )

                        .user(
                                user
                        )

                        .build()

        );

    }
    /*
     * ==========================
     * STATUS VALIDATION
     * ==========================
     */

    private void validateStatusTransition(

            BookingStatus current,

            BookingStatus next

    ){

        switch (current){

            case PENDING -> {

                if(

                        next != BookingStatus.ACCEPTED &&

                                next != BookingStatus.REJECTED &&

                                next != BookingStatus.CANCELLED

                ){

                    throw new BusinessException(
                            "Invalid transition from PENDING"
                    );

                }

            }

            case ACCEPTED -> {

                if(

                        next != BookingStatus.IN_PROGRESS &&

                                next != BookingStatus.CANCELLED

                ){

                    throw new BusinessException(
                            "Invalid transition from ACCEPTED"
                    );

                }

            }

            case IN_PROGRESS -> {

                if(

                        next != BookingStatus.COMPLETED &&

                                next != BookingStatus.CANCELLED

                ){

                    throw new BusinessException(
                            "Invalid transition from IN_PROGRESS"
                    );

                }

            }

            case COMPLETED,
                 REJECTED,
                 CANCELLED ->

                    throw new BusinessException(
                            "Final status cannot change."
                    );
        }

    }

    /*
     * ==========================
     * ENTITY → DTO MAPPER
     * ==========================
     */

    private BookingResponse mapBooking(
            Booking booking
    ) {

        String customerName =

                booking.getCustomer()
                        .getFirstName()

                        +

                        " "

                        +

                        booking.getCustomer()
                                .getLastName();

        Long providerId = null;

        String providerName = null;

        if (booking.getProvider() != null) {

            providerId =
                    booking.getProvider()
                            .getId();

            providerName =

                    booking.getProvider()
                            .getUser()
                            .getFirstName()

                            +

                            " "

                            +

                            booking.getProvider()
                                    .getUser()
                                    .getLastName();
        }

        List<Availability> availability = List.of();

        if (booking.getProvider() != null) {

            availability =
                    availabilityRepository
                            .findByProviderId(
                                    booking.getProvider()
                                            .getId()
                            );
        }

        Double customerLat =
                booking.getCustomerLatitude();

        Double customerLng =
                booking.getCustomerLongitude();

        Double distanceKm = null;

        if (
                booking.getProvider() != null
                        &&
                        customerLat != null
                        &&
                        customerLng != null
        ) {

            distanceKm =
                    calculateDistanceKm(

                            booking.getProvider()
                                    .getLatitude(),

                            booking.getProvider()
                                    .getLongitude(),

                            customerLat,

                            customerLng
                    );
        }

        WorkProof proof = workProofRepository
                .findByBookingId(booking.getId())
                .orElse(null);

        return BookingResponse
                .builder()

                .id(
                        booking.getId()
                )

                .customerId(
                        booking.getCustomer()
                                .getId()
                )

                .customerName(
                        customerName
                )

                .providerId(
                        providerId
                )

                .providerName(
                        providerName
                )

                .serviceType(
                        booking.getServiceType()
                )

                .description(
                        booking.getDescription()
                )

                .address(
                        booking.getAddress()
                )

                .city(
                        booking.getCity()
                )

                .state(
                        booking.getState()
                )

                .country(
                        booking.getCountry()
                )

                .zipCode(
                        booking.getZipCode()
                )

                .bookingDate(
                        booking.getBookingDate()
                )

                .startTime(
                        booking.getStartTime()
                )

                .endTime(
                        booking.getEndTime()
                )

                .amount(
                        booking.getAmount()
                )

                .status(
                        booking.getStatus()
                )
                .availability(
                        availability
                )

                .paymentStatus(
                        booking.getPaymentStatus()
                )

                .cancellationReason(
                        booking.getCancellationReason()
                )

                .adminNotes(
                        booking.getAdminNotes()
                )

                .createdAt(
                        booking.getCreatedAt()
                )

                .updatedAt(
                        booking.getUpdatedAt()
                )
                .customerLatitude(
                        booking.getCustomerLatitude()
                )

                .customerLongitude(
                        booking.getCustomerLongitude()
                )

                .distanceKm(
                        distanceKm
                )
                .proofImage(
                        proof != null ? proof.getImageUrl() : null
                )
                .proofLatitude(
                        proof != null ? proof.getLatitude() : null
                )
                .proofLongitude(
                        proof != null ? proof.getLongitude() : null
                )
                .proofAddress(
                        proof != null ? proof.getAddress() : null
                )
                .proofCapturedAt(
                        proof != null ? proof.getCapturedAt() : null
                )
                .proofDeviceName(
                        proof != null ? proof.getDeviceName() : null
                )
                .proofNetworkType(
                        proof != null ? proof.getNetworkType() : null
                )
                .geoVerified(
                        proof != null ? proof.getGeoVerified() : null
                )

                .build();
    }

    /*
     * ==========================
     * PAGINATION HELPER
     * ==========================
     */

    private PaginationResponse
            <BookingResponse>

    mapPage(

            Page<Booking> page

    ) {

        return PaginationResponse

                .<BookingResponse>builder()

                .content(

                        page.getContent()

                                .stream()

                                .map(
                                        this::mapBooking
                                )

                                .toList()

                )

                .page(
                        page.getNumber()
                )

                .size(
                        page.getSize()
                )

                .totalElements(
                        page.getTotalElements()
                )

                .totalPages(
                        page.getTotalPages()
                )

                .first(
                        page.isFirst()
                )

                .last(
                        page.isLast()
                )

                .build();
    }

    private String generateOtp(){

        return String.valueOf(

                java.util.concurrent
                        .ThreadLocalRandom
                        .current()
                        .nextInt(
                                100000,
                                999999
                        )

        );
    }

    public BookingResponse acceptBooking(
            Long bookingId
    ){

        Booking booking =
                bookingRepository
                        .findById(bookingId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Booking not found"
                                )
                        );

        if(
                booking.getStatus()
                        != BookingStatus.PENDING
        ){
            throw new BusinessException(
                    "Booking already processed."
            );
        }

        String otp =
                generateOtp();

        booking.setStatus(
                BookingStatus.ACCEPTED
        );

        booking.setInstantBooking(
                false
        );

        booking.setStartOtp(
                otp
        );

        booking.setStartOtpVerified(
                false
        );

        booking.setStartOtpGeneratedAt(
                LocalDateTime.now()
        );

        Booking saved =
                bookingRepository.save(
                        booking
                );

        User customer =
                booking.getCustomer();

        emailService.sendEmail(

                customer.getEmail(),

                "Booking Accepted",

                """
                Hello %s,
    
                Your booking has been accepted.
    
                Booking ID: %d
    
                Start OTP: %s
    
                Give this OTP to the provider
                when work starts.
                """
                        .formatted(
                                customer.getFirstName(),
                                booking.getId(),
                                otp
                        )
        );

        createNotification(

                customer,

                "Booking Accepted",

                "Start OTP sent successfully."

        );

        return mapBooking(
                saved
        );
    }

    public BookingResponse rejectBooking(Long bookingId) {

        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found"));

        /*
         * INSTANT BOOKING
         */
        if (Boolean.TRUE.equals(booking.getInstantBooking())) {

            List<Provider> providers = getEligibleProviders(
                    booking.getCustomerLatitude(),
                    booking.getCustomerLongitude(),
                    booking.getServiceType()
            );

            // Remove current provider from list
            providers = providers.stream()
                    .filter(p -> !p.getId().equals(booking.getProvider().getId()))
                    .toList();

            if (providers.isEmpty()) {

                booking.setStatus(BookingStatus.REJECTED);
                booking.setInstantBooking(false);

                Booking saved = bookingRepository.save(booking);

                createNotification(
                        booking.getCustomer(),
                        "Booking Failed",
                        "No nearby service professional accepted your booking."
                );

                return mapBooking(saved);
            }

            Provider nextProvider = providers.get(0);

            booking.setProvider(nextProvider);

            booking.setOfferSentAt(LocalDateTime.now());

            Booking saved = bookingRepository.save(booking);

            emailService.sendEmail(
                    nextProvider.getUser().getEmail(),
                    "Instant Booking Request",
                    """
                    Hello %s,
        
                    You have received a new instant booking request.
        
                    Booking ID: %d
        
                    Please accept within 1 minute.
                    """
                            .formatted(
                                    nextProvider.getUser().getFirstName(),
                                    saved.getId()
                            )
            );

            createNotification(
                    nextProvider.getUser(),
                    "Instant Booking",
                    "Accept within 1 minute."
            );

            return mapBooking(saved);
        }

        /*
         * NORMAL BOOKING
         */
        booking.setStatus(BookingStatus.REJECTED);

        Booking saved = bookingRepository.save(booking);

        createNotification(
                booking.getCustomer(),
                "Booking Rejected",
                "Provider rejected your booking."
        );

        return mapBooking(saved);
    }

    public BookingResponse verifyStartOtp(

            Long bookingId,

            String otp

    ){

        Booking booking =
                bookingRepository
                        .findById(bookingId)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Booking not found"
                                        )
                        );

        if(
                booking.getStatus()
                        != BookingStatus.ACCEPTED
        ){
            throw new BusinessException(
                    "Booking not awaiting OTP verification."
            );
        }

        if(
                booking.getStartOtp() == null ||

                        !booking.getStartOtp()
                                .equals(otp)
        ){

            throw new BusinessException(
                    "Invalid OTP"
            );
        }

        booking.setStartOtpVerified(
                true
        );

        booking.setStatus(
                BookingStatus.IN_PROGRESS
        );

        LocalDateTime now = LocalDateTime.now();

        booking.setServiceStartTime(now);

        booking.setStartTime(
                now.toLocalTime()
        );

        Booking saved =
                bookingRepository.save(
                        booking
                );

        createNotification(

                booking.getCustomer(),

                "Service Started",

                "Provider has started the work."

        );

        return mapBooking(
                saved
        );
    }

    public void generateCompletionOtp(
            Long bookingId
    ){

        Booking booking =
                bookingRepository
                        .findById(bookingId)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Booking not found"
                                        )
                        );

        if(
                booking.getStatus()
                        != BookingStatus.IN_PROGRESS
        ){
            throw new BusinessException(
                    "Booking not in progress."
            );
        }

        String otp =
                generateOtp();

        booking.setCompletionOtp(
                otp
        );

        booking.setCompletionOtpVerified(
                false
        );

        booking.setCompletionOtpGeneratedAt(
                LocalDateTime.now()
        );

        bookingRepository.save(
                booking
        );

        User customer =
                booking.getCustomer();

        emailService.sendEmail(

                customer.getEmail(),

                "Completion Verification",

                """
                Hello %s,
    
                Work has been completed.
    
                Booking ID: %d
    
                Completion OTP: %s
    
                Share this OTP only after
                verifying the service.
                """
                        .formatted(
                                customer.getFirstName(),
                                booking.getId(),
                                otp
                        )
        );

        createNotification(

                customer,

                "Completion OTP",

                "Completion OTP sent."

        );
    }

    public BookingResponse verifyCompletionOtp(

            Long bookingId,

            String otp

    ){

        Booking booking =
                bookingRepository
                        .findById(bookingId)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Booking not found"
                                        )
                        );

        if(
                booking.getStatus()
                        != BookingStatus.IN_PROGRESS
        ){
            throw new BusinessException(
                    "Booking not in progress."
            );
        }

        if(
                booking.getCompletionOtp() == null ||

                        !booking.getCompletionOtp()
                                .equals(otp)
        ){

            throw new BusinessException(
                    "Invalid OTP"
            );
        }

        booking.setCompletionOtpVerified(
                true
        );

        booking.setStatus(
                BookingStatus.COMPLETED
        );
        if (booking.getServiceEndTime() == null) {

            LocalDateTime now = LocalDateTime.now();

            booking.setServiceEndTime(now);

            booking.setEndTime(now.toLocalTime());
        }

        Booking saved =
                bookingRepository.save(
                        booking
                );

        createNotification(

                booking.getCustomer(),

                "Booking Completed",

                "Service completed successfully."

        );

        createNotification(

                booking.getProvider()
                        .getUser(),

                "Booking Completed",

                "Job completed successfully."

        );

        return mapBooking(
                saved
        );
    }

    public void uploadWorkProof(
            Long bookingId,
            MultipartFile photo,
            Double latitude,
            Double longitude,
            String address
    ) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found."));

        if (booking.getStatus() != BookingStatus.IN_PROGRESS) {
            throw new BusinessException("Booking is not in progress.");
        }

        if (workProofRepository.existsByBookingId(bookingId)) {
            throw new BusinessException("Work proof already uploaded.");
        }

        if (photo == null || photo.isEmpty()) {
            throw new BusinessException("Please upload a photo.");
        }

        String contentType = photo.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BusinessException("Only image files are allowed.");
        }

        if (photo.getSize() > 5 * 1024 * 1024) {
            throw new BusinessException("Image size must be less than 5 MB.");
        }

        if (latitude == null || longitude == null) {
            throw new BusinessException("Current location is required.");
        }

        if (latitude < -90 || latitude > 90) {
            throw new BusinessException("Invalid latitude.");
        }

        if (longitude < -180 || longitude > 180) {
            throw new BusinessException("Invalid longitude.");
        }

        if (address == null || address.isBlank()) {
            address = "Unknown";
        }

        String imageUrl;

        try {
            imageUrl = fileStorageServices.save(photo);
        } catch (IOException e) {
            throw new BusinessException("Unable to upload work proof image.");
        }

        WorkProof proof = WorkProof.builder()
                .booking(booking)
                .imageUrl(imageUrl)
                .latitude(latitude)
                .longitude(longitude)
                .address(address)
                .capturedAt(LocalDateTime.now())
                .geoVerified(true)
                .build();

        workProofRepository.save(proof);
    }

    public BookingResponse finishWork(Long bookingId) {

        Booking booking =
                bookingRepository
                        .findById(bookingId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Booking not found"
                                )
                        );

        if (booking.getStatus() != BookingStatus.IN_PROGRESS) {
            throw new BusinessException(
                    "Booking not in progress."
            );
        }

        if (booking.getServiceStartTime() == null) {
            throw new BusinessException(
                    "Service start time missing."
            );
        }

        WorkProof proof = workProofRepository
                .findByBookingId(bookingId)
                .orElseThrow(() ->
                        new BusinessException(
                                "Please upload work completion proof first."
                        ));

        if (booking.getServiceEndTime() != null) {
            throw new BusinessException(
                    "Work already finished."
            );
        }

        booking.setServiceEndTime(
                LocalDateTime.now()
        );

        booking.setEndTime(booking.getServiceEndTime().toLocalTime());

        Duration duration =
                Duration.between(
                        booking.getServiceStartTime(),
                        booking.getServiceEndTime()
                );

        long minutes =
                duration.toMinutes();

        booking.setTotalMinutes(minutes);

        BigDecimal hourlyRate =
                booking.getProvider()
                        .getHourlyRate();

        BigDecimal amount;

        if (minutes <= 60) {

            amount = hourlyRate;

        } else {

            amount = hourlyRate.multiply(
                    BigDecimal.valueOf(minutes)
                            .divide(
                                    BigDecimal.valueOf(60),
                                    2,
                                    RoundingMode.HALF_UP
                            )
            );
        }

        booking.setAmount(amount);

        bookingRepository.save(booking);

        generateCompletionOtp(bookingId);

        return mapBooking(booking);
    }


    private List<Provider> findNearestProviders(
            InstantBookingRequest request
    ) {

        double[] radiusLevels = {
                5000,
                10000,
                20000,
                30000,
                50000
        };

        for (double radius : radiusLevels) {

            List<Provider> providers =
                    providerRepository
                            .findNearbyProvidersByRadius(
                                    request.getLatitude(),
                                    request.getLongitude(),
                                    radius,
                                    request.getServiceType()
                            );

            List<ProviderCandidate> candidates =
                    new ArrayList<>();

            for (Provider provider : providers) {

                /*
                 * ONLINE
                 */
                if (!Boolean.TRUE.equals(
                        provider.getOnline()
                )) {
                    continue;
                }

                /*
                 * AVAILABLE
                 */
                if (!Boolean.TRUE.equals(
                        provider.getAvailable()
                )) {
                    continue;
                }

                /*
                 * BUSY
                 */
                boolean busy =
                        bookingRepository
                                .existsByProviderIdAndStatusIn(
                                        provider.getId(),
                                        List.of(
                                                BookingStatus.ACCEPTED,
                                                BookingStatus.IN_PROGRESS
                                        )
                                );

                if (busy) {
                    continue;
                }

                /*
                 * DAILY LIMIT
                 */
                long todayJobs =
                        bookingRepository
                                .countByProviderIdAndBookingDate(
                                        provider.getId(),
                                        request.getBookingDate()
                                );

                if (todayJobs >= 3) {
                    continue;
                }

                /*
                 * DISTANCE SCORE
                 */
                double distance =
                        calculateDistanceKm(
                                request.getLatitude(),
                                request.getLongitude(),
                                provider.getLatitude(),
                                provider.getLongitude()
                        );

                double distanceScore;

                if (distance <= 5) {
                    distanceScore = 100;
                } else if (distance <= 10) {
                    distanceScore = 80;
                } else if (distance <= 20) {
                    distanceScore = 60;
                } else if (distance <= 30) {
                    distanceScore = 40;
                } else {
                    distanceScore = 20;
                }

                /*
                 * RATING SCORE
                 */
                double ratingScore =
                        provider.getAverageRating() * 20;

                /*
                 * EXPERIENCE SCORE
                 */
                long completed =
                        bookingRepository
                                .countByProviderIdAndStatus(
                                        provider.getId(),
                                        BookingStatus.COMPLETED
                                );

                double completedScore;

                if (completed >= 500) {
                    completedScore = 100;
                } else if (completed >= 200) {
                    completedScore = 80;
                } else if (completed >= 50) {
                    completedScore = 60;
                } else {
                    completedScore = 30;
                }

                /*
                 * WORKLOAD SCORE
                 */
                double workloadScore =
                        (3 - todayJobs) * 30;

                /*
                 * FINAL SCORE
                 */
                double finalScore =

                        distanceScore * 0.40 +

                                ratingScore * 0.30 +

                                completedScore * 0.20 +

                                workloadScore * 0.10;

                candidates.add(
                        new ProviderCandidate(
                                provider,
                                finalScore
                        )
                );
            }

            if (!candidates.isEmpty()) {

                candidates.sort(
                        Comparator.comparing(
                                        ProviderCandidate::getScore
                                )
                                .reversed()
                );

                return candidates
                        .stream()
                        .map(
                                ProviderCandidate::getProvider
                        )
                        .toList();
            }
        }

        return List.of();
    }

    public List<Provider> getEligibleProviders(
            Double latitude,
            Double longitude,
            String serviceType
    ) {

        List<Provider> providers =
                providerRepository.findNearbyProvidersByRadius(
                        latitude,
                        longitude,
                        50000.0,
                        serviceType
                );

        return providers.stream()
                .filter(provider ->
                        !bookingRepository
                                .existsByProviderIdAndStatusIn(
                                        provider.getId(),
                                        List.of(
                                                BookingStatus.PENDING,
                                                BookingStatus.ACCEPTED,
                                                BookingStatus.IN_PROGRESS
                                        )
                                )
                )
                .toList();
    }

    private double calculateDistanceKm(

            Double lat1,
            Double lon1,

            Double lat2,
            Double lon2

    ) {

        final int EARTH_RADIUS = 6371;

        double latDistance =
                Math.toRadians(
                        lat2 - lat1
                );

        double lonDistance =
                Math.toRadians(
                        lon2 - lon1
                );

        double a =

                Math.sin(
                        latDistance / 2
                )
                        *
                        Math.sin(
                                latDistance / 2
                        )

                        +

                        Math.cos(
                                Math.toRadians(
                                        lat1
                                )
                        )

                                *

                                Math.cos(
                                        Math.toRadians(
                                                lat2
                                        )
                                )

                                *

                                Math.sin(
                                        lonDistance / 2
                                )

                                *

                                Math.sin(
                                        lonDistance / 2
                                );

        double c =
                2 *
                        Math.atan2(
                                Math.sqrt(a),
                                Math.sqrt(1 - a)
                        );

        return EARTH_RADIUS * c;
    }
}