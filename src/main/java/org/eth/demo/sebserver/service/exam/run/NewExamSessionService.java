/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.exam.run;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.eth.demo.sebserver.batis.gen.mapper.ClientConnectionRecordDynamicSqlSupport;
import org.eth.demo.sebserver.batis.gen.mapper.ClientConnectionRecordMapper;
import org.eth.demo.sebserver.batis.gen.model.ClientConnectionRecord;
import org.eth.demo.sebserver.domain.ClientConnection;
import org.eth.demo.sebserver.domain.rest.exam.ClientEvent;
import org.eth.demo.sebserver.domain.rest.exam.Exam;
import org.eth.demo.sebserver.domain.rest.exam.SEBClientAuth;
import org.eth.demo.sebserver.service.exam.ExamStateService;
import org.eth.demo.util.Utils;
import org.springframework.transaction.annotation.Transactional;

public class NewExamSessionService {

    private final ExamStateService examStateService;
    private final ClientConnectionFactory clientConnectionFactory;
    private final ClientConnectionRecordMapper clientConnectionRecordMapper;

    private final Consumer<ClientEvent> clientEventConsumer;
    private final boolean indicatorValueCaching;
    private final Map<Long, ClientConnection> connectionCache = new ConcurrentHashMap<>();

    public NewExamSessionService(
            final ExamStateService examStateService,
            final ClientConnectionFactory clientConnectionFactory,
            final ClientConnectionRecordMapper clientConnectionRecordMapper,
            final Consumer<ClientEvent> clientEventConsumer,
            final boolean indicatorValueCaching) {

        this.examStateService = examStateService;
        this.clientConnectionFactory = clientConnectionFactory;
        this.clientConnectionRecordMapper = clientConnectionRecordMapper;
        this.clientEventConsumer = clientEventConsumer;
        this.indicatorValueCaching = indicatorValueCaching;
    }

    @Transactional
    public Long registerConnection(final SEBClientAuth sebClientAuth) {
        final ClientConnectionRecord ccRecord = new ClientConnectionRecord(
                null,
                null,
                ClientConnection.ConnectionStatus.CONNECTION_REQUESTED.name(),
                sebClientAuth.connectionToken,
                "[AWATING_LMS_CONFIRMATION]",
                sebClientAuth.clientAddress);

        this.clientConnectionRecordMapper.insert(ccRecord);
        return ccRecord.getId();
    }

    @Transactional
    public Exam connectClientToExam(
            final Long examId,
            final String username,
            final String connectionToken) {

        if (!this.examStateService.isRunning(examId)) {
            throw new IllegalStateException("Exam with id: " + examId + " is not running");
        }

        final Exam runningExam = this.examStateService.getRunningExam(examId);
        final List<ClientConnectionRecord> result = this.clientConnectionRecordMapper.selectByExample()
                .where(ClientConnectionRecordDynamicSqlSupport.status,
                        isEqualTo(ClientConnection.ConnectionStatus.AUTHENTICATED.name()))
                .and(ClientConnectionRecordDynamicSqlSupport.connectionToken, isEqualTo(connectionToken))
                .build()
                .execute();

        final ClientConnectionRecord clientConnectionRecord = Utils.getSingle(result);

        // TODO create a ClientConnection POJO and store it within the cache

        // TODO update connection-record status and invalidate connectionToken

        return runningExam;
    }

}
