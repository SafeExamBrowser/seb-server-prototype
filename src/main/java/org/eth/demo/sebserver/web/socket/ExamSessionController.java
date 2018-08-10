/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.socket;

import java.util.UUID;

import org.eth.demo.sebserver.domain.rest.exam.ClientEvent;
import org.eth.demo.sebserver.domain.rest.exam.LMSClientAuth;
import org.eth.demo.sebserver.domain.rest.exam.SEBClientAuth;
import org.eth.demo.sebserver.service.exam.run.ExamSessionService;
import org.eth.demo.sebserver.web.socket.Message.Type;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Deprecated // New implementation is in SEBClientConnectionController this works only with old SEB-Client-Bot's
@Controller
public class ExamSessionController {

    private final ExamSessionService examSessionService;
    private final ObjectMapper jsonMapper = new ObjectMapper();

    public ExamSessionController(final ExamSessionService examSessionService) {
        this.examSessionService = examSessionService;
    }

    @SubscribeMapping("/exam.{examId}/session.{sessionId}")
    public String subscribe(
            @DestinationVariable final long examId,
            @DestinationVariable final String sessionId) {

        try {
            final String uuid = UUID.randomUUID().toString();
            this.examSessionService.handshakeSEBClient(new SEBClientAuth(uuid, ""));
            this.examSessionService.handshakeLMSClient(new LMSClientAuth(uuid, uuid));
            this.examSessionService.connectClientToExam(Long.valueOf(examId), uuid);

            return messageToString(
                    new Message(Type.CONNECT, System.currentTimeMillis(), uuid));
        } catch (final Exception e) {
            e.printStackTrace();
            return messageToString(
                    new Message(Type.ERROR, System.currentTimeMillis(), e.getMessage()));
        }
    }

    @MessageMapping("/exam.{examId}/event")
    public void event(
            @DestinationVariable final long examId,
            @Payload final ClientEvent clientEvent,
            final SimpMessageHeaderAccessor accessor) {

        this.examSessionService.notifyClientEvent(clientEvent);
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
