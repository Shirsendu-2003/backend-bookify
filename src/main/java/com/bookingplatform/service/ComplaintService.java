package com.bookingplatform.service;

import com.bookingplatform.dto.complaint.*;
import com.bookingplatform.entity.*;
import com.bookingplatform.enums.ComplaintStatus;
import com.bookingplatform.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository
            complaintRepository;

    private final UserRepository
            userRepository;

    private final BookingRepository
            bookingRepository;

    public ComplaintResponse createComplaint(

            ComplaintRequest request,
            String email

    ){

        User customer =

                userRepository
                        .findByEmail(email)
                        .orElseThrow();

        Booking booking = null;

        Provider provider = null;

        if(request.getBookingId()!=null){

            booking =

                    bookingRepository
                            .findById(
                                    request.getBookingId()
                            )
                            .orElseThrow();

            provider =
                    booking.getProvider();
        }

        Complaint complaint =

                Complaint.builder()

                        .customer(customer)

                        .provider(provider)

                        .booking(booking)

                        .subject(
                                request.getSubject()
                        )

                        .description(
                                request.getDescription()
                        )

                        .priority(
                                request.getPriority()
                        )

                        .status(
                                ComplaintStatus.OPEN
                        )

                        .build();

        Complaint saved =

                complaintRepository
                        .save(complaint);

        return ComplaintResponse
                .builder()

                .id(saved.getId())

                .bookingId(

                        booking!=null
                                ? booking.getId()
                                : null

                )

                .customerId(
                        customer.getId()
                )

                .customerName(

                        customer.getFirstName()
                                + " "
                                + customer.getLastName()

                )

                .providerId(

                        provider!=null
                                ? provider.getId()
                                : null

                )

                .providerName(

                        provider!=null
                                ? provider.getName()
                                : null

                )

                .subject(
                        saved.getSubject()
                )

                .description(
                        saved.getDescription()
                )

                .priority(
                        saved.getPriority()
                                .name()
                )

                .status(
                        saved.getStatus()
                )

                .createdAt(
                        saved.getCreatedAt()
                )

                .updatedAt(
                        saved.getUpdatedAt()
                )

                .build();
    }
}