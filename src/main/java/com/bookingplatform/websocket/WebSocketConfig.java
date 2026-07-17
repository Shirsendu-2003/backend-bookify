package com.bookingplatform.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig
        implements WebSocketMessageBrokerConfigurer {

    /*
     * ==========================
     * MESSAGE BROKER
     * ==========================
     */

    @Override
    public void configureMessageBroker(
            MessageBrokerRegistry registry
    ) {

        /*
         * Server → Client
         */

        registry.enableSimpleBroker(
                "/topic",
                "/queue"
        );

        /*
         * Client → Server
         */

        registry.setApplicationDestinationPrefixes(
                "/app"
        );

        /*
         * Private User Queue
         */

        registry.setUserDestinationPrefix(
                "/user"
        );
    }

    /*
     * ==========================
     * STOMP ENDPOINT
     * ==========================
     */

    @Override
    public void registerStompEndpoints(
            StompEndpointRegistry registry
    ) {

        registry.addEndpoint("/ws")

                .setAllowedOriginPatterns("*")

                .withSockJS();
    }

}