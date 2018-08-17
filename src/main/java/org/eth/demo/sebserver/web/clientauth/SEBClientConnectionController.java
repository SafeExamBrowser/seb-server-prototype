/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.clientauth;

import java.util.List;
import java.util.stream.Collectors;

import org.eth.demo.sebserver.domain.rest.exam.Exam;
import org.eth.demo.sebserver.domain.rest.exam.Exam.ExamStatus;
import org.eth.demo.sebserver.domain.rest.exam.ExamLink;
import org.eth.demo.sebserver.service.exam.ExamDao;
import org.eth.demo.sebserver.service.exam.run.ExamConnectionService;
import org.eth.demo.sebserver.web.clientauth.ClientConnectionAuth.LMSConnectionAuth;
import org.eth.demo.sebserver.web.clientauth.ClientConnectionAuth.SEBConnectionAuth;
import org.eth.demo.sebserver.web.socket.Message;
import org.eth.demo.sebserver.web.socket.Message.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RestController
public class SEBClientConnectionController {

    private static final Logger log = LoggerFactory.getLogger(SEBClientConnectionController.class);

    public static final String CONNECTION_TOKEN_KEY_NAME = "connectionToken";
    public static final String USER_IDENTIFIER_KEY_NAME = "userId";
    public static final String EXAM_IDENTIFIER_KEY_NAME = "examId";

    private final ExamDao examDao;
    private final ExamConnectionService examConnectionService;
    private final ObjectMapper jsonMapper;

    public SEBClientConnectionController(
            final ExamDao examDao,
            final ExamConnectionService examConnectionService) {

        this.examDao = examDao;
        this.examConnectionService = examConnectionService;
        this.jsonMapper = new ObjectMapper();
    }

    /** SEB-Client authentication is the fist step on SEB-Client connection setup for a running exam.
     *
     * After successful authentication of the SEB-Client within the upstream-filter SEBClientAuthenticationFilter This
     * mapping gets an Authentication instance with a SEBClientAuth principal.
     *
     * This end-point correspond to point 3.a and b, 4. and 5. within the specification
     * (https://confluence.let.ethz.ch/x/uofQAQ)
     *
     * <pre>
     * case 3.a - 4. - 5.:
     *      The end-point responses with a list of running exams of the client's institution in JSON format.
     *      The SEB then presents the links for the exams to the examinee. Each link is pointing back
     *      to this end-point and sends the SEB-Client credentials and the identifier of the selects exam within.
     *      4. Examinees select the exam which they are supposed to participate in
     *      5. A link to the LMS exam will be sent along with a connection-token
     *
     * case of 3.b:
     *      5. A link to the overall LMS login page will be sent to the SEB-Client along with a connection-token
     * </pre>
     *
     * @param examId The identifier of the exam that was selected by an examinee
     * @param authentication Authentication instance with a SEBClientAuth principal
     * @return a list of currently running exams of the client's institution or a LMS login link */
    @RequestMapping(value = "/sebauth/sebhandshake", method = RequestMethod.POST)
    public ResponseEntity<String> sebHandshake(
            @RequestParam(name = EXAM_IDENTIFIER_KEY_NAME, required = false) final Long examId,
            final SEBConnectionAuth auth) {

        log.debug("SEB-Client hand-shake with ClientAuth: {}", auth);

        try {
            if (examId != null) {
                // user has already selected an exam from a previously sent list of exam links
                // the LMS link for specified exam is sent along with a connectionToken
                // Specification case 3.a. with point 4. and 5.

                log.debug("SEB-Client hand-shake case 3.a. on exam id: ", examId);

                final Exam exam = this.examDao.byId(examId);
                final String connectionToken = this.examConnectionService.handshakeSEBClient(
                        auth.clientAddress,
                        exam.id);

                return ResponseEntity.ok()
                        .header(CONNECTION_TOKEN_KEY_NAME, connectionToken)
                        .body(this.jsonMapper.writeValueAsString(exam.lmsExamURL));
            } else if (auth.lmsUrl == null) {
                // if no overall LMS URL is set on the specified seb-lms-setup,
                // this response with a list of currently running exams for selection
                // Specification case 3.a.

                log.debug("SEB-Client hand-shake case 3.a. no exam choosen. Send list of running exams ");

                final List<ExamLink> collect = this.examDao.getAll(
                        e -> e.status == ExamStatus.RUNNING &&
                                e.institutionId.longValue() == auth.institutionId.longValue())
                        .stream()
                        .map(exam -> new ExamLink(exam))
                        .collect(Collectors.toList());

                String jsonResponse;
                jsonResponse = this.jsonMapper.writeValueAsString(collect);
                return ResponseEntity.ok(jsonResponse);
            } else {
                // in this case the specified overall LMS login page URL (with auto-exam-scheduling) will be sent
                // to the SEB-client along with a connectionToken.
                // NOTE: in this case we additionally need a exam identifier from the later LMS handshake-request
                //       where the LMS also sends a user-identifier (Specification point 7.)
                // Specification case 3.b. with point 5.

                log.debug("SEB-Client hand-shake case 3.b. sending overal LMS login page");

                final String connectionToken = this.examConnectionService.handshakeSEBClient(
                        auth.clientAddress,
                        null);

                return ResponseEntity.ok()
                        .header(CONNECTION_TOKEN_KEY_NAME, connectionToken)
                        .body(this.jsonMapper.writeValueAsString(auth.lmsUrl));
            }
        } catch (final Exception e) {
            log.error("Unexpected Error while SEB handshake :", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /** The end-point of LMS hand-shake and confirmation of successful SEB-Client login on LMS side.
     *
     * This end-point implements point 6. and 7. within the specification (https://confluence.let.ethz.ch/x/uofQAQ)
     *
     * @param connectionToken The connection token created and supplied on SEB--hand-shake
     * @param userId The user-identifier that is sent by the LMS. See specification point 6
     * @param examId The identifier of the exam that was enrolled by the LMS (this is only required if case 3.b was
     *            applied before)
     * @param authentication */
    @RequestMapping(value = "/sebauth/lmshandshake", method = RequestMethod.POST)
    public ResponseEntity<Object> lmsHandshake(
            @RequestHeader(name = CONNECTION_TOKEN_KEY_NAME, required = true) final String connectionToken,
            @RequestHeader(name = USER_IDENTIFIER_KEY_NAME, required = true) final String userIdentifier,
            @RequestParam(name = EXAM_IDENTIFIER_KEY_NAME, required = false) final Long examId,
            final LMSConnectionAuth auth) {

        log.debug("LMS-Client hand-shake with ClientAuth: {}", auth);

        try {
            this.examConnectionService.handshakeLMSClient(
                    connectionToken,
                    userIdentifier,
                    examId);
        } catch (final Exception e) {
            log.error("Unexpected Error while LMS handshake :", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .build();
    }

    @SubscribeMapping("/sebauth/wsconnect")
    public String subscribe(@Header(CONNECTION_TOKEN_KEY_NAME) final String connectionToken) {

        log.debug("SEB-Client Web-Socket subscription");

        try {

            final Exam connectClientToExam = this.examConnectionService
                    .connectClientToExam(connectionToken);

            // TODO verify and get and send SEB-configuration for specified SEB-client

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

    private String messageToString(final Message message) {
        try {
            return this.jsonMapper.writeValueAsString(message);
        } catch (final JsonProcessingException e) {
            return e.getMessage();
        }
    }

}
