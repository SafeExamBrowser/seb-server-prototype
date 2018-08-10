/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.http;

import java.util.Collection;

import org.eth.demo.sebserver.domain.rest.exam.Exam;
import org.eth.demo.sebserver.domain.rest.exam.ExamStatus;
import org.eth.demo.sebserver.domain.rest.exam.SEBClientAuth;
import org.eth.demo.sebserver.service.exam.ExamDao;
import org.eth.demo.sebserver.service.exam.run.ExamSessionService;
import org.eth.demo.sebserver.web.socket.Message;
import org.eth.demo.sebserver.web.socket.Message.Type;
import org.eth.demo.util.Const;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RestController
public class SEBClientConnectionController {

    private final ExamDao examDao;
    private final ExamSessionService examSessionService;
    private final ObjectMapper jsonMapper = new ObjectMapper();

    public SEBClientConnectionController(
            final ExamDao examDao,
            final ExamSessionService examSessionService) {

        this.examDao = examDao;
        this.examSessionService = examSessionService;
    }

    /** SEB-Client authentication is the fist step on SEB-Client connection setup for a running exam.
     *
     * After successful authentication of the SEB-Client within the upstream-filter SEBClientAuthenticationFilter This
     * mapping gets an Authentication instance with a SEBClientAuth principal.
     *
     * This end-point correspond to point 3.a. and 3.b within specification (https://confluence.let.ethz.ch/x/uofQAQ)
     * In case of 3.a. the end-point responses with a list of running exams of the clients institution in JSON format.
     * In case of 3.b. there is only one Exam in the list but we have first to clarify how to associates a SEB-Client to
     * a specific exam within the SEB-Client start-config which should only be generated once per institution.
     * NOTE: This point is currently not really clear from the specification.
     *
     * @param authentication Authentication instance with a SEBClientAuth principal
     * @return a list of currently running exams of the clients institution */
    @RequestMapping(
            value = "/runningexam/auth",
            method = RequestMethod.GET,
            produces = Const.CONTENT_TYPE_APPLICATION_JSON)
    public final Collection<Exam> sebClientAuth_A(final Authentication authentication) {

        final SEBClientAuth clientAuth = (SEBClientAuth) authentication.getPrincipal();
        this.examSessionService.handshakeSEBClient(clientAuth);

        // get running exams for clients institution
        return this.examDao.getAll(
                e -> e.status == ExamStatus.RUNNING &&
                        e.institutionId.longValue() == clientAuth.getInstitutionId().longValue());

    }

    @SubscribeMapping("/runningexam/wsconnect")
    public String subscribe(@Header("examId") final long examId) {
        try {

            // TODO establish connection within

            return null;
        } catch (final Exception e) {
            return messageToString(
                    new Message(Type.ERROR, System.currentTimeMillis(), e.getMessage()));
        }
    }

    private String messageToString(final Message message) {
        try {
            return this.jsonMapper.writeValueAsString(message);
        } catch (final JsonProcessingException e) {
            return e.getMessage();
        }
    }

}
