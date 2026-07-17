package com.bookingplatform.dto.provider;

import com.bookingplatform.entity.Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProviderCandidate {

    private Provider provider;

    private Double score;
}
