/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eth.demo.sebserver.batis.gen.mapper.ClientConnectionRecordDynamicSqlSupport;
import org.eth.demo.sebserver.batis.gen.mapper.ClientConnectionRecordMapper;
import org.eth.demo.sebserver.batis.gen.model.ClientConnectionRecord;
import org.eth.demo.sebserver.domain.ClientConnection;
import org.eth.demo.sebserver.domain.rest.exam.Exam;
import org.eth.demo.sebserver.domain.rest.exam.IndicatorValue;
import org.eth.demo.sebserver.service.indicator.ClientConnectionFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Lazy
@Service
public class ClientConnectionService {

    private final ClientConnectionFactory clientConnectionFactory;
    private final ExamStateService examStateService;
    private final ClientConnectionRecordMapper clientConnectionRecordMapper;

    public ClientConnectionService(
            final ClientConnectionFactory clientConnectionFactory,
            final ExamStateService examStateService,
            final ClientConnectionRecordMapper clientConnectionRecordMapper) {

        this.clientConnectionFactory = clientConnectionFactory;
        this.examStateService = examStateService;
        this.clientConnectionRecordMapper = clientConnectionRecordMapper;
    }

    private final Map<UUID, ClientConnection> connectionCache = new ConcurrentHashMap<>();

    @Transactional
    public UUID establishConnection(final Exam runningExam) {

        final UUID clientUUID = UUID.randomUUID();
        final ClientConnectionRecord ccRecord = new ClientConnectionRecord(null, runningExam.id, clientUUID.toString());
        this.clientConnectionRecordMapper.insert(ccRecord);

        final ClientConnection clientConnection = createClientConnection(clientUUID);
        this.connectionCache.put(clientUUID, clientConnection);

        return clientUUID;
    }

    @Transactional
    public void disposeConnection(final UUID clientUUID) {
        if (!this.connectionCache.containsKey(clientUUID)) {
            throw new IllegalStateException("Client with UUID: " + clientUUID + " is not registered");
        }

        final ClientConnection clientConnection = this.connectionCache.get(clientUUID);

        this.clientConnectionRecordMapper.updateByPrimaryKey(
                new ClientConnectionRecord(clientConnection.clientId, clientConnection.examId, ""));

        this.connectionCache.remove(clientUUID);
    }

    public Collection<UUID> getConnectedClientUUIDs(final Long examId) {
        return this.connectionCache
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().examId == examId)
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ClientConnection> getClientConnection(final UUID clientUUID) {
        if (!this.connectionCache.containsKey(clientUUID)) {

            if (getActiveClientByToken(clientUUID) != null) {
                final ClientConnection clientConnection = createClientConnection(clientUUID);
                this.connectionCache.put(clientUUID, clientConnection);
            }
        }

        if (this.connectionCache.containsKey(clientUUID)) {
            return Optional.of(this.connectionCache.get(clientUUID));
        } else {
            return Optional.empty();
        }
    }

    public Stream<IndicatorValue> indicatorValues(final UUID clientUUID) {
        return getClientConnection(clientUUID)
                .map(cc -> cc.valuesStream()
                        .map(ci -> new IndicatorValue(
                                cc.clientUUID,
                                ci.getType(),
                                ci.getCurrentValue())))
                .orElse(Stream.empty());
    }

    public boolean checkActiveConnection(final UUID clientUUID) {
        return getClientConnection(clientUUID) != null;
    }

    private ClientConnectionRecord getActiveClientByToken(final UUID clientUUID) {
        final List<ClientConnectionRecord> connections = this.clientConnectionRecordMapper.selectByExample()
                .where(ClientConnectionRecordDynamicSqlSupport.token, isEqualTo(clientUUID.toString()))
                .build()
                .execute();

        if (connections == null || connections.isEmpty()) {
            return null;
        }

        return connections.get(0);
    }

    private ClientConnection createClientConnection(final UUID clientUUID) {
        final ClientConnectionRecord clientConnectionRecord = getActiveClientByToken(clientUUID);
        final Exam runningExam = this.examStateService.getRunningExam(clientConnectionRecord.getExamId());
        final ClientConnection clientConnection = this.clientConnectionFactory.create(
                runningExam.id,
                clientConnectionRecord.getId(),
                clientUUID,
                runningExam);
        return clientConnection;
    }

}
