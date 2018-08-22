/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.exam.run;

import static org.eth.demo.sebserver.batis.gen.mapper.SebLmsSetupRecordDynamicSqlSupport.lmsClientname;
import static org.eth.demo.sebserver.batis.gen.mapper.SebLmsSetupRecordDynamicSqlSupport.sebClientname;
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
import org.eth.demo.sebserver.batis.gen.mapper.SebLmsSetupRecordMapper;
import org.eth.demo.sebserver.batis.gen.model.ClientConnectionRecord;
import org.eth.demo.sebserver.batis.gen.model.SebLmsSetupRecord;
import org.eth.demo.sebserver.domain.rest.exam.ClientConnection;
import org.eth.demo.sebserver.domain.rest.exam.ClientConnection.ConnectionStatus;
import org.eth.demo.sebserver.domain.rest.exam.Exam;
import org.eth.demo.sebserver.domain.rest.exam.IndicatorDefinition;
import org.eth.demo.sebserver.service.exam.ExamStateService;
import org.eth.demo.sebserver.service.exam.indicator.ClientIndicator;
import org.eth.demo.sebserver.service.exam.indicator.ClientIndicatorFactory;
import org.eth.demo.sebserver.web.WebSecurityConfig.EncodingConfig;
import org.eth.demo.sebserver.web.clientauth.ClientConnectionAuth;
import org.eth.demo.sebserver.web.clientauth.ClientConnectionAuth.SEBWebSocketAuth;
import org.eth.demo.util.Result;
import org.eth.demo.util.Utils;
import org.mybatis.dynamic.sql.SqlColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExamConnectionService implements ClientConnectionDelegate {

    private static final Logger log = LoggerFactory.getLogger(ExamConnectionService.class);

    private final ExamStateService examStateService;
    private final ClientConnectionRecordMapper clientConnectionRecordMapper;
    private final SebLmsSetupRecordMapper sebLmsSetupRecordMapper;
    private final ClientIndicatorFactory clientIndicatorFactory;
    private final PasswordEncoder clientPasswordEncoder;

    private final Map<String, ConnectionData> connectionCache = new ConcurrentHashMap<>();

    public ExamConnectionService(
            final ExamStateService examStateService,
            final ClientConnectionRecordMapper clientConnectionRecordMapper,
            final SebLmsSetupRecordMapper sebLmsSetupRecordMapper,
            final ClientIndicatorFactory clientIndicatorFactory,
            @Qualifier(EncodingConfig.CLIENT_PASSWORD_ENCODER_BEAN_NAME) final PasswordEncoder clientPasswordEncoder) {

        this.examStateService = examStateService;
        this.clientConnectionRecordMapper = clientConnectionRecordMapper;
        this.sebLmsSetupRecordMapper = sebLmsSetupRecordMapper;
        this.clientIndicatorFactory = clientIndicatorFactory;
        this.clientPasswordEncoder = clientPasswordEncoder;
    }

    @Transactional
    public String handshakeSEBClient(final String clientAddress, final Long examId) {

        log.debug("Connection Handshake from SEB-Client. Checking integrity first");

        // Integrity check: in case examId is provided is the specified exam running?
        if (examId != null && !this.examStateService.getRunningExam(examId).isPresent()) {
            log.error("The exam {} is not running", examId);
            throw new IllegalStateException("The exam " + examId + " is not running");
        }
        ;

        // Integrity check: is there already a connection with the same remote address registered?
        // NOTE: Maybe the clientAddress will not be enough to identify a connection-attempt
        //       Clarify if there is a proper way to identify a connection-attempt
        final List<ClientConnectionRecord> withSameClientAddress = this.clientConnectionRecordMapper
                .selectByExample()
                .where(ClientConnectionRecordDynamicSqlSupport.clientAddress, isEqualTo(clientAddress))
                .build()
                .execute();

        if (!withSameClientAddress.isEmpty()) {
            // Integrity violation: Connections with same client address already exists
            // TODO clarify what to do in this case. For example:
            //      allow new connection and indicate Integrity constraint to the running exam dash-board
            log.warn("Integrity constraint: Connections with same client address already exists: {}",
                    withSameClientAddress);
        }

        // TODO maybe we need a stronger connectionToken but for now a simple UUID is used
        final String connectionToken = UUID.randomUUID().toString();

        final ClientConnectionRecord ccRecord = new ClientConnectionRecord(
                null,
                examId,
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

        // Integrity check: Is there already a connection with the same userIdentifier?
        // TODO check if there is already a connection with the given userIdentifier
        //      if so and the connection is in ESTABLISHED state, deny the connection attempt and also notify to LMS if possible?
        //      if so and the connection is in RELEASED state, allow the connection
        //      if so and the connection is in some different state, we have to specify what to do, deny the connection attempt

        final ClientConnectionRecord connection = getConnectionByToken(
                connectionToken,
                ClientConnection.ConnectionStatus.CONNECTION_REQUESTED)
                        .onError(t -> {
                            // TODO: This indicates some irregularity on SEB-Client connection attempt.
                            //       Later we should handle this more accurately, and maybe indicate this to the monitoring board
                            log.error("Unable to establish SEB-Client connection for {}", userIdentifier);
                            throw new IllegalStateException("Unable to establish SEB-Client connection", t);
                        });

        if (examId == null && connection.getExamId() == null) {
            log.error("Missing exam identifier within LMS handshake");
            throw new IllegalArgumentException("Missing exam identifier within LMS handshake");
        }

        this.clientConnectionRecordMapper.updateByPrimaryKeySelective(new ClientConnectionRecord(
                connection.getId(),
                examId,
                ClientConnection.ConnectionStatus.AUTHENTICATED.name(),
                null,
                userIdentifier,
                null));

        log.debug("Established SEB-Client connection within LMS authentication {}", userIdentifier);
    }

    @Transactional
    public ClientConnectionAuth verifyWebSocketConnectionAuthentication(final String connectionToken) {
        final ClientConnectionRecord connection = getConnectionByToken(
                connectionToken,
                ClientConnection.ConnectionStatus.AUTHENTICATED)
                        .onError(t -> {
                            log.error("Unable to find connection for token: {}", connectionToken, t);
                            return null;
                        });

        return SEBWebSocketAuth.sebWebSocketAuthOf(connection);
    }

    @Transactional
    public Exam establishConnection(final SEBWebSocketAuth auth) {
        final Optional<Exam> exam = this.examStateService.getRunningExam(auth.examId);
        if (!exam.isPresent()) {
            log.error("The exam {} is not running. Unable to connect user with identifier {} ",
                    auth.examId,
                    auth.userIdentifier);
            throw new IllegalStateException("The exam " + auth.examId + " is not running");
        }

        final Exam runningExam = exam.get();
        final Long connectionId = getConnectionId(
                auth.userIdentifier,
                ClientConnection.ConnectionStatus.AUTHENTICATED)
                        .onError(t -> {
                            // TODO: This indicates some irregularity on SEB-Client connection attempt.
                            //       Later we should handle this more accurately, and maybe indicate this to the monitoring board
                            //       For example; check if there is already a connection for the userIdentifier and
                            //       if true in which state it is.
                            log.debug("Unable to connect SEB-Client {} to exam {}",
                                    auth.userIdentifier,
                                    runningExam.name);
                            throw new IllegalStateException("Unable to connect SEB-Client to exam");
                        });

        log.debug("Connect client {} to exam {}", auth.userIdentifier, runningExam.name);

        // update connection-record status and invalidate connectionToken
        this.clientConnectionRecordMapper.updateByPrimaryKeySelective(new ClientConnectionRecord(
                connectionId,
                null,
                ClientConnection.ConnectionStatus.ESTABLISHED.name(),
                null,
                null, null));

        // create a ClientConnection POJO and store it within the cache
        createClientConnection(auth.userIdentifier)
                .ifPresent(connectionData -> this.connectionCache.put(
                        auth.userIdentifier,
                        connectionData));

        return runningExam;
    }

    @Transactional(readOnly = true)
    public boolean hasClosedOrAbortedConnection(final SEBWebSocketAuth auth) {
        final Optional<ConnectionData> clientConnection = getClientConnection(auth.userIdentifier);
        return clientConnection
                .map(cd -> cd.clientConnection.status == ConnectionStatus.CLOSED ||
                        cd.clientConnection.status == ConnectionStatus.ABORTED)
                .orElse(false);
    }

    @Transactional
    public void closeConnection(final SEBWebSocketAuth auth, final boolean aborted) {
        if (!this.connectionCache.containsKey(auth.userIdentifier)) {
            log.warn("No connection with clientIdentifier {} registered");
        }
        // Integrity check: Connection available in expected state?
        final Long connectionId = getConnectionId(
                auth.userIdentifier,
                ClientConnection.ConnectionStatus.ESTABLISHED)
                        .onError(t -> {
                            // TODO: This indicates some irregularity on SEB-Client connection attempt.
                            //       Later we should handle this more accurately, and maybe indicate this to the monitoring board
                            log.error("Unable to close connection or mark as aborted {}", auth.userIdentifier);
                            throw new IllegalStateException("Unable to close connection or mark as aborted", t);
                        });

        final ConnectionStatus state = (aborted)
                ? ClientConnection.ConnectionStatus.ABORTED
                : ClientConnection.ConnectionStatus.CLOSED;
        this.clientConnectionRecordMapper.updateByPrimaryKeySelective(
                new ClientConnectionRecord(connectionId, null, state.name(), null, null, null));

        // update cache
        if (this.connectionCache.containsKey(auth.userIdentifier)) {
            this.connectionCache.remove(auth.userIdentifier);
            getClientConnection(auth.userIdentifier);
        }

        log.debug("Connection {} {}", (aborted) ? "aborted" : "closed", auth.userIdentifier);
    }

    public Result<SebLmsSetupRecord> getLMSSetup(
            final String clientname,
            final String secret,
            final boolean byLMSClient) {

        final SqlColumn<String> clientNameColumn = (byLMSClient) ? lmsClientname : sebClientname;
        return Utils.getSingle(this.sebLmsSetupRecordMapper.selectByExample()
                .where(clientNameColumn, isEqualTo(clientname))
                .build()
                .execute()
                .stream()
                .filter(record -> this.clientPasswordEncoder.matches(
                        secret,
                        (byLMSClient) ? record.getLmsClientsecret() : record.getSebClientsecret()))
                .collect(Collectors.toList()));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConnectionData> getClientConnection(final String userIdentifier) {
        if (!this.connectionCache.containsKey(userIdentifier)) {
            if (getActiveClientByToken(userIdentifier) != null) {
                createClientConnection(userIdentifier)
                        .ifPresent(clientData -> this.connectionCache.put(userIdentifier, clientData));
            }
        }

        if (this.connectionCache.containsKey(userIdentifier)) {
            return Optional.of(this.connectionCache.get(userIdentifier));
        } else {
            return Optional.empty();
        }
    }

    @Transactional(readOnly = true)
    public Result<ClientConnectionRecord> getConnectionByToken(
            final String connectionToken,
            final ConnectionStatus cStatus) {

        return Utils.getSingle(this.clientConnectionRecordMapper.selectByExample()
                .where(ClientConnectionRecordDynamicSqlSupport.status,
                        isEqualTo(cStatus.name()))
                .and(ClientConnectionRecordDynamicSqlSupport.connectionToken,
                        isEqualTo(connectionToken))
                .build()
                .execute());
    }

    public Collection<ConnectionData> getConnectionInfo(final Long examId) {
        return this.connectionCache
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().clientConnection.examId.longValue() == examId)
                .map(entry -> entry.getValue())
                .collect(Collectors.toList());
    }

    private Optional<ConnectionData> createClientConnection(final String userIdentifier) {
        return getActiveClientByToken(userIdentifier)
                .map(clientConnectionRecord -> {
                    return this.examStateService.getRunningExam(clientConnectionRecord.getExamId())
                            .map(runningExam -> {
                                return Optional.of(new ConnectionData(
                                        ClientConnection.fromRecord(clientConnectionRecord),
                                        this.clientIndicatorFactory.createFor(
                                                runningExam,
                                                userIdentifier)));
                            }).orElse(Optional.empty());
                }).orElse(Optional.empty());
    }

    private Result<Long> getConnectionId(
            final String clientIdentifier,
            final ConnectionStatus expectedCurrentStatus) {

        return Utils.getSingle(this.clientConnectionRecordMapper.selectByExample()
                .where(ClientConnectionRecordDynamicSqlSupport.status,
                        isEqualTo(expectedCurrentStatus.name()))
                .and(ClientConnectionRecordDynamicSqlSupport.userIdentifier, isEqualTo(clientIdentifier))
                .build()
                .execute())
                .map(t -> t.getId());
    }

    private Optional<ClientConnectionRecord> getActiveClientByToken(final String userIdentifier) {
        final List<ClientConnectionRecord> connections = this.clientConnectionRecordMapper.selectByExample()
                .where(ClientConnectionRecordDynamicSqlSupport.userIdentifier, isEqualTo(userIdentifier.toString()))
                .build()
                .execute();

        if (connections == null || connections.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(connections.get(0));
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
