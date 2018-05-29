/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.eth.demo.sebserver.domain.rest.ClientEvent;
import org.eth.demo.sebserver.domain.rest.Exam;
import org.eth.demo.sebserver.domain.rest.IndicatorValue;
import org.springframework.stereotype.Service;

@Service
public class ExamSessionService {

    private final ExamStateService examStateService;
    private final ClientConnectionService clientConnectionService;

    private final Consumer<ClientEvent> clientEventConsumer;
    private final boolean indicatorValueCaching;

    public ExamSessionService(
            final ExamStateService examStateService,
            final ClientConnectionService clientConnectionService,
            final Consumer<ClientEvent> clientEventConsumer,
            final boolean indicatorValueCaching) {

        this.examStateService = examStateService;
        this.clientConnectionService = clientConnectionService;
        this.clientEventConsumer = clientEventConsumer;
        this.indicatorValueCaching = indicatorValueCaching;
    }

    public UUID connectClient(final Long examId) {
        if (!this.examStateService.isRunning(examId)) {
            throw new IllegalStateException("Exam with id: " + examId + " is not running");
        }

        final Exam runningExam = this.examStateService.getRunningExam(examId);

        return this.clientConnectionService.establishConnection(runningExam);
    }

    public void disconnectClient(final UUID clientUUID) {
        this.clientConnectionService.disposeConnection(clientUUID);
    }

    public Collection<IndicatorValue> getIndicatorValues(final Long examId) {
        if (!this.examStateService.isRunning(examId)) {
            throw new IllegalStateException("Exam with id: " + examId + " is not running");
        }

        return this.clientConnectionService
                .getConnectedClientUUIDs(examId)
                .stream()
                .flatMap(this.clientConnectionService::indicatorValues)
                .collect(Collectors.toList());

    }

    public void notifyClientEvent(final ClientEvent event) {
        this.clientEventConsumer.accept(event);

        if (this.indicatorValueCaching) {
            this.clientConnectionService.getClientConnection(event.clientId)
                    .ifPresent(cc -> cc.indicators()
                            .forEach(ci -> ci.notifyClientEvent(event)));
        }
    }

}
