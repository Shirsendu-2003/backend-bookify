package com.bookingplatform.entity;

import com.bookingplatform.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({
        "users",
        "hibernateLazyInitializer",
        "handler"
})
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "phone")
        }
)
public class User extends BaseEntity {

    @NotBlank
    @Column(nullable = false, length = 100)
    private String firstName;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String lastName;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Column(length = 255)
    private String profileImage;


    @Column(length = 500)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 100)
    private String country;

    @Column(length = 20)
    private String zipCode;

    @Builder.Default
    @Column(nullable = false)
    private Boolean emailVerified = false;

    @Builder.Default
    @Column(nullable = false)
    private Boolean phoneVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    /*
     * ==========================
     * USER ↔ ROLE
     * ==========================
     */

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",

            joinColumns = @JoinColumn(
                    name = "user_id"
            ),

            inverseJoinColumns = @JoinColumn(
                    name = "role_id"
            )
    )
    @JsonIgnoreProperties({
            "users"
    })
    private Set<Role> roles = new HashSet<>();

    /*
     * ==========================
     * USER ↔ PROVIDER PROFILE
     * One User → One Provider Profile
     * ==========================
     */

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private Provider provider;

    /*
     * ==========================
     * CUSTOMER BOOKINGS
     * ==========================
     */

    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<Booking> bookings;

    /*
     * ==========================
     * CUSTOMER REVIEWS
     * ==========================
     */

    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<Review> reviews;

    /*
     * ==========================
     * CUSTOMER COMPLAINTS
     * ==========================
     */

    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<Complaint> complaints =
            new ArrayList<>();
    /*
     * ==========================
     * USER NOTIFICATIONS
     * ==========================
     */

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<Notification> notifications;

    private String resetToken;

    private LocalDateTime
            resetTokenExpiry;

}