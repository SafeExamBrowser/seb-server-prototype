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
import org.eth.demo.sebserver.service.exam.run.NewExamSessionService;
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
public class SEBClientConnection {

    private final ExamDao examDao;
    private final NewExamSessionService examSessionService;
    private final ObjectMapper jsonMapper = new ObjectMapper();

    public SEBClientConnection(
            final ExamDao examDao,
            final NewExamSessionService examSessionService) {

        this.examDao = examDao;
        this.examSessionService = examSessionService;
    }

    /** SEB-Client authentication is the fist step on SEB-Client connection setup for a running exam. The
     * upstream-filter ClientConnectionAuthenticationFilter can define several methods to authenticate a SEB-Client and
     * login the SEB-Client within the clients LMS system.
     *
     * After successful authentication of the SEB-Client within the upstream-filter ClientConnectionAuthenticationFilter
     * This mapping gets an Authentication instance with a SEBClientAuth principal. This end-point then registers the
     * authenticated SEB-Client within the SEB-Server and sends back a list of currently running exams.
     *
     * @param authentication Authentication instance with a SEBClientAuth principal
     * @return a list of currently running exams */
    @RequestMapping(
            value = "/runningexam/auth",
            method = RequestMethod.GET,
            produces = Const.CONTENT_TYPE_APPLICATION_JSON)
    public final Collection<Exam> sebClientAuth(final Authentication authentication) {

        // NOTE: after ClientConnectionAuthenticationFilter has successfully authenticated
        //       a client against an LMS, we should be able here to get the User instance
        //       created ad-hoc by the ClientConnectionAuthenticationFilter with all
        //       needed information (institution)
        final SEBClientAuth client = (SEBClientAuth) authentication.getPrincipal();
        this.examSessionService.registerConnection(client);

        // TODO add also institution extracted from the User to the filter
        final Collection<Exam> runningExams = this.examDao.getAll(e -> e.status == ExamStatus.RUNNING);

        return runningExams;
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
