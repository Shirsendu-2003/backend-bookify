package com.bookingplatform.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.
        UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.
        SecurityContextHolder;
import org.springframework.security.core.userdetails.
        UserDetails;
import org.springframework.security.web.authentication.
        WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.
        OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final CustomUserDetailsService
            userDetailsService;

    @Override
    protected void doFilterInternal(

            HttpServletRequest request,

            HttpServletResponse response,

            FilterChain filterChain

    )

            throws ServletException,
            IOException {

        try {

            final String authHeader =
                    request.getHeader(
                            "Authorization"
                    );

            System.out.println(
                    "AUTH HEADER = "
                            + authHeader
            );

            if (

                    authHeader == null ||

                            !authHeader.startsWith(
                                    "Bearer "
                            )

            ) {

                System.out.println(
                        "NO TOKEN FOUND"
                );

                filterChain.doFilter(
                        request,
                        response
                );

                return;
            }

            String jwt =
                    authHeader.substring(7);

            System.out.println(
                    "JWT = " + jwt
            );

            String username =
                    jwtService.extractUsername(
                            jwt
                    );

            System.out.println(
                    "USERNAME = "
                            + username
            );

            if (

                    username != null &&

                            SecurityContextHolder
                                    .getContext()
                                    .getAuthentication()

                                    == null

            ) {

                UserDetails userDetails =

                        userDetailsService
                                .loadUserByUsername(
                                        username
                                );

                System.out.println(
                        "USER FOUND = "
                                + userDetails
                                .getUsername()
                );

                boolean valid =

                        jwtService
                                .validateToken(

                                        jwt,

                                        userDetails

                                );

                System.out.println(
                        "TOKEN VALID = "
                                + valid
                );

                if(valid){

                    UsernamePasswordAuthenticationToken
                            authToken =

                            new UsernamePasswordAuthenticationToken(

                                    userDetails,

                                    null,

                                    userDetails
                                            .getAuthorities()

                            );

                    authToken.setDetails(

                            new WebAuthenticationDetailsSource()

                                    .buildDetails(
                                            request
                                    )

                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(
                                    authToken
                            );

                    System.out.println(
                            "AUTH SUCCESS"
                    );
                }
            }

        }

        catch(Exception e){

            System.out.println(
                    "JWT ERROR = "
                            + e.getMessage()
            );

            e.printStackTrace();
        }

        filterChain.doFilter(
                request,
                response
        );
    }

}