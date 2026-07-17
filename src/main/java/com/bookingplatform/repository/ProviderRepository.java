package com.bookingplatform.repository;

import com.bookingplatform.entity.Provider;
import com.bookingplatform.entity.User;
import com.bookingplatform.enums.ProviderType;
import com.bookingplatform.enums.VerificationStatus;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

    /*
     * ==========================
     * BASIC
     * ==========================
     */

    Optional<Provider> findByUserId(Long userId);
    Optional<Provider> findByUser(
            User user
    );

    boolean existsByUserId(Long userId);

    List<Provider> findByProviderType(
            ProviderType providerType
    );

    List<Provider> findByLocationContainingIgnoreCase(
            String location
    );

    List<Provider>
    findByNameContainingIgnoreCase(
            String keyword
    );


    /*
     * ==========================
     * FILTERS
     * ==========================
     */

    Page<Provider> findByAvailable(
            Boolean available,
            Pageable pageable
    );

    Page<Provider> findByServiceTypeContainingIgnoreCase(
            String serviceType,
            Pageable pageable
    );

    /*
     * ==========================
     * SEARCH
     * ==========================
     */

    @Query("""
        SELECT p
        FROM Provider p
        WHERE
            LOWER(p.serviceType)
            LIKE LOWER(CONCAT('%',:keyword,'%'))

            OR

            LOWER(p.city)
            LIKE LOWER(CONCAT('%',:keyword,'%'))

            OR

            LOWER(p.state)
            LIKE LOWER(CONCAT('%',:keyword,'%'))
    """)
    Page<Provider> searchProviders(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    /*
     * ==========================
     * TOP PROVIDERS
     * ==========================
     */

    List<Provider> findTop10ByOrderByAverageRatingDesc();

    /*
     * ==========================
     * DASHBOARD
     * ==========================
     */

    long countByAvailable(Boolean available);


    @Query("""
SELECT p
FROM Provider p
WHERE p.verificationStatus =
      com.bookingplatform.enums.VerificationStatus.APPROVED
ORDER BY
CASE
WHEN LOWER(p.location)
LIKE LOWER(CONCAT('%', :location, '%'))
THEN 0
WHEN LOWER(p.city)
LIKE LOWER(CONCAT('%', :city, '%'))
THEN 1
ELSE 2
END,
p.averageRating DESC
""")
    List<Provider> findNearbyProviders(
            @Param("location") String location,
            @Param("city") String city
    );

    List<Provider> findByVerificationStatus(
            VerificationStatus verificationStatus
    );

    List<Provider>
    findByNameContainingIgnoreCaseAndVerificationStatus(
            String name,
            VerificationStatus status
    );

    List<Provider> findByProviderType(
            String providerType
    );

    @Query("""
SELECT p
FROM Provider p
WHERE LOWER(p.city)=LOWER(:city)
AND p.verificationStatus =
    com.bookingplatform.enums.VerificationStatus.APPROVED
ORDER BY p.averageRating DESC
""")
    List<Provider> findProvidersNearCity(
            @Param("city") String city
    );


    @Query(value = """
SELECT *
FROM providers p
WHERE
      p.verification_status = 'APPROVED'
  AND p.available = true
  AND p.online = true
  AND p.provider_type = :serviceType
  AND (
      6371000 *
      acos(
          cos(radians(:lat))
          *
          cos(radians(p.latitude))
          *
          cos(
              radians(p.longitude)
              -
              radians(:lng)
          )
          +
          sin(radians(:lat))
          *
          sin(radians(p.latitude))
      )
  ) <= :radius
ORDER BY average_rating DESC
""", nativeQuery = true)
    List<Provider> findNearbyProvidersByRadius(
            @Param("lat") Double lat,
            @Param("lng") Double lng,
            @Param("radius") Double radius,
            @Param("serviceType") String serviceType
    );

    Optional<Provider>
    findByUserEmail(String email);





}