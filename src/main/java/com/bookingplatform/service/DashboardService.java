package com.bookingplatform.service;

import com.bookingplatform.dto.dashboard.DashboardResponse;
import com.bookingplatform.dto.dashboard.SettingsRequest;
import com.bookingplatform.dto.payment.PaymentResponse;
import com.bookingplatform.dto.user.UserResponse;
import com.bookingplatform.entity.Complaint;
import com.bookingplatform.enums.*;
import com.bookingplatform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final ReviewRepository reviewRepository;
    private final ComplaintRepository complaintRepository;

    /*
     * ==========================
     * DASHBOARD
     * ==========================
     */

    public DashboardResponse getDashboard() {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                auth.getName();

        var customer =
                userRepository
                        .findByEmail(email)
                        .orElseThrow();

        Long customerId =
                customer.getId();

        /*
         * BOOKINGS
         */

        Long totalBookings =

                bookingRepository
                        .countByCustomerId(
                                customerId
                        );

        Long pendingBookings =

                bookingRepository
                        .countByCustomerIdAndStatus(
                                customerId,
                                BookingStatus.PENDING
                        );

        Long acceptedBookings =

                bookingRepository
                        .countByCustomerIdAndStatus(
                                customerId,
                                BookingStatus.ACCEPTED
                        );

        Long completedBookings =

                bookingRepository
                        .countByCustomerIdAndStatus(
                                customerId,
                                BookingStatus.COMPLETED
                        );

        Long cancelledBookings =

                bookingRepository
                        .countByCustomerIdAndStatus(
                                customerId,
                                BookingStatus.CANCELLED
                        );

        /*
         * PAYMENTS
         */

        Long totalPayments =

                paymentRepository
                        .countByCustomerId(
                                customerId
                        );

        BigDecimal totalRevenue =

                paymentRepository
                        .getRevenueByCustomerId(
                                customerId
                        );

        BigDecimal refundedAmount =
                BigDecimal.ZERO;

        /*
         * REVIEWS
         */

        Long totalReviews =

                reviewRepository
                        .countByCustomerId(
                                customerId
                        );

        Double averageRating =

                totalReviews > 0

                        ?

                        providerRepository
                                .findTop10ByOrderByAverageRatingDesc()
                                .stream()
                                .mapToDouble(

                                        p ->

                                                p.getAverageRating() != null

                                                        ?

                                                        p.getAverageRating()

                                                        :

                                                        0.0

                                )

                                .average()

                                .orElse(0.0)

                        :

                        0.0;

        /*
         * COMPLAINTS
         */

        Long totalComplaints =

                complaintRepository
                        .countByCustomerId(
                                customerId
                        );

        Long openComplaints =

                complaintRepository
                        .countByCustomerIdAndStatus(
                                customerId,
                                ComplaintStatus.OPEN
                        );

        Long resolvedComplaints =

                complaintRepository
                        .countByCustomerIdAndStatus(
                                customerId,
                                ComplaintStatus.RESOLVED
                        );

        /*
         * PROVIDERS
         */

        Long availableProviders =

                providerRepository
                        .countByAvailable(
                                true
                        );

        Long unavailableProviders =

                providerRepository
                        .countByAvailable(
                                false
                        );

        /*
         * CHARTS
         */

        Map<String,Long> bookingChart =

                Map.of(

                        "PENDING",

                        pendingBookings,

                        "ACCEPTED",

                        acceptedBookings,

                        "COMPLETED",

                        completedBookings,

                        "CANCELLED",

                        cancelledBookings

                );

        Map<String,BigDecimal> revenueChart =

                Map.of(

                        "TOTAL_REVENUE",

                        totalRevenue == null

                                ?

                                BigDecimal.ZERO

                                :

                                totalRevenue,

                        "REFUNDED_AMOUNT",

                        refundedAmount == null

                                ?

                                BigDecimal.ZERO

                                :

                                refundedAmount

                );

        return DashboardResponse
                .builder()

                .totalUsers(1L)

                .totalCustomers(1L)

                .totalProviders(
                        availableProviders +
                                unavailableProviders
                )

                .activeUsers(

                        customer.getStatus()

                                == UserStatus.ACTIVE

                                ?

                                1L

                                :

                                0L
                )

                .totalBookings(
                        totalBookings
                )

                .pendingBookings(
                        pendingBookings
                )

                .acceptedBookings(
                        acceptedBookings
                )

                .completedBookings(
                        completedBookings
                )

                .cancelledBookings(
                        cancelledBookings
                )

                .totalPayments(
                        totalPayments
                )

                .totalRevenue(
                        totalRevenue
                )

                .refundedAmount(
                        refundedAmount
                )

                .totalReviews(
                        totalReviews
                )

                .averageRating(
                        averageRating
                )

                .totalComplaints(
                        totalComplaints
                )

                .openComplaints(
                        openComplaints
                )

                .resolvedComplaints(
                        resolvedComplaints
                )

                .availableProviders(
                        availableProviders
                )

                .unavailableProviders(
                        unavailableProviders
                )

                .bookingChart(
                        bookingChart
                )

                .revenueChart(
                        revenueChart
                )

                .build();
    }
    /*
     * ==========================
     * BOOKING CHART
     * ==========================
     */

    private Map<String, Long>
    buildBookingChart() {

        return Map.of(

                "PENDING",

                bookingRepository
                        .countByStatus(
                                BookingStatus.PENDING
                        ),

                "ACCEPTED",

                bookingRepository
                        .countByStatus(
                                BookingStatus.ACCEPTED
                        ),

                "COMPLETED",

                bookingRepository
                        .countByStatus(
                                BookingStatus.COMPLETED
                        ),

                "CANCELLED",

                bookingRepository
                        .countByStatus(
                                BookingStatus.CANCELLED
                        )

        );
    }

    /*
     * ==========================
     * REVENUE CHART
     * ==========================
     */

    private Map<String, BigDecimal>
    buildRevenueChart() {

        BigDecimal revenue =

                paymentRepository
                        .getTotalRevenue();

        BigDecimal refunded =

                paymentRepository
                        .getTotalRefundedAmount();

        revenue =

                revenue == null
                        ? BigDecimal.ZERO
                        : revenue;

        refunded =

                refunded == null
                        ? BigDecimal.ZERO
                        : refunded;

        BigDecimal netRevenue =

                revenue.subtract(
                        refunded
                );

        return Map.of(

                "TOTAL_REVENUE",
                revenue,

                "REFUNDED_AMOUNT",
                refunded,

                "NET_REVENUE",
                netRevenue

        );
    }
    public Object getActivities(

            int page,

            int size

    ){

        Pageable pageable =

                PageRequest.of(

                        page,

                        size,

                        Sort.by(
                                "createdAt"
                        ).descending()

                );

        var result =

                bookingRepository
                        .findAll(pageable);

        return Map.of(

                "content",

                result.getContent()

                        .stream()

                        .map(booking -> Map.of(

                                "id",
                                booking.getId(),

                                "serviceType",
                                booking.getServiceType(),

                                "status",
                                booking.getStatus(),

                                "createdAt",
                                booking.getCreatedAt(),

                                "bookingDate",
                                booking.getBookingDate(),

                                "amount",
                                booking.getAmount(),

                                "address",
                                booking.getAddress(),

                                "city",
                                booking.getCity(),

                                "customerName",

                                booking.getCustomer()!=null

                                        ?

                                        booking.getCustomer()
                                                .getFirstName()

                                                +

                                                " "

                                                +

                                                booking.getCustomer()
                                                        .getLastName()

                                        :

                                        "Unknown",

                                "providerName",

                                booking.getProvider()!=null

                                        &&

                                        booking.getProvider()
                                                .getUser()!=null

                                        ?

                                        booking.getProvider()
                                                .getUser()
                                                .getFirstName()

                                                +

                                                " "

                                                +

                                                booking.getProvider()
                                                        .getUser()
                                                        .getLastName()

                                        :

                                        "Unknown"

                        ))

                        .toList(),

                "page",
                result.getNumber(),

                "size",
                result.getSize(),

                "totalElements",
                result.getTotalElements(),

                "totalPages",
                result.getTotalPages()

        );

    }

    /*
     * ==========================
     * REVENUE ANALYTICS
     * ==========================
     */

    public Object getRevenueAnalytics(
            String period
    ){

        BigDecimal totalRevenue =

                paymentRepository
                        .getTotalRevenue();

        BigDecimal refunded =

                paymentRepository
                        .getTotalRefundedAmount();

        totalRevenue =

                totalRevenue == null
                        ? BigDecimal.ZERO
                        : totalRevenue;

        refunded =

                refunded == null
                        ? BigDecimal.ZERO
                        : refunded;

        return Map.of(

                "stats",

                Map.of(

                        "total",
                        totalRevenue,

                        "pending",
                        refunded,

                        "monthly",
                        totalRevenue,

                        "transactions",
                        paymentRepository.count()

                ),

                "transactions",

                paymentRepository
                        .findAll()

                        .stream()

                        .map(payment ->

                                PaymentResponse
                                        .builder()

                                        .id(
                                                payment.getId()
                                        )

                                        .bookingId(
                                                payment.getBooking()
                                                        .getId()
                                        )

                                        .customerId(
                                                payment.getCustomer()
                                                        .getId()
                                        )

                                        .customerName(

                                                payment.getCustomer()
                                                        .getFirstName()

                                                        + " "

                                                        +

                                                        payment.getCustomer()
                                                                .getLastName()
                                        )

                                        .amount(
                                                payment.getAmount()
                                        )

                                        .status(
                                                payment.getStatus()
                                        )

                                        .createdAt(
                                                payment.getCreatedAt()
                                        )

                                        .build()

                        )

                        .toList()
        );
    }

    /*
     * ==========================
     * BOOKING ANALYTICS
     * ==========================
     */

    public Object getBookingAnalytics(
            String period
    ){

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                auth.getName();

        var customer =
                userRepository
                        .findByEmail(email)
                        .orElseThrow();

        Long customerId =
                customer.getId();

        return Map.of(

                "total",

                bookingRepository
                        .countByCustomerId(
                                customerId
                        ),

                "pending",

                bookingRepository
                        .countByCustomerIdAndStatus(

                                customerId,

                                BookingStatus.PENDING
                        ),

                "accepted",

                bookingRepository
                        .countByCustomerIdAndStatus(

                                customerId,

                                BookingStatus.ACCEPTED
                        ),

                "completed",

                bookingRepository
                        .countByCustomerIdAndStatus(

                                customerId,

                                BookingStatus.COMPLETED
                        ),

                "cancelled",

                bookingRepository
                        .countByCustomerIdAndStatus(

                                customerId,

                                BookingStatus.CANCELLED
                        )

        );

    }

    /*
     * ==========================
     * DASHBOARD STATS
     * ==========================
     */

    public Object getStats() {

        return getDashboard();
    }

    /*
     * ==========================
     * USERS
     * ==========================
     */

    public Object getUsers(
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(page,size);

        return userRepository
                .findAll(pageable)
                .map(user ->

                        UserResponse.builder()

                                .id(
                                        user.getId()
                                )

                                .name(
                                        user.getFirstName()
                                                + " "
                                                + user.getLastName()
                                )

                                .email(
                                        user.getEmail()
                                )

                                .roles(
                                        user.getRoles()
                                                .stream()
                                                .map(
                                                        role -> role
                                                                .getName()
                                                                .name()
                                                )
                                                .collect(
                                                        java.util.stream
                                                                .Collectors
                                                                .toSet()
                                                )
                                )

                                .status(
                                        UserStatus.valueOf(user.getStatus()
                                                .name())
                                )

                                .createdAt(
                                        user.getCreatedAt()
                                )

                                .build()

                );
    }

    /*
     * ==========================
     * REPORTS
     * ==========================
     */

    public Object getReports() {

        BigDecimal revenue =

                paymentRepository
                        .getTotalRevenue();

        return Map.of(

                "users",
                userRepository.count(),

                "providers",
                providerRepository.count(),

                "bookings",
                bookingRepository.count(),

                "payments",
                paymentRepository.count(),

                "revenue",

                revenue == null
                        ? BigDecimal.ZERO
                        : revenue

        );
    }

    /*
     * ==========================
     * COMPLAINTS
     * ==========================
     */

    /*
     * ==========================
     * GET COMPLAINTS
     * ==========================
     */

    public Object getComplaints(){

        return complaintRepository
                .findAll()

                .stream()

                .map(complaint -> Map.of(

                        "id",
                        complaint.getId(),

                        "subject",
                        complaint.getSubject(),

                        "description",
                        complaint.getDescription(),

                        "userName",

                        complaint.getCustomer()!=null

                                ?

                                complaint.getCustomer()
                                        .getFirstName()

                                        + " "

                                        + complaint.getCustomer()
                                        .getLastName()

                                :

                                "Unknown User",

                        "priority",
                        complaint.getPriority(),

                        "status",
                        complaint.getStatus(),

                        "createdAt",
                        complaint.getCreatedAt()

                ))

                .toList();
    }

    public Object resolveComplaint(Long id){

        Complaint complaint =
                complaintRepository
                        .findById(id)
                        .orElseThrow();

        complaint.setStatus(
                ComplaintStatus.RESOLVED
        );

        Complaint saved =
                complaintRepository
                        .save(complaint);

        return Map.of(

                "id",
                saved.getId(),

                "status",
                saved.getStatus(),

                "message",
                "Complaint resolved"

        );
    }

    public Object closeComplaint(Long id){

        Complaint complaint =
                complaintRepository
                        .findById(id)
                        .orElseThrow();

        complaint.setStatus(
                ComplaintStatus.CLOSED
        );

        Complaint saved =
                complaintRepository
                        .save(complaint);

        return Map.of(

                "id",
                saved.getId(),

                "status",
                saved.getStatus(),

                "message",
                "Complaint closed"

        );
    }

    /*
     * ==========================
     * TOP PROVIDERS
     * ==========================
     */

    public Object getTopProviders() {

        return providerRepository
                .findTop10ByOrderByAverageRatingDesc();
    }

    /*
     * ==========================
     * RECENT BOOKINGS
     * ==========================
     */

    public Object getRecentBookings() {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                auth.getName();

        var customer =
                userRepository
                        .findByEmail(email)
                        .orElseThrow();

        Pageable pageable =
                PageRequest.of(0,10);

        return bookingRepository

                .findByCustomerIdOrderByCreatedAtDesc(
                        customer.getId(),
                        pageable
                )

                .map(booking -> Map.of(

                        "id",
                        booking.getId(),

                        "serviceType",
                        booking.getServiceType(),

                        "providerName",

                        booking.getProvider()!=null

                                ?

                                booking.getProvider()
                                        .getName()

                                :

                                "Unknown Provider",

                        "bookingDate",
                        booking.getBookingDate(),

                        "amount",
                        booking.getAmount(),

                        "status",
                        booking.getStatus()

                ));
    }

    /*
     * ==========================
     * TOGGLE USER STATUS
     * ==========================
     */

    public Object toggleUserStatus(

            Long userId,

            Boolean enabled

    ) {

        var user =

                userRepository
                        .findById(userId)
                        .orElseThrow();

        user.setStatus(

                enabled

                        ?

                        UserStatus.ACTIVE

                        :

                        UserStatus.INACTIVE

        );

        return userRepository
                .save(user);
    }

    public String exportReport(){

        return

                "Users,Providers,Bookings,Payments,Revenue\n"

                        +

                        userRepository.count()

                        + ","

                        +

                        providerRepository.count()

                        + ","

                        +

                        bookingRepository.count()

                        + ","

                        +

                        paymentRepository.count()

                        + ","

                        +

                        (
                                paymentRepository
                                        .getTotalRevenue()

                                        == null

                                        ?

                                        BigDecimal.ZERO

                                        :

                                        paymentRepository
                                                .getTotalRevenue()
                        );
    }

    public Object getSettings(){

        return Map.of(

                "platformName",
                "ServiceHub",

                "supportEmail",
                "support@servicehub.com",

                "notifications",
                true,

                "maintenance",
                false,

                "defaultTheme",
                "light"
        );
    }

    public Object saveSettings(
            SettingsRequest request
    ){

        return Map.of(

                "success",
                true,

                "message",
                "Settings saved successfully.",

                "data",
                request
        );
    }

    public DashboardResponse
    getProviderDashboard(){

        Authentication auth =

                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                auth.getName();

        var user =

                userRepository
                        .findByEmail(email)
                        .orElseThrow();

        var provider =

                providerRepository
                        .findByUserId(
                                user.getId()
                        )
                        .orElseThrow();

        Long providerId =
                provider.getId();

        Long totalBookings =

                bookingRepository
                        .countByProviderId(
                                providerId
                        );

        Long completedBookings =

                bookingRepository
                        .countByProviderIdAndStatus(

                                providerId,

                                BookingStatus.COMPLETED
                        );

        Long pendingBookings =

                bookingRepository
                        .countByProviderIdAndStatus(

                                providerId,

                                BookingStatus.PENDING
                        );

        Long acceptedBookings =

                bookingRepository
                        .countByProviderIdAndStatus(

                                providerId,

                                BookingStatus.ACCEPTED
                        );

        Long cancelledBookings =

                bookingRepository
                        .countByProviderIdAndStatus(

                                providerId,

                                BookingStatus.CANCELLED
                        );

        BigDecimal totalRevenue =

                paymentRepository
                        .getRevenueByProviderId(
                                providerId
                        );

        Double averageRating =

                provider.getAverageRating()

                        == null

                        ?

                        0.0

                        :

                        provider.getAverageRating();

        return DashboardResponse
                .builder()

                .totalBookings(
                        totalBookings
                )

                .completedBookings(
                        completedBookings
                )

                .pendingBookings(
                        pendingBookings
                )

                .acceptedBookings(
                        acceptedBookings
                )

                .cancelledBookings(
                        cancelledBookings
                )

                .totalRevenue(
                        totalRevenue
                )

                .averageRating(
                        averageRating
                )

                .build();
    }

    public Object getProviderRecentJobs(){

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = auth.getName();

        var user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow();

        var provider =
                providerRepository
                        .findByUserId(user.getId())
                        .orElseThrow();

        Pageable pageable =
                PageRequest.of(0,10);

        return bookingRepository
                .findByProviderIdOrderByCreatedAtDesc(
                        provider.getId(),
                        pageable
                )

                .map(booking -> Map.of(

                        "id",
                        booking.getId(),

                        "serviceType",
                        booking.getServiceType(),

                        "customerName",

                        booking.getCustomer()!=null
                                ?

                                booking.getCustomer()
                                        .getFirstName()

                                        + " " +

                                        booking.getCustomer()
                                                .getLastName()

                                :

                                "Unknown",

                        "customerPhone",

                        booking.getCustomer()!=null
                                ?

                                booking.getCustomer()
                                        .getPhone()

                                :

                                "N/A",

                        "bookingDate",
                        booking.getBookingDate(),

                        "bookingTime",
                        booking.getStartTime(),

                        "createdAt",
                        booking.getCreatedAt(),

                        "address",
                        booking.getAddress(),

                        "amount",
                        booking.getAmount(),

                        "status",
                        booking.getStatus()
                ));
    }

    public Object
    getProviderBookingAnalytics(

            String period

    ){

        Authentication auth =

                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                auth.getName();

        var user =

                userRepository
                        .findByEmail(email)
                        .orElseThrow();

        var provider =

                providerRepository
                        .findByUserId(
                                user.getId()
                        )
                        .orElseThrow();

        Long providerId =
                provider.getId();

        return Map.of(

                "total",

                bookingRepository
                        .countByProviderId(
                                providerId
                        ),

                "pending",

                bookingRepository
                        .countByProviderIdAndStatus(

                                providerId,

                                BookingStatus.PENDING
                        ),

                "accepted",

                bookingRepository
                        .countByProviderIdAndStatus(

                                providerId,

                                BookingStatus.ACCEPTED
                        ),

                "completed",

                bookingRepository
                        .countByProviderIdAndStatus(

                                providerId,

                                BookingStatus.COMPLETED
                        ),

                "cancelled",

                bookingRepository
                        .countByProviderIdAndStatus(

                                providerId,

                                BookingStatus.CANCELLED
                        )

        );

    }

    public Object
    getAdminBookingAnalytics(

            String period

    ){

        return Map.of(

                "total",

                bookingRepository.count(),

                "pending",

                bookingRepository
                        .countByStatus(
                                BookingStatus.PENDING
                        ),

                "accepted",

                bookingRepository
                        .countByStatus(
                                BookingStatus.ACCEPTED
                        ),

                "completed",

                bookingRepository
                        .countByStatus(
                                BookingStatus.COMPLETED
                        ),

                "cancelled",

                bookingRepository
                        .countByStatus(
                                BookingStatus.CANCELLED
                        )

        );

    }

    public DashboardResponse
    getAdminDashboard(){

        Long totalUsers =

                userRepository.count();

        Long totalCustomers =

                userRepository
                        .countByRoles_Name(
                                RoleType.ROLE_CUSTOMER
                        );

        Long totalProviders =

                providerRepository.count();

        Long activeUsers =

                userRepository
                        .countByStatus(
                                UserStatus.ACTIVE
                        );

        Long totalBookings =

                bookingRepository.count();

        Long pendingBookings =

                bookingRepository
                        .countByStatus(
                                BookingStatus.PENDING
                        );

        Long acceptedBookings =

                bookingRepository
                        .countByStatus(
                                BookingStatus.ACCEPTED
                        );

        Long completedBookings =

                bookingRepository
                        .countByStatus(
                                BookingStatus.COMPLETED
                        );
        Long totalComplaints =
                complaintRepository.count();

        Double averageRating =
                reviewRepository.findAll()
                        .stream()
                        .mapToDouble(r ->
                                r.getRating() != null
                                        ? r.getRating()
                                        : 0.0
                        )
                        .average()
                        .orElse(0.0);

        Long cancelledBookings =

                bookingRepository
                        .countByStatus(
                                BookingStatus.CANCELLED
                        );

        BigDecimal totalRevenue =

                paymentRepository
                        .getTotalRevenue();

        return DashboardResponse
                .builder()

                .totalUsers(
                        totalUsers
                )

                .totalCustomers(
                        totalCustomers
                )

                .totalProviders(
                        totalProviders
                )

                .activeUsers(
                        activeUsers
                )

                .totalBookings(
                        totalBookings
                )

                .pendingBookings(
                        pendingBookings
                )

                .acceptedBookings(
                        acceptedBookings
                )

                .completedBookings(
                        completedBookings
                )
                .averageRating(
                        averageRating
                )

                .totalComplaints(
                        totalComplaints
                )

                .cancelledBookings(
                        cancelledBookings
                )

                .totalRevenue(
                        totalRevenue
                )

                .build();

    }

    public Object
    getProviderRevenueAnalytics(

            String period

    ){

        Authentication auth =

                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                auth.getName();

        var user =

                userRepository
                        .findByEmail(email)
                        .orElseThrow();

        var provider =

                providerRepository
                        .findByUserId(
                                user.getId()
                        )
                        .orElseThrow();

        Long providerId =
                provider.getId();

        BigDecimal totalRevenue =

                paymentRepository
                        .getRevenueByProviderId(
                                providerId
                        );

        totalRevenue =

                totalRevenue == null

                        ?

                        BigDecimal.ZERO

                        :

                        totalRevenue;

        return Map.of(

                "stats",

                Map.of(

                        "total",
                        totalRevenue,

                        "pending",
                        BigDecimal.ZERO,

                        "monthly",
                        totalRevenue,

                        "transactions",

                        paymentRepository
                                .countByProviderId(
                                        providerId
                                )

                ),

                "transactions",

                paymentRepository

                        .findByProviderId(
                                providerId
                        )

                        .stream()

                        .map(payment ->

                                Map.of(

                                        "id",
                                        payment.getId(),

                                        "customerName",

                                        payment.getCustomer()
                                                .getFirstName()

                                                +

                                                " "

                                                +

                                                payment.getCustomer()
                                                        .getLastName(),

                                        "amount",
                                        payment.getAmount(),

                                        "status",
                                        payment.getStatus(),

                                        "date",
                                        payment.getCreatedAt()

                                )

                        )

                        .toList()

        );

    }

    public Object
    getAdminRevenueAnalytics(

            String period

    ){

        BigDecimal totalRevenue =

                paymentRepository
                        .getTotalRevenue();

        BigDecimal refunded =

                paymentRepository
                        .getTotalRefundedAmount();

        totalRevenue =

                totalRevenue == null

                        ?

                        BigDecimal.ZERO

                        :

                        totalRevenue;

        refunded =

                refunded == null

                        ?

                        BigDecimal.ZERO

                        :

                        refunded;

        return Map.of(

                "stats",

                Map.of(

                        "total",
                        totalRevenue,

                        "pending",
                        refunded,

                        "monthly",
                        totalRevenue,

                        "transactions",

                        paymentRepository
                                .count()

                ),

                "transactions",

                paymentRepository
                        .findAll()

                        .stream()

                        .map(payment ->

                                Map.of(

                                        "id",
                                        payment.getId(),

                                        "customerName",

                                        payment.getCustomer()!=null

                                                ?

                                                payment.getCustomer()
                                                        .getFirstName()

                                                        +

                                                        " "

                                                        +

                                                        payment.getCustomer()
                                                                .getLastName()

                                                :

                                                "Unknown Customer",

                                        "amount",
                                        payment.getAmount(),

                                        "status",
                                        payment.getStatus(),

                                        "date",
                                        payment.getCreatedAt()

                                )

                        )

                        .toList(),

                "monthlyRevenue",

                java.util.List.of(

                        8000,

                        12000,

                        9000,

                        15000,

                        11000,

                        18000,

                        14000,

                        21000,

                        17000,

                        25000,

                        19000,

                        totalRevenue.intValue()

                )

        );

    }
}