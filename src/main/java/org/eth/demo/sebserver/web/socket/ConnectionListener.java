/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.socket;

import java.security.Principal;

import org.eth.demo.sebserver.domain.rest.exam.ClientConnection.ConnectionStatus;
import org.eth.demo.sebserver.service.exam.run.ExamConnectionService;
import org.eth.demo.sebserver.web.clientauth.ClientConnectionAuth.SEBWebSocketAuth;
import org.eth.demo.sebserver.web.clientauth.SEBClientConnectionController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
public class ConnectionListener {

    private static final Logger log = LoggerFactory.getLogger(ConnectionListener.class);

    private final ExamConnectionService examConnectionService;

    public ConnectionListener(final ExamConnectionService examConnectionService) {
        this.examConnectionService = examConnectionService;
    }

    @EventListener
    public void handleUnsubscribe(final SessionUnsubscribeEvent event) {

        log.info("Received unsubscribe event: {}", event);

        final StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        final Principal principal = headerAccessor.getUser();
        final boolean hasConnectionToken = headerAccessor.containsNativeHeader(
                SEBClientConnectionController.CONNECTION_TOKEN_KEY_NAME);
        if (hasConnectionToken) {
            markClosedOrAborted(principal, false);
        }
    }

    @EventListener
    public void handleWebSocketDisconnect(final SessionDisconnectEvent event) {

        log.info("Received disconnect event: {}", event);

        final StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        final Principal principal = headerAccessor.getUser();
        markClosedOrAborted(principal, true);
    }

    private void markClosedOrAborted(final Principal principal, final boolean aborted) {
        if (principal instanceof SEBWebSocketAuth) {
            final SEBWebSocketAuth user = (SEBWebSocketAuth) principal;

            log.info("Closing connection for user: {} to status: {}",
                    user.userIdentifier,
                    (aborted) ? ConnectionStatus.ABORTED : ConnectionStatus.CLOSED);

            try {
                this.examConnectionService.closeConnection(user, aborted);
            } catch (final Exception e) {
                log.error("Unexpected error while trying to close connection for user. {}", user);
            }
        } else {
            log.error("Principal is null or not of expected type SEBWebSocketAuth. Skip closing connection");
        }
    }

}
