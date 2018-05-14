/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web;

import org.eth.demo.sebserver.domain.rest.ClientEvent;
import org.eth.demo.sebserver.service.ExamSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exam/clientevent")
public class ExamSessionController {

    @Autowired
    private ExamSessionService examSessionService;

    @RequestMapping(value = "/{examId}/{clientId}", method = RequestMethod.POST)
    final void deleteExam(@PathVariable final Long examId,
            @PathVariable final Long clientUUID,
            @RequestBody final ClientEvent clientEvent) {

        this.examSessionService.logClientEvent(examId, clientUUID, clientEvent);
    }

}
