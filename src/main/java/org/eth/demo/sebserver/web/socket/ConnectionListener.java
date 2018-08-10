/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.socket;

import org.eth.demo.sebserver.service.exam.run.ExamSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
public class ConnectionListener {

    private static final Logger log = LoggerFactory.getLogger(ConnectionListener.class);

    private final ExamSessionService examSessionService;

    public ConnectionListener(final ExamSessionService examSessionService) {
        this.examSessionService = examSessionService;
    }

    @EventListener
    public void handleWebSocketConnect(final SessionConnectedEvent event) {
        log.info("Received a new web socket connection: {}", event);
    }

    @EventListener
    public void handleUnsubscribe(final SessionUnsubscribeEvent event) {
        log.info("unsubscribe: {}", event);
        final StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        final String token = headerAccessor.getFirstNativeHeader("TOKEN");

        // TODO how can we dedicate in which channel the unsubscribe event has been applied
        if (token != null) {
            this.examSessionService.closeConnection(token, false);
        }
    }

    @EventListener
    public void handleWebSocketDisconnect(final SessionDisconnectEvent event) {
        log.info("disconnect: {}", event);
    }

}
