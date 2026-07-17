package com.bookingplatform.dto.booking;

import com.bookingplatform.entity.Availability;
import com.bookingplatform.enums.BookingStatus;
import com.bookingplatform.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

    private Long id;

    /*
     * CUSTOMER INFO
     */

    private Long customerId;

    private String customerName;

    /*
     * PROVIDER INFO
     */

    private Long providerId;

    private String providerName;

    private String serviceType;

    /*
     * BOOKING DETAILS
     */

    private String description;

    private String address;

    private String city;

    private String state;

    private String country;

    private String zipCode;

    /*
     * DATE & TIME
     */

    private LocalDate bookingDate;

    private LocalTime startTime;

    private LocalTime endTime;

    /*
     * PAYMENT
     */

    private BigDecimal amount;

    /*
     * STATUS
     */

    private BookingStatus status;

    private PaymentStatus paymentStatus;

    private String cancellationReason;

    private String adminNotes;

    /*
     * AUDIT
     */

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<Availability> availability;

    private Double customerLatitude;

    private Double customerLongitude;

    private Double distanceKm;


    // ===============================
    // WORK PROOF
    // ===============================

    /**
     * Uploaded completion photo
     */
    private String proofImage;

    /**
     * Provider location while uploading proof
     */
    private Double proofLatitude;
    private Double proofLongitude;

    /**
     * Human-readable address
     */
    private String proofAddress;

    /**
     * Time when proof was uploaded
     */
    private LocalDateTime proofCapturedAt;

    /**
     * Optional device information
     */
    private String proofDeviceName;

    /**
     * WIFI / MOBILE
     */
    private String proofNetworkType;

    /**
     * GPS validation flag
     */
    private Boolean geoVerified;

}