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
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eth.demo.sebserver.appevents.ExamFinishedEvent;
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
import org.eth.demo.sebserver.web.clientauth.ClientConnectionAuth.SEBWebSocketAuth;
import org.eth.demo.util.Result;
import org.eth.demo.util.Utils;
import org.mybatis.dynamic.sql.SqlColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExamConnectionService {

    private static final Logger log = LoggerFactory.getLogger(ExamConnectionService.class);

    private final ExamStateService examStateService;
    private final ClientConnectionRecordMapper clientConnectionRecordMapper;
    private final SebLmsSetupRecordMapper sebLmsSetupRecordMapper;
    private final ClientIndicatorFactory clientIndicatorFactory;
    private final PasswordEncoder clientPasswordEncoder;

    private final Map<Long, ConnectionData> activeConnectionCache = new ConcurrentHashMap<>();

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

    @EventListener(ExamFinishedEvent.class)
    protected void examFinished(final ExamFinishedEvent event) {
        final Long examId = (Long) event.getSource();
        this.activeConnectionCache.values()
                .removeIf(cd -> cd.clientConnection.examId.longValue() == examId.longValue());
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
        updateCache(ccRecord.getId());

        log.debug("Registered SEB-Client connection for {}", clientAddress);

        return connectionToken;
    }

    @Transactional
    public void handshakeLMSClient(
            final String connectionToken,
            final String userIdentifier,
            final Long examId) {

        log.debug("Connection Handshake from LMS-Client");

        // Integrity check: Is there already a connection with the same userIdentifier for the specified exam?
        // TODO check if there is already a connection with the given userIdentifier
        //      if so and the connection is in ESTABLISHED state, deny the connection attempt and also notify to LMS if possible?
        //      if so and the connection is in RELEASED state, allow the connection
        //      if so and the connection is in some different state, we have to specify what to do, deny the connection attempt
        final List<ClientConnectionRecord> userConnectionRecords = this.clientConnectionRecordMapper.selectByExample()
                .where(ClientConnectionRecordDynamicSqlSupport.userIdentifier, isEqualTo(userIdentifier))
                .and(ClientConnectionRecordDynamicSqlSupport.examId, isEqualTo(examId))
                .build()
                .execute();

        if (userConnectionRecords.size() > 0) {
            final EnumSet<ConnectionStatus> states = userConnectionRecords.stream()
                    .reduce(
                            EnumSet.noneOf(ConnectionStatus.class),
                            (set, rec) -> {
                                set.add(ConnectionStatus.valueOf(rec.getStatus()));
                                return set;
                            },
                            (set1, set2) -> {
                                set1.addAll(set2);
                                return set1;
                            });
            if (states.contains(ConnectionStatus.RELEASED) && states.size() == 1) {
                log.info(
                        "It seems that there is already one connection for the specified user but the connection is/was released");
            } else {
                log.error(
                        "Invalid connection attempt. There is already a connection for the specified user and exam and the connection is not released --> Handshake is blocked.");
                throw new IllegalStateException("Invalid connection attempt");
            }
        }

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

        updateCache(connection.getId());

        log.debug("Established SEB-Client connection within LMS authentication {}", userIdentifier);
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

        updateCache(connectionId);

        return runningExam;
    }

    @Transactional
    public void closeConnection(final SEBWebSocketAuth auth, final boolean aborted) {
        if (!this.activeConnectionCache.containsKey(auth.connectionId)) {
            log.warn("No active connection with connectionId {} registered", auth.connectionId);
            return;
        }

        final ConnectionData connectionData = this.activeConnectionCache.get(auth.connectionId);

        // Integrity check: Connection available in expected state?
        final ConnectionStatus currentStatus = connectionData.clientConnection.status;
        if (currentStatus != ConnectionStatus.ESTABLISHED) {
            if (currentStatus == ConnectionStatus.CLOSED && aborted) {
                // NOTE: This case is expected, when after a normal un-subscription the disconnection is following
                //       and intercepted by the ConnectionListener. In this case the connection stays CLOSED
                return;
            }
            // TODO: This indicates some irregularity on SEB-Client connection attempt.
            //       Later we should handle this more accurately, and maybe indicate this to the monitoring board
            log.error("The connection is in a illegal state for closing");
            throw new IllegalStateException("The connection is in a illegal state for closing");
        }

        final ConnectionStatus state = (aborted)
                ? ClientConnection.ConnectionStatus.ABORTED
                : ClientConnection.ConnectionStatus.CLOSED;
        this.clientConnectionRecordMapper.updateByPrimaryKeySelective(
                new ClientConnectionRecord(auth.connectionId, null, state.name(), null, null, null));

        updateCache(auth.connectionId);

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

    @Transactional(readOnly = true)
    public Optional<ConnectionData> getActiveClientConnection(final Long connectionId) {
        if (!this.activeConnectionCache.containsKey(connectionId)) {
            updateCache(connectionId);
        }

        if (this.activeConnectionCache.containsKey(connectionId)) {
            return Optional.of(this.activeConnectionCache.get(connectionId));
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
        return this.activeConnectionCache
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().clientConnection.examId.longValue() == examId)
                .map(entry -> entry.getValue())
                .collect(Collectors.toList());
    }

    private void updateCache(final Long connectionId) {
        activeConnectionDataById(connectionId).ifPresent(
                cd -> this.activeConnectionCache.put(cd.clientConnection.id, cd));
    }

    /** Creates a ConnectionData from specified connection-record-id if and only if the connection record refers to a
     * currently running exam.
     *
     * @param connectionId
     * @return */
    private Optional<ConnectionData> activeConnectionDataById(final Long connectionId) {
        return Optional.ofNullable(this.clientConnectionRecordMapper.selectByPrimaryKey(connectionId))
                .map(clientConnectionRecord -> {
                    return this.examStateService.getRunningExam(clientConnectionRecord.getExamId())
                            .map(runningExam -> {
                                return Optional.of(new ConnectionData(
                                        ClientConnection.fromRecord(clientConnectionRecord),
                                        this.clientIndicatorFactory.createFor(
                                                runningExam,
                                                connectionId)));
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
