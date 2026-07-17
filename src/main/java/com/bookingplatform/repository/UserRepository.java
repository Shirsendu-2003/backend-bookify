package com.bookingplatform.repository;

import com.bookingplatform.entity.User;
import com.bookingplatform.enums.RoleType;
import com.bookingplatform.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /*
     * ==========================
     * AUTH
     * ==========================
     */

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<User>
    findByResetToken(
            String resetToken
    );

    /*
     * ==========================
     * USER STATUS
     * ==========================
     */

    Page<User> findByStatus(
            UserStatus status,
            Pageable pageable
    );

    /*
     * ==========================
     * SEARCH
     * ==========================
     */

    @Query("""
        SELECT u
        FROM User u
        WHERE
            LOWER(u.firstName) LIKE LOWER(CONCAT('%',:keyword,'%'))
            OR
            LOWER(u.lastName) LIKE LOWER(CONCAT('%',:keyword,'%'))
            OR
            LOWER(u.email) LIKE LOWER(CONCAT('%',:keyword,'%'))
    """)
    Page<User> searchUsers(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    /*
     * ==========================
     * DASHBOARD
     * ==========================
     */

    long countByStatus(UserStatus status);
    long countByRoles_Name(
            RoleType roleType
    );

}