/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.bot;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WebSocketClientBot {

    public static final String DEFAULT_ROOT_URL = "ws://localhost:8080/ws";
    public static final long DEFAULT_EXAM_ID = 4;
    public static final int DEFAULT_CONNECT_ATTEMPTS = 3;

    private static final Logger log = LoggerFactory.getLogger(WebSocketClientBot.class);

    private static final long ONE_SECOND = 1000; // milliseconds
    private static final long TEN_SECONDS = 10 * ONE_SECOND;
    private static final long ONE_MINUTE = 60 * ONE_SECOND;

    private WebSocketClientBot(final String rootURL,
            final long examId,
            final int connectAttempts,
            final long errorTimeInterval,
            final long pingTimeInterval,
            final long runtime) {

        final SEBServerConnection connection = new SEBServerConnection(examId);
        final String token = connection.connect(rootURL);

        log.info("Successfully connected to SEBServer. Token: {}", token);

        try {
            final long startTime = System.currentTimeMillis();
            final long endTime = startTime + runtime;
            long currentTime = startTime;
            long lastPingTime = startTime;
            long lastErrorTime = startTime;

            while (currentTime < endTime) {
                if (currentTime - lastPingTime >= pingTimeInterval) {
                    connection.sendEvent(token, 1, "Ping from client: " + token);
                    lastPingTime = currentTime;
                }
                if (currentTime - lastErrorTime >= errorTimeInterval) {
                    connection.sendEvent(token, 2, "Error from client: " + token);
                    lastErrorTime = currentTime;
                }
                try {
                    Thread.sleep(100);
                } catch (final Exception e) {
                }
                currentTime = System.currentTimeMillis();
            }
        } catch (final Throwable t) {
            log.error("Error sending events: " + t);
        } finally {
            try {
                connection.disconnect(/* token */);
            } catch (final Throwable t) {
                log.error("Error on disconnect: " + t);
            }
        }
        connection.sendEvent(token, 1, "Ping from client: " + token);
    }

    public static void main(final String[] args) {
        new WebSocketClientBot(
                DEFAULT_ROOT_URL,
                DEFAULT_EXAM_ID,
                DEFAULT_CONNECT_ATTEMPTS,
                TEN_SECONDS, 100,
                ONE_MINUTE);
    }

    static final class SEBServerConnection {

        private static final Logger log = LoggerFactory.getLogger(WebSocketClientBot.SEBServerConnection.class);

        private final WebSocketStompClient client;

        private final long examId;
        private ConnectionReference connRef;

        private int connectionAttempt = 0;

        SEBServerConnection(final long examId) {
            this.examId = examId;

            this.client = new WebSocketStompClient(new SockJsClient(
                    Stream.of(new WebSocketTransport(new StandardWebSocketClient()))
                            .collect(Collectors.toList())));

            this.client.setMessageConverter(new StringMessageConverter() {
                @Override
                public List<MimeType> getSupportedMimeTypes() {
                    final ArrayList<MimeType> types = new ArrayList<>();
                    types.addAll(super.getSupportedMimeTypes());
                    types.add(MimeTypeUtils.APPLICATION_JSON);
                    return types;
                }
            });
        }

        String connect(final String url) {
            try {
                this.connectionAttempt++;
                if (this.connectionAttempt > 3) {
                    return null;
                }

                log.info("Trying to connect to SEBServer on URL: {}, attempt: {}", url, this.connectionAttempt);

                this.connRef = new ConnectionReference(this.examId);
                return this.connRef.connect(this.client, url);

            } catch (final Exception e) {
                log.error("Error while trying to connect to SEBServer on URL: {}", url, e);
                try {
                    Thread.sleep(ONE_SECOND);
                } catch (final Exception es) {
                }

                if (isConnected()) {
                    disconnect();
                }

                return connect(url);
            }
        }

        boolean isConnected() {
            return this.connRef != null && this.connRef.isConnected();
        }

        boolean sendEvent(final String token,
                final int eventType,
                final String text) {

            if (!isConnected()) {
                return false;
            }

            log.trace("Trying to post an event to {} using token: {}", "/app/exam.4/event", token);

            final JSONObject json = new JSONObject();
            try {
                json.put("clientId", token);
                json.put("type", eventType);
                json.put("timestamp", System.currentTimeMillis());
                json.put("text", text);
            } catch (final Exception e) {
                throw new RuntimeException("Unexpected Error: ", e);
            }
            this.connRef.stompSession.send(this.connRef.eventHeaders, json.toString());
            return true;
        }

        void disconnect() {
            this.connRef.disconnect();
        }

        static final class ConnectionReference {

            private StompSession stompSession;
            final SessionHandler sessionHandler;

            final String sessionSubscriptionPrefix;
            final String eventEndpoint;

            final StompHeaders connectionHeaders;
            final StompHeaders sessionSubscriptionHeaders;
            final StompHeaders errorSubscriptionHeaders;
            final StompHeaders eventHeaders;
            final StompHeaders unsubscribeHeaders;

            Subscription sessionSubscription;
            Subscription errorSubscription;

            ConnectionReference(final long examId) {
                this.sessionSubscriptionPrefix = "/app/exam." + examId + "/session.";
                this.eventEndpoint = "/app/exam." + examId + "/event";

                this.sessionHandler = new SessionHandler();

                this.connectionHeaders = new StompHeaders();

                this.eventHeaders = new StompHeaders();
                this.eventHeaders.add(StompHeaders.CONTENT_TYPE, "application/json");
                this.eventHeaders.setDestination(this.eventEndpoint);

                this.sessionSubscriptionHeaders = new StompHeaders();
                this.sessionSubscriptionHeaders.add(StompHeaders.CONTENT_TYPE, "application/json");

                this.errorSubscriptionHeaders = new StompHeaders();
                this.errorSubscriptionHeaders.setDestination("/user/exam/error");

                this.unsubscribeHeaders = new StompHeaders();
            }

            String connect(final WebSocketStompClient client, final String url)
                    throws InterruptedException, ExecutionException, TimeoutException {
                this.stompSession = client.connect(
                        url,
                        this.sessionHandler,
                        this.connectionHeaders).get();

                log.debug("Connection established, trying to subscribe...");

                this.errorSubscription = this.stompSession.subscribe(
                        this.errorSubscriptionHeaders,
                        this.sessionHandler);

                this.sessionSubscriptionHeaders.setDestination(
                        this.sessionSubscriptionPrefix +
                                this.stompSession.getSessionId());

                this.sessionSubscription = this.stompSession.subscribe(
                        this.sessionSubscriptionHeaders,
                        this.sessionHandler);

                log.debug("subscription successfull");

                final String token = this.sessionHandler.connectioToken.get(5, TimeUnit.SECONDS);
                this.eventHeaders.set("TOKEN", token);
                this.unsubscribeHeaders.set("TOKEN", token);
                return token;
            }

            boolean isConnected() {
                return this.stompSession != null && this.stompSession.isConnected();
            }

            void disconnect() {
                if (this.sessionSubscription != null) {
                    this.sessionSubscription.unsubscribe(this.unsubscribeHeaders);
                }
                if (this.errorSubscription != null) {
                    this.errorSubscription.unsubscribe();
                }
                this.stompSession.disconnect();
            }
        }
    }

    static final class SessionHandler implements StompSessionHandler {

        private static final Logger log = LoggerFactory.getLogger(WebSocketClientBot.SessionHandler.class);
        private final ObjectMapper jsonMapper = new ObjectMapper();

        int connectAttempts = 0;
        CompletableFuture<String> connectioToken = new CompletableFuture<>();

        @Override
        public Type getPayloadType(final StompHeaders headers) {
            return String.class;
        }

        @Override
        public void handleFrame(final StompHeaders headers, final Object payload) {
            log.debug("connection replied: {}, {}", headers, payload);
            try {
                final Message message = this.jsonMapper.readValue(payload.toString(), Message.class);
                log.debug("received message: {}", message);

                switch (message.type) {
                    case CONNECT: {
                        this.connectioToken.complete(message.content);
                        break;
                    }
                    case ERROR: {
                        // TODO: handle error message
                        break;
                    }
                    case UPDATE_CONFIG: {
                        // TODO handle config update
                        break;
                    }
                }
            } catch (final Exception e) {
                log.error("JSON Error: ", e);
            }
        }

        @Override
        public void afterConnected(final StompSession session, final StompHeaders connectedHeaders) {
            log.debug("connected: {}, {}", session, connectedHeaders);
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

    public static class Message {

        enum Type {
            CONNECT, UPDATE_CONFIG, ERROR
        }

        public final Type type;
        public final long timestamp;
        public final String content;

        Message(@JsonProperty("type") final Type type,
                @JsonProperty("timestamp") final long timestamp,
                @JsonProperty("content") final String content) {

            this.type = type;
            this.timestamp = timestamp;
            this.content = content;
        }

        @Override
        public String toString() {
            return "Message [type=" + this.type + ", timestamp=" + this.timestamp + ", content=" + this.content + "]";
        }
    }

}
