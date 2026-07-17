package com.bookingplatform.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.
        AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.
        EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.
        SessionCreationPolicy;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.
        UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter
            jwtAuthenticationFilter;

    private final AuthEntryPointJwt
            authEntryPointJwt;

    @Bean
    public SecurityFilterChain
    securityFilterChain(
            HttpSecurity http
    )

            throws Exception {

        http

                /*
                 * CORS + CSRF
                 */

                .cors(cors -> {})

                .csrf(csrf -> csrf.disable())

                /*
                 * Exception Handling
                 */

                .exceptionHandling(ex ->

                        ex.authenticationEntryPoint(
                                authEntryPointJwt
                        )
                )

                /*
                 * Stateless JWT
                 */

                .sessionManagement(session ->

                        session.sessionCreationPolicy(

                                SessionCreationPolicy
                                        .STATELESS

                        )
                )

                /*
                 * Authorization Rules
                 */

                .authorizeHttpRequests(auth -> auth

                        /*
                         * PUBLIC
                         */

                        .requestMatchers(
                                "/api/auth/**",
                                "/oauth2/**",
                                "/login/**",
                                "/api/reports/**",
                                "/uploads/**",
                                "/api/report/**",
                                "/api/invoices/**",
                                "/ws/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        )
                        .permitAll()

                        /*
                         * ADMIN
                         */

                        .requestMatchers(
                                "/api/admin/**"
                        )
                        .hasRole("ADMIN")

                        /*
                         * PROVIDER
                         */

                        .requestMatchers(
                                "/api/providers/**"
                        )
                        .hasAnyRole("PROVIDER", "CUSTOMER","ADMIN")

                        /*
                         * USER
                         */

                        .requestMatchers(
                                "/api/user/**"
                        )
                        .hasAnyRole(
                                "CUSTOMER",
                                "ADMIN"
                        )

                        /*
                         * BOOKINGS
                         */

                        .requestMatchers(
                                "/api/bookings/**"
                        )
                        .hasAnyRole(
                                "CUSTOMER",
                                "PROVIDER",
                                "ADMIN"
                        )

                        /*
                         * PAYMENTS
                         */

                        .requestMatchers(
                                "/api/payment/**"
                        )
                        .hasAnyRole(
                                "CUSTOMER",
                                "ADMIN"
                        )

                        /*
                         * REVIEWS
                         */

                        .requestMatchers(
                                "/api/review/**"
                        )
                        .hasAnyRole(
                                "CUSTOMER",
                                "PROVIDER",
                                "ADMIN"
                        )

                        /*
                         * EVERYTHING ELSE
                         */

                        .anyRequest()
                        .authenticated()
                )

                /*
                 * JWT FILTER
                 */

                .addFilterBefore(

                        jwtAuthenticationFilter,

                        UsernamePasswordAuthenticationFilter
                                .class
                );

        return http.build();
    }

    /*
     * Authentication Manager
     */

    @Bean
    public AuthenticationManager
    authenticationManager(

            AuthenticationConfiguration config

    )

            throws Exception {

        return config
                .getAuthenticationManager();
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {

        return new WebMvcConfigurer() {

            @Override
            public void addResourceHandlers(
                    ResourceHandlerRegistry registry
            ) {

                registry.addResourceHandler(
                                "/uploads/**"
                        )
                        .addResourceLocations(
                                "file:uploads/"
                        );
            }
        };

    }

}