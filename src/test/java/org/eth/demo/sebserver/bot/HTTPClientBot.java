/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.bot;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class HTTPClientBot {

    public static final String DEFAULT_ROOT_URL = "http://localhost:8080/exam";
    public static final long DEFAULT_EXAM_ID = 4;
    public static final int DEFAULT_CONNECT_ATTEMPTS = 3;

    private static final Logger log = LoggerFactory.getLogger(HTTPClientBot.class);

    private static final long ONE_SECOND = 1000; // milliseconds
    private static final long TEN_SECONDS = 10 * ONE_SECOND;
    private static final long ONE_MINUTE = 60 * ONE_SECOND;

    private final RestTemplate restTemplate = new RestTemplate();

    public static void main(final String[] args) {
        new HTTPClientBot(
                DEFAULT_ROOT_URL,
                DEFAULT_EXAM_ID,
                DEFAULT_CONNECT_ATTEMPTS,
                TEN_SECONDS, 100,
                ONE_MINUTE);
    }

    private HTTPClientBot(
            final String rootURL,
            final long examId,
            final int connectAttempts,
            final long errorTimeInterval,
            final long pingTimeInterval,
            final long runtime) {

        // connection
        String token = null;
        for (int i = 0; i < connectAttempts; i++) {
            try {
                token = connect(rootURL, examId);
                break;
            } catch (final Throwable t) {
                log.error("Error while trying to connect to exam: {} : ", examId, t);
            }
            try {
                Thread.sleep(ONE_SECOND);
            } catch (final Exception e) {
            }
        }

        if (token == null) {
            log.error("Failed to connect to exam: {} give up after {} attempts", examId, connectAttempts);
            return;
        }

        try {

            final long startTime = System.currentTimeMillis();
            final long endTime = startTime + runtime;
            long currentTime = startTime;
            long lastPingTime = startTime;
            long lastErrorTime = startTime;

            while (currentTime < endTime) {
                if (currentTime - lastPingTime >= pingTimeInterval) {
                    sendPingEvent(rootURL, examId, token);
                    lastPingTime = currentTime;
                }
                if (currentTime - lastErrorTime >= errorTimeInterval) {
                    sendErrorEvent(rootURL, examId, token);
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
                disconnect(rootURL, token);
            } catch (final Throwable t) {
                log.error("Error on disconnect: " + t);
            }
        }
    }

    private String connect(final String rootURL, final long examId) {
        final String url = rootURL + "/connect/" + examId;
        log.trace("Trying to connect to exam: {}", url);

        final UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(url);

        return this.restTemplate.getForObject(builder.toUriString(), String.class);
    }

    private void sendPingEvent(final String rootURL, final long examId, final String token) {
        sendEvent(rootURL, examId, token, 1, "Ping from client: " + token);
    }

    private void sendErrorEvent(final String rootURL, final long examId, final String token) {
        sendEvent(rootURL, examId, token, 2, "Error from client: " + token);
    }

    private void sendEvent(
            final String rootURL,
            final long examId,
            final String token,
            final int eventType,
            final String text) {

        final String url = rootURL + "/event/" + examId;
        log.trace("Trying to post a ping event to {} using token: {}", url, token);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        httpHeaders.set("Token", token);

        final JSONObject json = new JSONObject();
        try {
            json.put("clientId", token);
            json.put("type", eventType);
            json.put("timestamp", System.currentTimeMillis());
            json.put("text", text);
        } catch (final Exception e) {
            throw new RuntimeException("Unexpected Error: ", e);
        }

        log.info("event data: {}", json.toString());

        final HttpEntity<String> httpEntity = new HttpEntity<>(json.toString(), httpHeaders);

        final UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(url);

        final String response = this.restTemplate.postForObject(
                builder.toUriString(),
                httpEntity,
                String.class);

        log.trace("response data: {}", response);
    }

    private void disconnect(final String rootURL, final String token) {
        final String url = rootURL + "/disconnect";
        log.trace("Trying to disconnect uuid: {}", token);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        httpHeaders.set("Token", token);

        final HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        final UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(url);

        final String response = this.restTemplate.postForObject(
                builder.toUriString(),
                httpEntity, String.class);

        log.trace("response data: {}", response);
    }

}
