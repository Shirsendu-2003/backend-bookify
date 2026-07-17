package com.bookingplatform.repository;

import com.bookingplatform.entity.ProviderService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProviderServiceRepository
        extends JpaRepository<
        ProviderService,
        Long> {

    List<ProviderService>
    findByProviderId(Long providerId);
}