package com.bookingplatform.dto.booking;

import com.bookingplatform.enums.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingStatusRequest {

    @NotNull
    private BookingStatus status;

    private String cancellationReason;

    private String adminNotes;

}