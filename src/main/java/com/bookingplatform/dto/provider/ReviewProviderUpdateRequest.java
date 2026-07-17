package com.bookingplatform.dto.provider;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewProviderUpdateRequest {

    @NotBlank
    private String remarks;

}