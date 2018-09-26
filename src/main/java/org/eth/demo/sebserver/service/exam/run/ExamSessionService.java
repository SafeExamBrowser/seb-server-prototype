/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.exam.run;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.eth.demo.sebserver.batis.gen.mapper.ClientConnectionRecordMapper;
import org.eth.demo.sebserver.domain.rest.exam.ClientEvent;
import org.eth.demo.sebserver.domain.rest.exam.ConnectionInfo;
import org.eth.demo.sebserver.domain.rest.exam.IndicatorValue;
import org.eth.demo.sebserver.service.exam.ExamDao;
import org.eth.demo.sebserver.service.exam.run.ExamConnectionService.ConnectionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class ExamSessionService {

    private static final Logger log = LoggerFactory.getLogger(ExamSessionService.class);

    private final ExamConnectionService examConnectionService;
    private final ExamDao examDao;
    private final EventHandlingStrategy eventHandlingStrategy;

    public ExamSessionService(
            final ExamConnectionService examConnectionService,
            final ExamDao examDao,
            final ClientConnectionRecordMapper clientConnectionRecordMapper,
            final EventHandlingStrategy eventHandlingStrategy) {

        this.examConnectionService = examConnectionService;
        this.examDao = examDao;
        this.eventHandlingStrategy = eventHandlingStrategy;
    }

    public Collection<ConnectionInfo> getConnectionInfo(final Long examId) {
        if (!this.examDao.runningExam(examId).isPresent()) {
            log.error("The exam {} is not running");
            return Collections.emptyList();
        }

        return this.examConnectionService.getActiveConnectionData(examId)
                .stream()
                .map(data -> new ConnectionInfo(
                        data.clientConnection.userIdentifier,
                        data.clientConnection.status.name(),
                        data.clientConnection.clientAddress,
                        indicatorValues(data)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public void notifyClientEvent(final ClientEvent event) {
        this.eventHandlingStrategy.accept(event);

        // notify client indicators for update
        this.examConnectionService.getActiveClientConnection(event.getAuth().connectionId)
                .ifPresent(cc -> cc.indicators()
                        .forEach(ci -> ci.notifyClientEvent(event)));
    }

    public Collection<IndicatorValue> indicatorValues(final ConnectionData data) {
        return data.valuesStream()
                .map(ci -> new IndicatorValue(
                        ci.getType(),
                        ci.getCurrentValue()))
                .collect(Collectors.toList());
    }

}
