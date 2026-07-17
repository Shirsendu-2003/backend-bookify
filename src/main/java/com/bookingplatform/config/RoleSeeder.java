package com.bookingplatform.config;

import com.bookingplatform.entity.Role;
import com.bookingplatform.enums.RoleType;
import com.bookingplatform.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleSeeder {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void seedRoles() {

        for (RoleType roleType
                : RoleType.values()) {

            if (

                    roleRepository
                            .findByName(roleType)
                            .isEmpty()

            ) {

                roleRepository.save(

                        Role.builder()

                                .name(
                                        roleType
                                )

                                .description(
                                        roleType.name()
                                )

                                .build()
                );
            }
        }
    }
}