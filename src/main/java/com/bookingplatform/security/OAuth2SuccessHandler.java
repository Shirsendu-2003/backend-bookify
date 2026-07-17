package com.bookingplatform.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.
        OAuth2User;
import org.springframework.security.web.authentication.
        AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler
        implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(

            HttpServletRequest request,

            HttpServletResponse response,

            Authentication authentication

    )

            throws IOException,
            ServletException {

        OAuth2User oauthUser =

                (OAuth2User)
                        authentication
                                .getPrincipal();

        /*
         * Generate JWT
         */

        String token =

                jwtService
                        .generateAccessToken(

                                (org.springframework.security
                                        .core.userdetails
                                        .UserDetails)

                                        authentication
                                                .getPrincipal()

                        );

        /*
         * Redirect frontend
         */

        String redirectUrl =

                frontendUrl

                        +

                        "/oauth2/success?token="

                        +

                        token;

        response.sendRedirect(
                redirectUrl
        );
    }

}