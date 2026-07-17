package com.bookingplatform.repository;

import com.bookingplatform.entity.WorkProof;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkProofRepository extends JpaRepository<WorkProof, Long> {

    /**
     * Find work proof by booking ID
     */
    Optional<WorkProof> findByBookingId(Long bookingId);

    /**
     * Check whether a booking already has a work proof
     */
    boolean existsByBookingId(Long bookingId);

    /**
     * Delete proof when a booking is deleted (optional)
     */
    void deleteByBookingId(Long bookingId);

}