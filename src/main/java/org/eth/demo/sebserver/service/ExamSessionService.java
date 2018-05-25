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
import java.util.stream.Collectors;

import org.eth.demo.sebserver.batis.gen.mapper.ClientEventRecordMapper;
import org.eth.demo.sebserver.batis.gen.model.ClientEventRecord;
import org.eth.demo.sebserver.domain.ClientConnection;
import org.eth.demo.sebserver.domain.rest.ClientEvent;
import org.eth.demo.sebserver.domain.rest.Exam;
import org.eth.demo.sebserver.domain.rest.IndicatorValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExamSessionService {

    private final ClientEventRecordMapper clientEventRecordMapper;
    private final ExamStateService examStateService;
    private final ClientConnectionService clientConnectionService;

    public ExamSessionService(final ClientEventRecordMapper clientEventRecordMapper,
            final ExamStateService examStateService,
            final ClientConnectionService clientConnectionService) {

        this.clientEventRecordMapper = clientEventRecordMapper;
        this.examStateService = examStateService;
        this.clientConnectionService = clientConnectionService;
    }

    public UUID connectClient(final Long examId) {
        if (!this.examStateService.isRunning(examId)) {
            throw new IllegalStateException("Exam with id: " + examId + " is not running");
        }

        final Exam runningExam = this.examStateService.getRunningExam(examId);

        return this.clientConnectionService.establishConnection(runningExam);
    }

    public void disconnectClient(final UUID clientUUID) {
        System.out.println("********************** disconnectClient: " + clientUUID);
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

    @Transactional
    public void logClientEvent(final UUID clientUUID, final ClientEvent event) {
        if (!this.clientConnectionService.checkActiveConnection(clientUUID)) {
            throw new IllegalStateException("Client with UUID: " + clientUUID + " is not registered");
        }

        final ClientConnection clientConnection = this.clientConnectionService.getClientConnection(clientUUID);
        final ClientEventRecord record = event.toRecord(
                clientConnection.examId,
                clientConnection.clientId);
        this.clientEventRecordMapper.insert(record);
    }

}
