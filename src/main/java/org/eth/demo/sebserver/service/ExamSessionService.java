/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eth.demo.sebserver.batis.gen.mapper.ClientEventRecordMapper;
import org.eth.demo.sebserver.batis.gen.model.ClientEventRecord;
import org.eth.demo.sebserver.domain.rest.ClientEvent;
import org.eth.demo.sebserver.domain.rest.Exam;
import org.eth.demo.sebserver.domain.rest.IndicatorInfo;
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
        this.clientConnectionService.disposeConnection(clientUUID);
    }

    public Map<UUID, Map<String, IndicatorInfo>> getStatusReport(final Long examId) {
        if (!this.examStateService.isRunning(examId)) {
            throw new IllegalStateException("Exam with id: " + examId + " is not running");
        }

        final Map<UUID, Map<String, IndicatorInfo>> result = new LinkedHashMap<>();
        for (final UUID uuid : this.clientConnectionService.getConnectedClientUUIDs(examId)) {
            result.put(uuid, getIndicatorInfo(examId, uuid));
        }

        return result;
    }

    public Map<String, IndicatorInfo> getIndicatorInfo(final Long examId, final UUID clientUUID) {
        return this.clientConnectionService
                .getClientConnection(examId, clientUUID)
                .getIndicatorInfo()
                .stream()
                .collect(Collectors.toMap(
                        IndicatorInfo::getName,
                        Function.identity()));
    }

    @Transactional
    public void logClientEvent(final Long examId,
            final UUID clientUUID,
            final ClientEvent event) {

        if (!this.examStateService.isRunning(examId)) {
            throw new IllegalStateException("Exam with id: " + examId + " is not running");
        }

        if (!this.clientConnectionService.checkActiveConnection(examId, clientUUID)) {
            throw new IllegalStateException("Client with UUID: " + clientUUID + " is not registered");
        }

        final ClientEventRecord record = event.toRecord(
                examId,
                this.clientConnectionService.getActiveClientPK(examId, clientUUID));
        this.clientEventRecordMapper.insert(record);
    }

}
