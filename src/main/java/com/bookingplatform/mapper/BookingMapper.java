package com.bookingplatform.mapper;

import com.bookingplatform.dto.booking.*;
import com.bookingplatform.entity.Booking;

public class BookingMapper {

    private BookingMapper() {}

    public static Booking toEntity(
            BookingRequest request
    ) {

        if (request == null) {
            return null;
        }

        Booking booking =
                new Booking();

        booking.setServiceType(
                request.getServiceType()
        );

        booking.setDescription(
                request.getDescription()
        );

        booking.setAddress(
                request.getAddress()
        );

        booking.setCity(
                request.getCity()
        );

        booking.setState(
                request.getState()
        );

        booking.setCountry(
                request.getCountry()
        );

        booking.setZipCode(
                request.getZipCode()
        );

        booking.setBookingDate(
                request.getBookingDate()
        );

        booking.setStartTime(
                request.getStartTime()
        );

        booking.setEndTime(
                request.getEndTime()
        );

        booking.setAmount(
                request.getAmount()
        );

        return booking;
    }

    public static BookingResponse toResponse(
            Booking booking
    ) {

        if (booking == null) {
            return null;
        }

        return BookingResponse
                .builder()

                .id(booking.getId())

                .customerId(
                        booking.getCustomer().getId()
                )

                .customerName(
                        booking.getCustomer().getFirstName()
                                + " "
                                + booking.getCustomer().getLastName()
                )

                .providerId(
                        booking.getProvider().getId()
                )

                .providerName(
                        booking.getProvider()
                                .getUser()
                                .getFirstName()
                                + " "
                                +
                                booking.getProvider()
                                        .getUser()
                                        .getLastName()
                )

                .serviceType(
                        booking.getServiceType()
                )

                .description(
                        booking.getDescription()
                )

                .address(
                        booking.getAddress()
                )

                .city(
                        booking.getCity()
                )

                .state(
                        booking.getState()
                )

                .country(
                        booking.getCountry()
                )

                .zipCode(
                        booking.getZipCode()
                )

                .bookingDate(
                        booking.getBookingDate()
                )

                .startTime(
                        booking.getStartTime()
                )

                .endTime(
                        booking.getEndTime()
                )

                .amount(
                        booking.getAmount()
                )

                .status(
                        booking.getStatus()
                )

                .paymentStatus(
                        booking.getPaymentStatus()
                )


                .cancellationReason(
                        booking.getCancellationReason()
                )

                .adminNotes(
                        booking.getAdminNotes()
                )

                .createdAt(
                        booking.getCreatedAt()
                )

                .updatedAt(
                        booking.getUpdatedAt()
                )

                .build();
    }

}