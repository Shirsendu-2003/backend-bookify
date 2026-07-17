package com.bookingplatform.service;

import com.bookingplatform.dto.provider.ProviderAdminResponse;
import com.bookingplatform.entity.Provider;
import com.bookingplatform.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderService {

    private final ProviderRepository providerRepository;


    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }
}
