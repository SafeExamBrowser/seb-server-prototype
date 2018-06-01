/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.bot;

import java.lang.reflect.Type;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

public class WebSocketClientBot {

    private final WebSocketStompClient client;

    private WebSocketClientBot() {

        this.client = new WebSocketStompClient(new SockJsClient(
                Stream.of(new WebSocketTransport(new StandardWebSocketClient())).collect(Collectors.toList())));
        this.client.setMessageConverter(new StringMessageConverter());

        final StompHeaders headers = new StompHeaders();
        headers.add(StompHeaders.CONTENT_TYPE, "application/json");

        final SessionHandler sessionHandler = new SessionHandler();

        try {
            final StompSession stompSession =
                    this.client.connect("ws://localhost:8080/ws", sessionHandler, headers).get();

            System.out.println("********************** connected: " + stompSession.isConnected());

            final Subscription subscribe = stompSession.subscribe("/user/queue/reply", sessionHandler);

            stompSession.send("/app/connect/4", "{}");

            while (stompSession.isConnected()) {
                try {
                    Thread.sleep(1000);
                } catch (final InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            subscribe.unsubscribe();
            stompSession.disconnect();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) {
        new WebSocketClientBot();
    }

    static final class SessionHandler implements StompSessionHandler {

        private static final Logger log = LoggerFactory.getLogger(WebSocketClientBot.SessionHandler.class);

        @Override
        public Type getPayloadType(final StompHeaders headers) {
            return String.class;
        }

        @Override
        public void handleFrame(final StompHeaders headers, final Object payload) {
            log.info("connection replied: {}, {}", headers, payload);
        }

        @Override
        public void afterConnected(final StompSession session, final StompHeaders connectedHeaders) {
            log.info("connected: {}, {}", session, connectedHeaders);

        }

        @Override
        public void handleException(final StompSession session, final StompCommand command, final StompHeaders headers,
                final byte[] payload,
                final Throwable exception) {
            log.error("ERROR: ", exception);
        }

        @Override
        public void handleTransportError(final StompSession session, final Throwable exception) {
            log.error("TRANSPORT ERROR: ", exception);
        }

    }

}
