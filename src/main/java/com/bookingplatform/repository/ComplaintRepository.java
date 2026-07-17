package com.bookingplatform.repository;

import com.bookingplatform.entity.Complaint;
import com.bookingplatform.enums.ComplaintStatus;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRepository
        extends JpaRepository<Complaint, Long> {


    Page<Complaint>
    findByCustomer_Id(
            Long customerId,
            Pageable pageable
    );

    Page<Complaint>
    findByProvider_Id(
            Long providerId,
            Pageable pageable
    );



    Page<Complaint> findByBookingId(
            Long bookingId,
            Pageable pageable
    );

    /*
     * ==========================
     * STATUS FILTER
     * ==========================
     */

    Page<Complaint> findByStatus(
            ComplaintStatus status,
            Pageable pageable
    );

    /*
     * ==========================
     * DASHBOARD COUNTS
     * ==========================
     */

    long countByStatus(
            ComplaintStatus status
    );
    Long countByCustomerId(
            Long customerId
    );

    Long countByCustomerIdAndStatus(
            Long customerId,
            ComplaintStatus status
    );

}