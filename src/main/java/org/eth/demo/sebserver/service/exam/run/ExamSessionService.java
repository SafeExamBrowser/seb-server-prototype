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
import java.util.stream.Stream;

import org.eth.demo.sebserver.batis.gen.mapper.ClientConnectionRecordMapper;
import org.eth.demo.sebserver.domain.rest.exam.ClientEvent;
import org.eth.demo.sebserver.domain.rest.exam.IndicatorValue;
import org.eth.demo.sebserver.service.exam.ExamStateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class ExamSessionService {

    private static final Logger log = LoggerFactory.getLogger(ExamSessionService.class);

    private final ExamConnectionService examConnectionService;
    private final ExamStateService examStateService;
    private final EventHandlingStrategy eventHandlingStrategy;

    public ExamSessionService(
            final ExamConnectionService examConnectionService,
            final ExamStateService examStateService,
            final ClientConnectionRecordMapper clientConnectionRecordMapper,
            final EventHandlingStrategy eventHandlingStrategy) {

        this.examConnectionService = examConnectionService;
        this.examStateService = examStateService;
        this.eventHandlingStrategy = eventHandlingStrategy;
    }

    public Collection<IndicatorValue> getIndicatorValues(final Long examId) {
        if (!this.examStateService.getRunningExam(examId).isPresent()) {
            log.error("The exam {} is not running");
            return Collections.emptyList();
        }

        return this.examConnectionService.getConnectedClientIds(examId)
                .stream()
                .flatMap(this::indicatorValues)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public void notifyClientEvent(final ClientEvent event) {
        this.eventHandlingStrategy.accept(event);

        // notify client indicators for update
        this.examConnectionService.getClientConnection(event.getAuth().userIdentifier)
                .ifPresent(cc -> cc.indicators()
                        .forEach(ci -> ci.notifyClientEvent(event)));
    }

    public Stream<IndicatorValue> indicatorValues(final String userIdentifier) {
        return this.examConnectionService.getClientConnection(userIdentifier)
                .map(cc -> cc.valuesStream()
                        .map(ci -> new IndicatorValue(
                                cc.clientConnection.userIdentifier,
                                ci.getType(),
                                ci.getCurrentValue())))
                .orElse(Stream.empty());
    }

    public boolean checkActiveConnection(final String userIdentifier) {
        return this.examConnectionService.getClientConnection(userIdentifier) != null;
    }

}
