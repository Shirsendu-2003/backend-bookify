package com.bookingplatform.repository;

import com.bookingplatform.entity.ProviderUpdateRequest;
import com.bookingplatform.enums.UpdateRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProviderUpdateRequestRepository
        extends JpaRepository<ProviderUpdateRequest, Long> {

    @Query("""
SELECT r
FROM ProviderUpdateRequest r
JOIN FETCH r.provider
LEFT JOIN FETCH r.reviewedBy
WHERE r.status=:status
""")
    List<ProviderUpdateRequest> findByStatus(
            @Param("status") UpdateRequestStatus status
    );

    List<ProviderUpdateRequest> findByProviderIdOrderByRequestedAtDesc(Long providerId);

    Optional<ProviderUpdateRequest> findFirstByProviderIdAndStatus(
            Long providerId,
            UpdateRequestStatus status
    );

    boolean existsByProviderIdAndStatus(
            Long providerId,
            UpdateRequestStatus status
    );

}