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
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
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

        final StompHeaders headers = new StompHeaders();
        headers.add("examId", "4");

        final SessionHandler sessionHandler = new SessionHandler();

        try {
            final StompSession stompSession =
                    this.client.connect("wss://localhost:8080/ws", sessionHandler, headers).get();

            while (sessionHandler.connected) {
                try {
                    Thread.sleep(1000);
                } catch (final InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) {
        new WebSocketClientBot();
    }

    static final class SessionHandler implements StompSessionHandler {

        private static final Logger log = LoggerFactory.getLogger(WebSocketClientBot.SessionHandler.class);

        boolean connected = false;

        @Override
        public Type getPayloadType(final StompHeaders headers) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void handleFrame(final StompHeaders headers, final Object payload) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterConnected(final StompSession session, final StompHeaders connectedHeaders) {
            this.connected = true;

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
