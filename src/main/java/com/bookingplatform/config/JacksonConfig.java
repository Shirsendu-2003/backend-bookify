package com.bookingplatform.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.*;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper mapper =
                new ObjectMapper();

        /*
         * Java 8 Date/Time Support
         */

        mapper.registerModule(
                new JavaTimeModule()
        );

        /*
         * Disable timestamps
         */

        mapper.disable(
                SerializationFeature
                        .WRITE_DATES_AS_TIMESTAMPS
        );

        /*
         * Ignore null values
         */

        mapper.setSerializationInclusion(
                JsonInclude.Include.NON_NULL
        );

        /*
         * Ignore unknown properties
         */

        mapper.configure(
                DeserializationFeature
                        .FAIL_ON_UNKNOWN_PROPERTIES,
                false
        );

        return mapper;
    }

}