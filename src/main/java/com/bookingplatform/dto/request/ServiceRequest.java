package com.bookingplatform.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRequest {

    private String title;

    private String description;

    private Double price;
}