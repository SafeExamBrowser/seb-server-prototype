/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.socket;

import org.eth.demo.sebserver.domain.rest.exam.ClientEvent;
import org.eth.demo.sebserver.service.exam.run.ExamConnectionService;
import org.eth.demo.sebserver.service.exam.run.ExamSessionService;
import org.eth.demo.sebserver.web.clientauth.ClientConnectionAuth;
import org.eth.demo.sebserver.web.clientauth.ClientConnectionAuth.SEBWebSocketAuth;
import org.eth.demo.sebserver.web.socket.Message.Type;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ExamSessionController {

    private final ExamSessionService examSessionService;
    private final ExamConnectionService examConnectionService;
    private final ObjectMapper jsonMapper = new ObjectMapper();

    public ExamSessionController(
            final ExamSessionService examSessionService,
            final ExamConnectionService examConnectionService) {

        this.examSessionService = examSessionService;
        this.examConnectionService = examConnectionService;
    }

    @Deprecated // this is still supporting bot's running with no LMS
    @SubscribeMapping("/exam.{examId}/session.{sessionId}")
    public String subscribe(
            @DestinationVariable final long examId,
            @DestinationVariable final String sessionId) {

        try {
            final String uuid = this.examConnectionService.handshakeSEBClient("", examId);
            this.examConnectionService.handshakeLMSClient(uuid, uuid, null);
            this.examConnectionService.connectClientToExam(Long.valueOf(examId), uuid);

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(ClientConnectionAuth.sebWebSocketAuthOf(
                            examId,
                            0L,
                            uuid));

            return messageToString(
                    new Message(Type.CONNECT, System.currentTimeMillis(), uuid));
        } catch (final Exception e) {
            e.printStackTrace();
            return messageToString(
                    new Message(Type.ERROR, System.currentTimeMillis(), e.getMessage()));
        }
    }

    @Deprecated // this is still supporting bot's running with no LMS
    @MessageMapping("/exam.{examId}/event")
    public void event(
            @DestinationVariable final long examId,
            @Payload final ClientEvent clientEvent,
            final SEBWebSocketAuth auth) {

        this.examSessionService.notifyClientEvent(clientEvent.setAuth(auth));
    }

    @MessageMapping("/runningexam/event")
    public void eventpoint(
            @Payload final ClientEvent clientEvent,
            final SEBWebSocketAuth auth) {

        this.examSessionService.notifyClientEvent(clientEvent.setAuth(auth));
    }

    @MessageExceptionHandler(Exception.class)
    @SendToUser("/exam/error")
    public String handleException(final Exception e) {
        return messageToString(
                new Message(Type.ERROR, System.currentTimeMillis(), e.getMessage()));
    }

    private String messageToString(final Message message) {
        try {
            return this.jsonMapper.writeValueAsString(message);
        } catch (final JsonProcessingException e) {
            return e.getMessage();
        }
    }

}
