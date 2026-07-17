package com.bookingplatform.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bookingPlatformOpenAPI() {

        final String securitySchemeName =
                "bearerAuth";

        return new OpenAPI()

                .info(

                        new Info()

                                .title(
                                        "Booking Platform API"
                                )

                                .description(
                                        "Spring Boot Booking Platform Backend API Documentation"
                                )

                                .version(
                                        "v1.0.0"
                                )

                )

                .addSecurityItem(

                        new SecurityRequirement()
                                .addList(
                                        securitySchemeName
                                )

                )

                .components(

                        new Components()

                                .addSecuritySchemes(

                                        securitySchemeName,

                                        new SecurityScheme()

                                                .name(
                                                        securitySchemeName
                                                )

                                                .type(
                                                        SecurityScheme.Type.HTTP
                                                )

                                                .scheme(
                                                        "bearer"
                                                )

                                                .bearerFormat(
                                                        "JWT"
                                                )

                                )

                );
    }

}