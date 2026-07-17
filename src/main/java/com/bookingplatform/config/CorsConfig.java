package com.bookingplatform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.web.cors.*;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Bean
    public CorsFilter corsFilter() {

        CorsConfiguration config =
                new CorsConfiguration();

        config.setAllowCredentials(true);

        config.setAllowedOrigins(
                List.of(
                        frontendUrl,
                        "http://localhost:3000",
                        "http://localhost:5173",
                        "https://bookify-nine-virid.vercel.app"
                    
                )
        );

        config.setAllowedHeaders(
                List.of("*")
        );

        config.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );

        config.setExposedHeaders(
                List.of(
                        "Authorization"
                )
        );

        UrlBasedCorsConfigurationSource
                source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                config
        );

        return new CorsFilter(source);
    }

}
