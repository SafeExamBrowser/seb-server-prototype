/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.web.socket;

import org.eth.demo.sebserver.service.ExamSessionService;
import org.eth.demo.sebserver.web.socket.Message.Type;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class ExamSessionController {

    private final ExamSessionService examSessionService;

    public ExamSessionController(final ExamSessionService examSessionService) {
        this.examSessionService = examSessionService;
    }

    @MessageMapping("/connect/{examId}")
    @SendToUser(value = "/queue/reply", broadcast = false)
    public Message connect(
            @DestinationVariable final long examId,
            final SimpMessageHeaderAccessor headerAccessor) {

        try {
            return new Message(Type.CONNECT, "{tocken=\"test\"}"
//                    this.examSessionService
//                            .connectClient(Long.valueOf(examId))
//                            .toString()
            );
        } catch (final Exception e) {
            return new Message(Type.ERROR, e.getMessage());
        }
    }

}
