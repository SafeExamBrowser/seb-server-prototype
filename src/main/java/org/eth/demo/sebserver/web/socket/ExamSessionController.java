/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.socket;

import org.eth.demo.sebserver.domain.rest.exam.ClientEvent;
import org.eth.demo.sebserver.domain.rest.exam.ConnectionInfo;
import org.eth.demo.sebserver.domain.rest.exam.Exam;
import org.eth.demo.sebserver.service.exam.run.ExamConnectionService;
import org.eth.demo.sebserver.service.exam.run.ExamSessionService;
import org.eth.demo.sebserver.web.clientauth.ClientConnectionAuth.SEBWebSocketAuth;
import org.eth.demo.sebserver.web.clientauth.SEBClientConnectionController;
import org.eth.demo.sebserver.web.socket.Message.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;

@Controller
@RestController
public class ExamSessionController {

    private static final Logger log = LoggerFactory.getLogger(ExamSessionController.class);

    private final ExamSessionService examSessionService;
    private final ExamConnectionService examConnectionService;
    private final ObjectMapper jsonMapper = new ObjectMapper();

    public ExamSessionController(
            final ExamSessionService examSessionService,
            final ExamConnectionService examConnectionService) {

        this.examSessionService = examSessionService;
        this.examConnectionService = examConnectionService;
    }

    @SubscribeMapping("/runningexam/wsconnect/*")
    public String subscribe(
            @Header(SEBClientConnectionController.CONNECTION_TOKEN_KEY_NAME) final String connectionToken,
            final SEBWebSocketAuth auth) {

        log.debug("SEB-Client Web-Socket subscription with token: {}", connectionToken);

        try {

            final Exam connectClientToExam = this.examConnectionService
                    .establishConnection(auth);

            // TODO verify, get and send SEB-configuration for specified SEB-client

            return messageToString(new Message(
                    Type.CONNECT,
                    System.currentTimeMillis(),
                    "TODO: send SEB-configuration"));
        } catch (final Exception e) {
            return messageToString(new Message(
                    Type.ERROR,
                    System.currentTimeMillis(),
                    e.getMessage()));
        }
    }

    @MessageMapping("/runningexam/wsconnect/*")
    public void connectionMessages(@Payload final String payload) {
        System.out.println("****************************** payload " + payload);
    }

    @MessageMapping("/runningexam/event")
    public void eventpoint(
            @Payload final ClientEvent clientEvent,
            final SEBWebSocketAuth auth) {

        this.examSessionService.notifyClientEvent(clientEvent.setAuth(auth));
    }

    @MessageExceptionHandler(Exception.class)
    @SendToUser("/runningexam/error")
    public String handleException(final Exception e) {
        return messageToString(
                new Message(Type.ERROR, System.currentTimeMillis(), e.getMessage()));
    }

    // TODO: This is now called by the UI within HTTP polling
    //       But a better (performance) strategy would be to establish also a WebSocket connection
    //       here and push indicator changes to the GUI
    @RequestMapping(value = "/runningexam/indicatorValues/{examId}", method = RequestMethod.GET)
    public Flux<ConnectionInfo> connectionInfo(@PathVariable final Long examId) {
        return Flux.fromIterable(
                this.examSessionService.getConnectionInfo(examId));
    }

    private String messageToString(final Message message) {
        try {
            return this.jsonMapper.writeValueAsString(message);
        } catch (final JsonProcessingException e) {
            return e.getMessage();
        }
    }

}
