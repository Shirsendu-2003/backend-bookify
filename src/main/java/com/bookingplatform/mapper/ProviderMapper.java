package com.bookingplatform.mapper;

import com.bookingplatform.dto.provider.*;
import com.bookingplatform.entity.*;
import com.bookingplatform.enums.ProviderType;

import java.util.List;
import java.util.stream.Collectors;

public class ProviderMapper {

    private ProviderMapper(){}

    /*
     * ==========================
     * DTO → ENTITY
     * ==========================
     */

    public static Provider toEntity(
            ProviderRequest request
    ){

        if(request == null){
            return null;
        }

        Provider provider =
                new Provider();

        provider.setName(
                request.getName()
        );

        provider.setProviderType(

                request.getProviderType()
                        != null

                        ? request
                        .getProviderType()
                        .name()

                        : null
        );

        provider.setServiceType(
                request.getServiceType()
        );

        provider.setBio(
                request.getBio()
        );

        provider.setExperience(
                request.getExperience()
        );

        provider.setLocation(
                request.getLocation()
        );

        provider.setSkills(
                request.getSkills()
        );

        provider.setAvatar(
                request.getAvatar()
        );

        provider.setHourlyRate(
                request.getHourlyRate()
        );

        provider.setAvailable(
                request.getAvailable()
        );

        provider.setCity(
                request.getCity()
        );

        provider.setState(
                request.getState()
        );

        provider.setCountry(
                request.getCountry()
        );

        provider.setZipCode(
                request.getZipCode()
        );

        provider.setProfileImageUrl(
                request.getProfileImageUrl()
        );

        provider.setGovernmentIdUrl(
                request.getGovernmentIdUrl()
        );

        provider.setCertificateUrl(
                request.getCertificateUrl()
        );

        return provider;
    }

    /*
     * ==========================
     * ENTITY → DTO
     * ==========================
     */

    public static ProviderResponse toResponse(
            Provider provider
    ){

        if(provider == null){
            return null;
        }

        List<ProviderResponse
                .AvailabilityDto>
                availabilityDtos = null;

        if(provider.getAvailabilities()
                != null){

            availabilityDtos =

                    provider
                            .getAvailabilities()

                            .stream()

                            .map(a ->

                                    ProviderResponse
                                            .AvailabilityDto
                                            .builder()

                                            .id(
                                                    a.getId()
                                            )

                                            .dayOfWeek(
                                                    a.getDayOfWeek()
                                            )

                                            .startTime(
                                                    a.getStartTime()
                                            )

                                            .endTime(
                                                    a.getEndTime()
                                            )

                                            .available(
                                                    a.getAvailable()
                                            )

                                            .breakStart(
                                                    a.getBreakStart()
                                            )

                                            .breakEnd(
                                                    a.getBreakEnd()
                                            )

                                            .notes(
                                                    a.getNotes()
                                            )

                                            .build()

                            )

                            .collect(
                                    Collectors.toList()
                            );
        }

        User user =
                provider.getUser();

        return ProviderResponse
                .builder()

                .id(
                        provider.getId()
                )

                .name(
                        provider.getName()
                )

                .providerType(
                        ProviderType.valueOf(provider.getProviderType())

                )

                .location(
                        provider.getLocation()
                )

                .skills(
                        provider.getSkills()
                )

                .avatar(
                        provider.getAvatar()
                )

                .userId(

                        user != null

                                ? user.getId()

                                : null
                )

                .firstName(

                        user != null

                                ? user.getFirstName()

                                : null
                )

                .lastName(

                        user != null

                                ? user.getLastName()

                                : null
                )

                .email(

                        user != null

                                ? user.getEmail()

                                : null
                )

                .phone(

                        user != null

                                ? user.getPhone()

                                : null
                )

                .serviceType(
                        provider.getServiceType()
                )

                .bio(
                        provider.getBio()
                )

                .experience(
                        provider.getExperience()
                )

                .hourlyRate(
                        provider.getHourlyRate()
                )

                .averageRating(
                        provider.getAverageRating()
                )

                .totalReviews(
                        provider.getTotalReviews()
                )

                .available(
                        provider.getAvailable()
                )

                .city(
                        provider.getCity()
                )

                .state(
                        provider.getState()
                )

                .country(
                        provider.getCountry()
                )

                .zipCode(
                        provider.getZipCode()
                )

                .profileImageUrl(
                        provider.getProfileImageUrl()
                )

                .governmentIdUrl(
                        provider.getGovernmentIdUrl()
                )

                .certificateUrl(
                        provider.getCertificateUrl()
                )

                .availabilities(
                        availabilityDtos
                )

                .createdAt(
                        provider.getCreatedAt()
                )

                .updatedAt(
                        provider.getUpdatedAt()
                )

                .build();
    }

}