/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eth.demo.sebserver.domain.ClientConnection;
import org.eth.demo.sebserver.domain.rest.Exam;
import org.eth.demo.sebserver.domain.rest.IndicatorValue;
import org.eth.demo.sebserver.service.indicator.ClientConnectionFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
public class ClientConnectionService {

    private final ClientConnectionFactory clientConnectionFactory;
    private final ExamStateService examStateService;

    public ClientConnectionService(
            final ClientConnectionFactory clientConnectionFactory,
            final ExamStateService examStateService) {

        this.clientConnectionFactory = clientConnectionFactory;
        this.examStateService = examStateService;
    }

    // TODO: this state should go the persistent store
    private final Map<UUID, ActiveConnectionRecord> activeConnections = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    private final Map<UUID, ClientConnection> connectionCache = new ConcurrentHashMap<>();

    public UUID establishConnection(final Exam runningExam) {

        final Long newId = this.idCounter.incrementAndGet();
        final UUID clientUUID = UUID.randomUUID();

        // TODO later we have to store an active connection record for the client here
        this.activeConnections.put(
                clientUUID,
                new ActiveConnectionRecord(clientUUID, runningExam.id, newId));

        final ClientConnection clientConnection = createClientConnection(clientUUID);
        this.connectionCache.put(clientUUID, clientConnection);

        return clientUUID;
    }

    public void disposeConnection(final UUID clientUUID) {
        if (!this.connectionCache.containsKey(clientUUID)) {
            throw new IllegalStateException("Client with UUID: " + clientUUID + " is not registered");
        }

        // TODO later w have to delete the active connection record for the client here
        this.activeConnections.remove(clientUUID);
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

    public Optional<ClientConnection> getClientConnection(final UUID clientUUID) {
        if (!this.connectionCache.containsKey(clientUUID)) {
            // TODO later we have to check here if the client as an active connection record and if so, create and cache
            //      a specified ClientConnection
            if (this.activeConnections.containsKey(clientUUID)) {
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

    private ClientConnection createClientConnection(final UUID clientUUID) {
        final ActiveConnectionRecord activeConnectionRecord = this.activeConnections.get(clientUUID);
        final Exam runningExam = this.examStateService.getRunningExam(activeConnectionRecord.examId);
        final ClientConnection clientConnection = this.clientConnectionFactory.create(
                runningExam.id,
                activeConnectionRecord.clientId,
                clientUUID,
                runningExam);
        return clientConnection;
    }

    private static final class ActiveConnectionRecord {
        @SuppressWarnings("unused")
        public final UUID clientUUID;
        public final Long examId;
        public final Long clientId;

        public ActiveConnectionRecord(final UUID clientUUID, final Long examId, final Long clientId) {
            super();
            this.clientUUID = clientUUID;
            this.examId = examId;
            this.clientId = clientId;
        }
    }

}
