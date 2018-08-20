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
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eth.demo.util.Const;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.util.Base64Utils;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WebSocketClientBot {

    public static final String DEFAULT_HANDSHAKE_URL = "http://localhost:8080/sebauth/sebhandshake";
    public static final String DEFAULT_LMS_HANDSHAKE_URL = "http://localhost:8080/sebauth/lmshandshake";
    public static final String DEFAULT_WEB_SOCKET_ROOT_URL = "ws://localhost:8080/ws";
    public static final long DEFAULT_EXAM_ID = 4;
    public static final int DEFAULT_CONNECT_ATTEMPTS = 1;

    public static final boolean MOCK_LMS = true;

    private static final Logger log = LoggerFactory.getLogger(WebSocketClientBot.class);

    private static final long ONE_SECOND = 1000; // milliseconds
    private static final long TEN_SECONDS = 10 * ONE_SECOND;
    private static final long ONE_MINUTE = 60 * ONE_SECOND;

    public static void main(final String[] args) {
        new WebSocketClientBot(
                DEFAULT_WEB_SOCKET_ROOT_URL,
                DEFAULT_EXAM_ID,
                DEFAULT_CONNECT_ATTEMPTS,
                TEN_SECONDS, 100,
                TEN_SECONDS);
    }

    private final long errorTimeInterval;
    private final long pingTimeInterval;
    private final long runtime;

    private WebSocketClientBot(final String rootURL,
            final long examId,
            final int connectAttempts,
            final long errorTimeInterval,
            final long pingTimeInterval,
            final long runtime) {

        this.errorTimeInterval = errorTimeInterval;
        this.pingTimeInterval = pingTimeInterval;
        this.runtime = runtime;

        final ResponseEntity<String> handshake = doHandshake("sebclient", "sebclient");
        final String body = handshake.getBody();
        final String connectionToken = handshake.getHeaders().getFirst("connectionToken");

        if (MOCK_LMS) {
            final ResponseEntity<String> lmsHandshake = mockLMSHandshake(
                    connectionToken,
                    "testUser" + UUID.randomUUID().toString(),
                    "lmsclient",
                    "lmsclient",
                    "4");
        }

        final WebSocketConnection connection = new WebSocketConnection();
        final String sebConfiguration = connection.connect(rootURL, connectionToken, connectAttempts);

        log.info("Successfully establish web-socket connection to SEB-Server");
        log.debug("SEB-Server sent configuration {}", sebConfiguration);

        sendEvents(connection);
    }

    private ResponseEntity<String> doHandshake(final String clientname, final String secret) {
        final RestTemplate restTemplate = new RestTemplate();
        String credentials;
        try {
            credentials = Base64Utils.encodeToString((clientname + ":" + secret).getBytes("UTF8"));
            final HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, Const.CONTENT_TYPE_APPLICATION_JSON);
            httpHeaders.add(HttpHeaders.AUTHORIZATION, "Basic " + credentials);

            return restTemplate.exchange(
                    DEFAULT_HANDSHAKE_URL,
                    HttpMethod.POST,
                    new HttpEntity<String>(httpHeaders),
                    String.class);

        } catch (final Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private ResponseEntity<String> mockLMSHandshake(
            final String token,
            final String userId,
            final String clientname,
            final String secret,
            final String examId) {

        final RestTemplate restTemplate = new RestTemplate();
        String credentials;
        try {
            credentials = Base64Utils.encodeToString((clientname + ":" + secret).getBytes("UTF8"));
            final HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, Const.CONTENT_TYPE_APPLICATION_JSON);
            httpHeaders.add(HttpHeaders.AUTHORIZATION, "Basic " + credentials);
            httpHeaders.add("connectionToken", token);
            httpHeaders.add("userId", userId);

            return restTemplate.exchange(
                    DEFAULT_LMS_HANDSHAKE_URL + "?examId=" + examId,
                    HttpMethod.POST,
                    new HttpEntity<String>(httpHeaders),
                    String.class);

        } catch (final Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void sendEvents(final WebSocketConnection connection) {
        try {
            final long startTime = System.currentTimeMillis();
            final long endTime = startTime + this.runtime;
            long currentTime = startTime;
            long lastPingTime = startTime;
            long lastErrorTime = startTime;

            while (currentTime < endTime) {
                if (currentTime - lastPingTime >= this.pingTimeInterval) {
                    connection.sendEvent(1, "Ping from client: " + this);
                    lastPingTime = currentTime;
                }
                if (currentTime - lastErrorTime >= this.errorTimeInterval) {
                    connection.sendEvent(2, "Error from client: " + this);
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
    }

    static final class WebSocketConnection {

        private static final Logger log = LoggerFactory.getLogger(WebSocketClientBot.WebSocketConnection.class);

        private final WebSocketStompClient client;
        private ConnectionReference connRef;

        WebSocketConnection() {

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

        String connect(final String url, final String connectionToken, int connectAttempts) {
            try {
                connectAttempts--;
                if (connectAttempts < 0) {
                    return null;
                }

                log.info("Trying to connect to SEBServer on URL: {}, attempt: {}", url, connectAttempts);

                this.connRef = new ConnectionReference(connectionToken);
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

                return connect(url, connectionToken, connectAttempts);
            }
        }

        boolean isConnected() {
            return this.connRef != null && this.connRef.isConnected();
        }

        boolean sendEvent(final int eventType, final String text) {

            if (!isConnected()) {
                return false;
            }

            log.debug("Trying to post an event to {}", "/app/exam.4/event");

            final JSONObject json = new JSONObject();
            try {
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

            final WebSocketHttpHeaders connectionHeaders;
            final StompHeaders sessionSubscriptionHeaders;
            final StompHeaders errorSubscriptionHeaders;
            final StompHeaders eventHeaders;
            final StompHeaders unsubscribeHeaders;

            Subscription sessionSubscription;
            Subscription errorSubscription;

            ConnectionReference(final String connectionToken) {
                this.sessionSubscriptionPrefix = "/app/runningexam/wsconnect/";
                this.eventEndpoint = "/app/runningexam/event";

                this.sessionHandler = new SessionHandler();

                this.connectionHeaders = new WebSocketHttpHeaders();
                this.connectionHeaders.add("connectionToken", connectionToken);

                this.eventHeaders = new StompHeaders();
                this.eventHeaders.add(StompHeaders.CONTENT_TYPE, "application/json");
                this.eventHeaders.setDestination(this.eventEndpoint);

                this.sessionSubscriptionHeaders = new StompHeaders();
                this.sessionSubscriptionHeaders.add(StompHeaders.CONTENT_TYPE, "application/json");
                this.sessionSubscriptionHeaders.add("connectionToken", connectionToken);

                this.errorSubscriptionHeaders = new StompHeaders();
                this.errorSubscriptionHeaders.setDestination("/app/runningexam/error/");
                this.errorSubscriptionHeaders.add("connectionToken", connectionToken);

                this.unsubscribeHeaders = new StompHeaders();
                this.unsubscribeHeaders.add("connectionToken", connectionToken);
            }

            String connect(
                    final WebSocketStompClient client,
                    final String url) throws InterruptedException, ExecutionException, TimeoutException {

                this.stompSession = client.connect(
                        url,
                        this.connectionHeaders,
                        this.sessionHandler).get();

                log.debug("Connection established, trying to subscribe...");

                this.errorSubscription = this.stompSession.subscribe(
                        this.errorSubscriptionHeaders,
                        this.sessionHandler);

                this.sessionSubscriptionHeaders.setDestination(
                        this.sessionSubscriptionPrefix);

                this.sessionSubscription = this.stompSession.subscribe(
                        this.sessionSubscriptionHeaders,
                        this.sessionHandler);

                log.debug("subscription successfull");

                // block and wait for answer or timeout
                return this.sessionHandler.awaitingAnswer.get(5, TimeUnit.SECONDS);
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
        CompletableFuture<String> awaitingAnswer = new CompletableFuture<>();

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
                        this.awaitingAnswer.complete(message.content);
                        break;
                    }
                    case ERROR: {
                        this.awaitingAnswer.completeExceptionally(
                                new RuntimeException("SEBServer sent connection error: " + message.content));
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
