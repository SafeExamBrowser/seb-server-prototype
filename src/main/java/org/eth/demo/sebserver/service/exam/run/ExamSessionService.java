/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.exam.run;

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
import org.eth.demo.sebserver.domain.rest.exam.ClientConnection;
import org.eth.demo.sebserver.domain.rest.exam.ClientConnection.ConnectionStatus;
import org.eth.demo.sebserver.domain.rest.exam.ClientEvent;
import org.eth.demo.sebserver.domain.rest.exam.Exam;
import org.eth.demo.sebserver.domain.rest.exam.IndicatorDefinition;
import org.eth.demo.sebserver.domain.rest.exam.IndicatorValue;
import org.eth.demo.sebserver.service.exam.ExamStateService;
import org.eth.demo.sebserver.service.exam.indicator.ClientIndicator;
import org.eth.demo.sebserver.service.exam.indicator.ClientIndicatorFactory;
import org.eth.demo.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class ExamSessionService implements ClientConnectionDelegate {

    private static final Logger log = LoggerFactory.getLogger(ExamSessionService.class);

    private final ExamStateService examStateService;
    private final ClientConnectionRecordMapper clientConnectionRecordMapper;
    private final EventHandlingStrategy eventHandlingStrategy;
    private final ClientIndicatorFactory clientIndicatorFactory;

    private final Map<String, ConnectionData> connectionCache = new ConcurrentHashMap<>();

    public ExamSessionService(
            final ExamStateService examStateService,
            final ClientConnectionRecordMapper clientConnectionRecordMapper,
            final EventHandlingStrategy eventHandlingStrategy,
            final ClientIndicatorFactory clientIndicatorFactory) {

        this.examStateService = examStateService;
        this.clientConnectionRecordMapper = clientConnectionRecordMapper;
        this.eventHandlingStrategy = eventHandlingStrategy;
        eventHandlingStrategy.setClientConnectionDelegate(this);
        this.clientIndicatorFactory = clientIndicatorFactory;
    }

    @Transactional
    public String handshakeSEBClient(final String clientAddress, final Long examId) {

        log.debug("Connection Handshake from SEB-Client. Checking integrity first");

        // Integrity check
        // NOTE: Maybe the clientAddress will not be enough to identify a connection-attempt
        //       Clarify if there is a proper way to identify a connection-attempt
        final List<ClientConnectionRecord> withSameClientAddress = this.clientConnectionRecordMapper
                .selectByExample()
                .where(ClientConnectionRecordDynamicSqlSupport.clientAddress, isEqualTo(clientAddress))
                .build()
                .execute();

        if (!withSameClientAddress.isEmpty()) {
            // Integrity constraint: Connections with same client address already exists
            // TODO clarify what to do in this case. For example:
            //      allow new connection and indicate Integrity constraint to the running exam dash-board
            log.warn("Integrity constraint: Connections with same client address already exists: {}",
                    withSameClientAddress);
        }

        // TODO maybe we need a stronger connectionToken but for now a simple UUID is used
        final String connectionToken = UUID.randomUUID().toString();

        final ClientConnectionRecord ccRecord = new ClientConnectionRecord(
                null,
                (examId != null) ? examId : null,
                ClientConnection.ConnectionStatus.CONNECTION_REQUESTED.name(),
                connectionToken,
                "[AWATING_LMS_CONFIRMATION [" + connectionToken + "]]",
                clientAddress);

        this.clientConnectionRecordMapper.insert(ccRecord);

        log.debug("Registered SEB-Client connection for {}", clientAddress);

        return connectionToken;
    }

    @Transactional
    public void handshakeLMSClient(
            final String connectionToken,
            final String userIdentifier,
            final Long examId) {

        log.debug("Connection Handshake from LMS-Client");

        Long connectionId = null;
        Long examIdToSet = null;
        try {
            final ClientConnectionRecord connection = getConnection(
                    connectionToken,
                    ClientConnection.ConnectionStatus.CONNECTION_REQUESTED);
            connectionId = connection.getId();

            if (examId == null && connection.getExamId() == null) {
                log.error("Missing exam identifier within LMS handshake");
                throw new IllegalArgumentException("Missing exam identifier within LMS handshake");
            }

            examIdToSet = (examId == null) ? connection.getExamId() : examId;

        } catch (final Exception e) {
            // TODO: This indicates some irregularity on SEB-Client connection attempt.
            //       Later we should handle this more accurately, and maybe indicate this to the monitoring board
            log.error("Unable to establish SEB-Client connection for {}", userIdentifier);
            throw new IllegalStateException("Unable to establish SEB-Client connection", e);
        }

        this.clientConnectionRecordMapper.updateByPrimaryKeySelective(new ClientConnectionRecord(
                connectionId,
                examIdToSet,
                ClientConnection.ConnectionStatus.AUTHENTICATED.name(),
                null,
                userIdentifier,
                null));

        log.debug("Established SEB-Client connection within LMS authentication {}", userIdentifier);
    }

    @Transactional
    public Exam connectClientToExam(final String connectionToken) {
        // TODO Integrity check and exception handling
        final ClientConnectionRecord connection = getConnection(
                connectionToken,
                ClientConnection.ConnectionStatus.AUTHENTICATED);

        return connectClientToExam(
                connection.getExamId(),
                connection.getUserIdentifier());
    }

    @Transactional
    public Exam connectClientToExam(final Long examId, final String userIdentifier) {
        checkRunningExam(examId);

        final Exam runningExam = this.examStateService.getRunningExam(examId);
        final ConnectionStatus expectedCurrentStatus = ClientConnection.ConnectionStatus.AUTHENTICATED;
        Long connectionId = null;
        try {
            connectionId = getConnectionId(userIdentifier, expectedCurrentStatus);
        } catch (final Exception e) {
            // TODO: This indicates some irregularity on SEB-Client connection attempt.
            //       Later we should handle this more accurately, and maybe indicate this to the monitoring board
            log.debug("Unable to connect SEB-Client {} to exam {}", userIdentifier, runningExam.name);
            throw new IllegalStateException("Unable to connect SEB-Client to exam");

        }

        log.debug("Connect client {} to exam {}", userIdentifier, runningExam.name);

        // create a ClientConnection POJO and store it within the cache
        final ConnectionData createClientConnection = createClientConnection(userIdentifier);
        this.connectionCache.put(userIdentifier, createClientConnection);

        // update connection-record status and invalidate connectionToken
        this.clientConnectionRecordMapper.updateByExampleSelective(new ClientConnectionRecord(
                connectionId,
                null,
                ClientConnection.ConnectionStatus.WEB_SOCKET_ESTABLISHED.name(),
                "[NO_TOKEN]",
                null, null));

        return runningExam;
    }

    public Collection<IndicatorValue> getIndicatorValues(final Long examId) {
        checkRunningExam(examId);

        return getConnectedClientIds(examId)
                .stream()
                .flatMap(this::indicatorValues)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public void notifyClientEvent(final ClientEvent event) {
        this.eventHandlingStrategy.accept(event);

        getClientConnection(event.userIdentifier)
                .ifPresent(cc -> cc.indicators()
                        .forEach(ci -> ci.notifyClientEvent(event)));
    }

    @Transactional
    public void closeConnection(final String userIdentifier, final boolean aborted) {
        if (!this.connectionCache.containsKey(userIdentifier)) {
            log.warn("No connection with clientIdentifier {} registered");
        }

        try {
            getConnectionId(userIdentifier, ClientConnection.ConnectionStatus.WEB_SOCKET_ESTABLISHED);
        } catch (final Exception e) {
            // TODO: This indicates some irregularity on SEB-Client connection attempt.
            //       Later we should handle this more accurately, and maybe indicate this to the monitoring board
            log.debug("Unable to close connection or mark as aborted {}", userIdentifier);
            throw new IllegalStateException("Unable to close connection or mark as aborted");

        }

        final ConnectionStatus state = (aborted)
                ? ClientConnection.ConnectionStatus.ABORTED
                : ClientConnection.ConnectionStatus.FINISHED;
        this.clientConnectionRecordMapper.updateByExampleSelective(
                new ClientConnectionRecord(null, null, state.name(), null, null, null));

        this.connectionCache.remove(userIdentifier);

        log.debug("Connection {} {}", (aborted) ? "aborted" : "closed", userIdentifier);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConnectionData> getClientConnection(final String userIdentifier) {
        if (!this.connectionCache.containsKey(userIdentifier)) {

            if (getActiveClientByToken(userIdentifier) != null) {
                final ConnectionData connectionData = createClientConnection(userIdentifier);
                this.connectionCache.put(userIdentifier, connectionData);
            }
        }

        if (this.connectionCache.containsKey(userIdentifier)) {
            return Optional.of(this.connectionCache.get(userIdentifier));
        } else {
            return Optional.empty();
        }
    }

    public Stream<IndicatorValue> indicatorValues(final String userIdentifier) {
        return getClientConnection(userIdentifier)
                .map(cc -> cc.valuesStream()
                        .map(ci -> new IndicatorValue(
                                cc.clientConnection.userIdentifier,
                                ci.getType(),
                                ci.getCurrentValue())))
                .orElse(Stream.empty());
    }

    public boolean checkActiveConnection(final String userIdentifier) {
        return getClientConnection(userIdentifier) != null;
    }

    private ClientConnectionRecord getActiveClientByToken(final String userIdentifier) {
        final List<ClientConnectionRecord> connections = this.clientConnectionRecordMapper.selectByExample()
                .where(ClientConnectionRecordDynamicSqlSupport.userIdentifier, isEqualTo(userIdentifier.toString()))
                .build()
                .execute();

        if (connections == null || connections.isEmpty()) {
            return null;
        }

        return connections.get(0);
    }

    private ConnectionData createClientConnection(final String userIdentifier) {
        final ClientConnectionRecord clientConnectionRecord = getActiveClientByToken(userIdentifier);
        final Exam runningExam = this.examStateService.getRunningExam(clientConnectionRecord.getExamId());
        final ConnectionData connectionData = new ConnectionData(
                ClientConnection.fromRecord(clientConnectionRecord),
                this.clientIndicatorFactory.createFor(
                        runningExam,
                        userIdentifier));
        return connectionData;
    }

    private Collection<String> getConnectedClientIds(final long examId) {
        return this.connectionCache
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().clientConnection.examId.longValue() == examId)
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
    }

    private Long getConnectionId(
            final String clientIdentifier,
            final ConnectionStatus expectedCurrentStatus) {

        return Utils.getSingle(this.clientConnectionRecordMapper.selectByExample()
                .where(ClientConnectionRecordDynamicSqlSupport.status,
                        isEqualTo(expectedCurrentStatus.name()))
                .and(ClientConnectionRecordDynamicSqlSupport.userIdentifier, isEqualTo(clientIdentifier))
                .build()
                .execute()).getId();
    }

    private void checkRunningExam(final Long examId) {
        if (!this.examStateService.isRunning(examId)) {
            log.error("No running exam found for id {}", examId);
            throw new IllegalStateException("Exam with id: " + examId + " is not running");
        }
    }

    private ClientConnectionRecord getConnection(
            final String connectionToken,
            final ConnectionStatus cStatus) {

        final ClientConnectionRecord connection =
                Utils.getSingle(this.clientConnectionRecordMapper.selectByExample()
                        .where(ClientConnectionRecordDynamicSqlSupport.status,
                                isEqualTo(cStatus.name()))
                        .and(ClientConnectionRecordDynamicSqlSupport.connectionToken,
                                isEqualTo(connectionToken))
                        .build()
                        .execute());
        return connection;
    }

    static final class ConnectionData {

        public final ClientConnection clientConnection;
        public final Map<IndicatorDefinition, ClientIndicator> indicatorMapping;

        public ConnectionData(
                final ClientConnection clientConnection,
                final Map<IndicatorDefinition, ClientIndicator> indicatorMapping) {

            this.clientConnection = clientConnection;
            this.indicatorMapping = indicatorMapping;
        }

        public Stream<ClientIndicator> valuesStream() {
            return this.indicatorMapping.values().stream();
        }

        public Stream<ClientIndicator> indicators() {
            return this.indicatorMapping.values().stream();
        }
    }

}
