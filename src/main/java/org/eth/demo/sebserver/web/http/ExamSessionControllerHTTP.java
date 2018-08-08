/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.http;

import java.util.UUID;

import org.eth.demo.sebserver.domain.rest.exam.ClientEvent;
import org.eth.demo.sebserver.domain.rest.exam.IndicatorValue;
import org.eth.demo.sebserver.service.exam.run.ExamSessionService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Deprecated // NOTE: As we decided to use WebSocket protocol for SEB-Client connection, this has been deprecated
@RestController
@RequestMapping("/exam")
public class ExamSessionControllerHTTP {

    private static final String REQUEST_HEADER_KEY_TOKEN = "Token";
    private final ExamSessionService examSessionService;

    public ExamSessionControllerHTTP(final ExamSessionService examSessionService) {
        this.examSessionService = examSessionService;
    }

    @RequestMapping(value = "/connect/{examId}", method = RequestMethod.GET)
    public final Mono<String> connect(@PathVariable final Long examId) {
        return Mono.fromCallable(() -> this.examSessionService
                .connectClient(examId)
                .toString());
    }

    @RequestMapping(value = "/disconnect", method = RequestMethod.POST)
    public final Mono<Void> disconnect(@RequestHeader(value = REQUEST_HEADER_KEY_TOKEN) final String clientUUID) {
        return Mono.fromRunnable(() -> this.examSessionService
                .disconnectClient(UUID.fromString(clientUUID)));
    }

    @RequestMapping(value = "/event/{examId}", method = RequestMethod.POST)
    public final Mono<Void> clientEvent(
            @RequestHeader(value = REQUEST_HEADER_KEY_TOKEN) final String clientUUID,
            @RequestBody final ClientEvent clientEvent) {

        return Mono.fromRunnable(() -> {
            this.examSessionService.notifyClientEvent(clientEvent);
        });
    }

    @RequestMapping(value = "/indicatorValues/{examId}", method = RequestMethod.GET)
    public Flux<IndicatorValue> indicatorValues(@PathVariable final Long examId) {
        return Flux.fromIterable(
                this.examSessionService.getIndicatorValues(examId));
    }

}
