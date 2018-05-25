/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web;

import java.util.UUID;

import org.eth.demo.sebserver.domain.rest.ClientEvent;
import org.eth.demo.sebserver.domain.rest.IndicatorValue;
import org.eth.demo.sebserver.service.ExamSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/exam")
public class ExamSessionController {

    @Autowired
    private ExamSessionService examSessionService;

    @RequestMapping(value = "/connect/{examId}", method = RequestMethod.GET)
    final Mono<String> connect(@PathVariable final Long examId) {
        return Mono.fromCallable(() -> this.examSessionService
                .connectClient(examId)
                .toString());
    }

    @RequestMapping(value = "/disconnect", method = RequestMethod.POST)
    final Mono<Void> disconnect(@RequestHeader(value = "Token") final String clientUUID) {
        return Mono.fromRunnable(() -> this.examSessionService
                .disconnectClient(UUID.fromString(clientUUID)));
    }

    @RequestMapping(value = "/event/{examId}", method = RequestMethod.POST)
    final Mono<Void> clientEvent(@RequestHeader(value = "Token") final String clientUUID,
            @RequestBody final ClientEvent clientEvent) {

        return Mono.fromRunnable(() -> {
            this.examSessionService.saveClientEvent(
                    UUID.fromString(clientUUID),
                    clientEvent);
        });
    }

    @RequestMapping(value = "/indicatorValues/{examId}", method = RequestMethod.GET)
    public Flux<IndicatorValue> indicatorValues(@PathVariable final Long examId) {
        return Flux.fromIterable(
                this.examSessionService.getIndicatorValues(examId));
    }

}
