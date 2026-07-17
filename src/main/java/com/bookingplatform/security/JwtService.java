package com.bookingplatform.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtUtil jwtUtil;

    /*
     * ==========================
     * ACCESS TOKEN
     * ==========================
     */

    public String generateAccessToken(
            UserDetails userDetails
    ) {

        Map<String,Object> claims =
                new HashMap<>();

        claims.put(
                "username",
                userDetails.getUsername()
        );

        claims.put(
                "authorities",
                userDetails
                        .getAuthorities()
        );

        return jwtUtil.generateToken(
                claims,
                userDetails
        );
    }

    /*
     * ==========================
     * REFRESH TOKEN
     * ==========================
     */

    public String generateRefreshToken(
            UserDetails userDetails
    ) {

        return jwtUtil.generateToken(
                userDetails
        );
    }

    /*
     * ==========================
     * VALIDATION
     * ==========================
     */

    public boolean validateToken(

            String token,

            UserDetails userDetails

    ) {

        return jwtUtil.isTokenValid(
                token,
                userDetails
        );
    }

    public String extractUsername(
            String token
    ) {

        return jwtUtil.extractUsername(
                token
        );
    }

}