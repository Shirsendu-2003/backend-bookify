package com.bookingplatform.config;

import com.bookingplatform.entity.Role;
import com.bookingplatform.entity.User;
import com.bookingplatform.enums.RoleType;
import com.bookingplatform.enums.UserStatus;
import com.bookingplatform.repository.RoleRepository;
import com.bookingplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoaderService implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        loadRoles();

        loadAdmin();
    }

    private void loadRoles() {

        createRoleIfNotExists(
                RoleType.ROLE_ADMIN,
                "System Administrator"
        );

        createRoleIfNotExists(
                RoleType.ROLE_CUSTOMER,
                "Platform Customer"
        );

        createRoleIfNotExists(
                RoleType.ROLE_PROVIDER,
                "Service Provider"
        );

        log.info("Roles loaded successfully.");
    }

    private void createRoleIfNotExists(
            RoleType roleType,
            String description
    ) {

        if (!roleRepository.existsByName(roleType)) {

            Role role = Role.builder()
                    .name(roleType)
                    .description(description)
                    .build();

            roleRepository.save(role);

            log.info("Created role: {}", roleType);
        }
    }

    private void loadAdmin() {

        if (userRepository.existsByEmail("admin@booking.com")) {

            log.info("Default admin already exists.");

            return;
        }

        Role adminRole = roleRepository
                .findByName(RoleType.ROLE_ADMIN)
                .orElseThrow(
                        () -> new RuntimeException(
                                "ROLE_ADMIN not found"
                        )
                );

        User admin = User.builder()
                .firstName("Super")
                .lastName("Admin")
                .email("admin@booking.com")
                .password(
                        passwordEncoder.encode(
                                "Admin@123"
                        )
                )
                .phone("9999999999")
                .status(UserStatus.ACTIVE)
                .emailVerified(true)
                .phoneVerified(true)
                .build();

        admin.getRoles().add(adminRole);

        userRepository.save(admin);

        log.info("Default admin user created successfully.");
    }
}