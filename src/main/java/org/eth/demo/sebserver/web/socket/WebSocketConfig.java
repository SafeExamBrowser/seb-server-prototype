/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.socket;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final String WEB_SOCKET_ENDPOINT = "/ws";

    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        registry.addEndpoint(WEB_SOCKET_ENDPOINT)
                .setAllowedOrigins("*")
                // TODO test if it is possible to do SEB-Client authentication-check on HandshakeInterceptor level
                .addInterceptors(new HandshakeInterceptor() {

                    @Override
                    public boolean beforeHandshake(final ServerHttpRequest request, final ServerHttpResponse response,
                            final WebSocketHandler wsHandler,
                            final Map<String, Object> attributes) throws Exception {
                        System.out.println("***************** beforeHandshake ");
                        return true;
                    }

                    @Override
                    public void afterHandshake(final ServerHttpRequest request, final ServerHttpResponse response,
                            final WebSocketHandler wsHandler,
                            final Exception exception) {
                        System.out.println("***************** afterHandshake ");
                    }
                })
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(final MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/runningexam");
        registry.setApplicationDestinationPrefixes("/app");
        //registry.enableSimpleBroker("/exam");
        //registry.setUserDestinationPrefix("/user");
    }

    @Override
    public boolean configureMessageConverters(final List<MessageConverter> messageConverters) {
        messageConverters.add(new StringMessageConverter());
        messageConverters.add(new MappingJackson2MessageConverter());
        return WebSocketMessageBrokerConfigurer.super.configureMessageConverters(messageConverters);
    }

}
