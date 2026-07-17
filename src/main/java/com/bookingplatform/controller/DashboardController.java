package com.bookingplatform.controller;

import com.bookingplatform.dto.common.ApiResponse;
import com.bookingplatform.dto.dashboard.DashboardResponse;
import com.bookingplatform.dto.dashboard.ToggleStatusRequest;
import com.bookingplatform.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import com.bookingplatform.dto.dashboard.SettingsRequest;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /*
     * ==========================
     * GET ADMIN DASHBOARD
     * ==========================
     */

    @GetMapping
    public ApiResponse<DashboardResponse>
    getDashboard() {

        return ApiResponse
                .<DashboardResponse>builder()

                .success(true)

                .message(
                        "Dashboard data fetched successfully."
                )

                .data(

                        dashboardService
                                .getDashboard()

                )

                .build();
    }
    @GetMapping("/activities")
    public ResponseEntity<?> getActivities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                dashboardService.getActivities(page, size)
        );
    }
    @GetMapping("/revenue")
    public ResponseEntity<?> getRevenueAnalytics(

            @RequestParam(
                    defaultValue = "monthly"
            )
            String period

    ) {

        return ResponseEntity.ok(

                dashboardService
                        .getRevenueAnalytics(period)

        );
    }

    /* ==========================
       BOOKING ANALYTICS
    ========================== */

    @GetMapping("/bookings")
    public ResponseEntity<?> getBookingAnalytics(

            @RequestParam(
                    defaultValue = "monthly"
            )
            String period

    ) {

        return ResponseEntity.ok(

                dashboardService
                        .getBookingAnalytics(period)

        );
    }

    /* ==========================
       DASHBOARD STATS
    ========================== */

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {

        return ResponseEntity.ok(

                dashboardService
                        .getStats()

        );
    }
    @GetMapping("/users")
    public ResponseEntity<?> getUsers(

            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "10"
            )
            int size

    ) {

        return ResponseEntity.ok(

                dashboardService
                        .getUsers(
                                page,
                                size
                        )

        );
    }

    /* ==========================
       REPORTS
    ========================== */

    @GetMapping("/reports")
    public ResponseEntity<?> getReports() {

        return ResponseEntity.ok(

                dashboardService
                        .getReports()

        );
    }

    /* ==========================
       COMPLAINTS
    ========================== */

    @GetMapping("/complaints")
    public ResponseEntity<?> getComplaints(){

        return ResponseEntity.ok(

                dashboardService
                        .getComplaints()

        );
    }

    @PatchMapping(
            "/complaints/{id}/resolve"
    )
    public ResponseEntity<?> resolveComplaint(

            @PathVariable
            Long id

    ){

        return ResponseEntity.ok(

                dashboardService
                        .resolveComplaint(id)

        );
    }

    @PatchMapping(
            "/complaints/{id}/close"
    )
    public ResponseEntity<?> closeComplaint(

            @PathVariable
            Long id

    ){

        return ResponseEntity.ok(

                dashboardService
                        .closeComplaint(id)

        );
    }

    /* ==========================
       TOP PROVIDERS
    ========================== */

    @GetMapping("/top-providers")
    public ResponseEntity<?> getTopProviders() {

        return ResponseEntity.ok(

                dashboardService
                        .getTopProviders()

        );
    }

    /* ==========================
       RECENT BOOKINGS
    ========================== */

    @GetMapping("/recent-bookings")
    public ResponseEntity<?> getRecentBookings() {

        return ResponseEntity.ok(

                dashboardService
                        .getRecentBookings()



        );
    }

    /* ==========================
       TOGGLE USER STATUS
    ========================== */

    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<?> toggleUserStatus(

            @PathVariable
            Long userId,

            @RequestBody
            ToggleStatusRequest request

    ) {

        return ResponseEntity.ok(

                dashboardService
                        .toggleUserStatus(
                                userId,
                                request.getEnabled()
                        )

        );
    }

    @PostMapping("/reports/export")
    public ResponseEntity<byte[]>
    exportReport(){

        String content =

                dashboardService
                        .exportReport();

        return ResponseEntity.ok()

                .header(

                        HttpHeaders
                                .CONTENT_DISPOSITION,

                        "attachment; filename=report.csv"

                )

                .contentType(
                        MediaType.TEXT_PLAIN
                )

                .body(
                        content.getBytes()
                );
    }

    @GetMapping("/settings")
    public ResponseEntity<?> getSettings(){

        return ResponseEntity.ok(

                dashboardService
                        .getSettings()

        );
    }

    @PostMapping("/settings")
    public ResponseEntity<?> saveSettings(

            @RequestBody
            SettingsRequest request

    ){

        return ResponseEntity.ok(

                dashboardService
                        .saveSettings(request)

        );
    }

    @GetMapping(
            "/provider"
    )
    public ResponseEntity<?>

    getProviderDashboard(){

        return ResponseEntity.ok(

                dashboardService
                        .getProviderDashboard()
        );
    }

    @GetMapping(
            "/provider/recent-jobs"
    )
    public ResponseEntity<?>

    getProviderRecentJobs(){

        return ResponseEntity.ok(

                dashboardService
                        .getProviderRecentJobs()
        );
    }

    @GetMapping(
            "/provider/bookings"
    )
    public ResponseEntity<?>

    getProviderBookingAnalytics(

            @RequestParam(
                    defaultValue="monthly"
            )
            String period

    ){

        return ResponseEntity.ok(

                dashboardService
                        .getProviderBookingAnalytics(
                                period
                        )

        );

    }

    @GetMapping(
            "/admin/bookings"
    )
    public ResponseEntity<?>

    getAdminBookingAnalytics(

            @RequestParam(
                    defaultValue="monthly"
            )
            String period

    ){

        return ResponseEntity.ok(

                dashboardService
                        .getAdminBookingAnalytics(
                                period
                        )

        );

    }

    @GetMapping(
            "/admin"
    )
    public ResponseEntity<?>

    getAdminDashboard(){

        return ResponseEntity.ok(

                dashboardService
                        .getAdminDashboard()

        );

    }

    @GetMapping(
            "/provider/revenue"
    )
    public ResponseEntity<?>

    getProviderRevenueAnalytics(

            @RequestParam(
                    defaultValue="monthly"
            )
            String period

    ){

        return ResponseEntity.ok(

                dashboardService
                        .getProviderRevenueAnalytics(
                                period
                        )

        );

    }

    @GetMapping(
            "/admin/revenue"
    )
    public ResponseEntity<?>

    getAdminRevenueAnalytics(

            @RequestParam(
                    defaultValue="monthly"
            )
            String period

    ){

        return ResponseEntity.ok(

                dashboardService
                        .getAdminRevenueAnalytics(
                                period
                        )

        );

    }



}