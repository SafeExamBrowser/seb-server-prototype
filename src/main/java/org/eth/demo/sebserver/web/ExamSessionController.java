/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web;

import java.util.Map;
import java.util.UUID;

import org.eth.demo.sebserver.domain.rest.ClientEvent;
import org.eth.demo.sebserver.domain.rest.IndicatorInfo;
import org.eth.demo.sebserver.service.ExamSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exam")
public class ExamSessionController {

    @Autowired
    private ExamSessionService examSessionService;

    @RequestMapping(value = "/connect/{examId}", method = RequestMethod.GET)
    final String connect(@PathVariable final Long examId) {
        return this.examSessionService
                .connectClient(examId)
                .toString();
    }

    @RequestMapping(value = "/disconnect", method = RequestMethod.POST)
    final void disconnect(@RequestHeader(value = "Token") final String clientUUID) {
        this.examSessionService.disconnectClient(UUID.fromString(clientUUID));
    }

    @RequestMapping(value = "/event/{examId}", method = RequestMethod.POST)
    final void clientEvent(@PathVariable final Long examId,
            @RequestHeader(value = "Token") final String clientUUID,
            @RequestBody final ClientEvent clientEvent) {

        this.examSessionService.logClientEvent(
                examId,
                UUID.fromString(clientUUID),
                clientEvent);
    }

    @RequestMapping(value = "/status/{examId}", method = RequestMethod.GET)
    public Map<UUID, Map<String, IndicatorInfo>> statusReport(@PathVariable final Long examId) {
        return this.examSessionService.getStatusReport(examId);
    }

}
