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
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.eth.demo.sebserver.domain.rest.ClientEvent;
import org.eth.demo.sebserver.domain.rest.Exam;
import org.eth.demo.sebserver.domain.rest.IndicatorValue;
import org.springframework.stereotype.Service;

@Service
public class ExamSessionService {

    private final ExamStateService examStateService;
    private final ClientConnectionService clientConnectionService;

    private final Consumer<ClientEvent> clientEventConsumer;

    public ExamSessionService(
            final ExamStateService examStateService,
            final ClientConnectionService clientConnectionService,
            final Consumer<ClientEvent> clientEventConsumer) {

        this.examStateService = examStateService;
        this.clientConnectionService = clientConnectionService;
        this.clientEventConsumer = clientEventConsumer;
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

    public void notifyClientEvent(final ClientEvent event) {
        this.clientEventConsumer.accept(event);
    }

//    /** Approach 1 to handle/save client events internally
//     *
//     * This saves a client event to persistence. With the @Async annotation spring executes every call in an own thread
//     * from a ThreadPool. This gives some performance-boost but if there are a lot of clients connected, firing events
//     * at small intervals like 100ms, this is blocking to much because every event is saved within its own SQL commit
//     * and also transactional.
//     *
//     * An advantage of this approach is minimal data loss on server fail.
//     *
//     * @param clientUUID The clients UUID
//     * @param event the client event data POJO */
//    @Transactional
//    @Async
//    public void saveClientEvent(final UUID clientUUID, final ClientEvent event) {
//        try {
//            if (!this.clientConnectionService.checkActiveConnection(clientUUID)) {
//                throw new IllegalStateException("Client with UUID: " + clientUUID + " is not registered");
//            }
//
//            final int saved = this.clientConnectionService
//                    .getClientConnection(clientUUID)
//                    .map(cc -> this.clientEventRecordMapper.insert(event.toRecord(
//                            cc.examId,
//                            cc.clientId)))
//                    .orElse(0);
//
//            if (saved < 1) {
//                System.out.println("######################## missing connection: " + clientUUID);
//            } else {
////              System.out.println("************************* saveClientEvent on Thread: " + Thread.currentThread());
//            }
//        } catch (final Exception e) {
//            log.error("Unexpected error while trying to save client event: {}, {} : ", clientUUID, event, e);
//        }
//    }
//
//    @Autowired
//    private AsyncBatchEventSaveStrategy asyncBatchEventSaveService;
//
//    /** Approach 2 to handle/save client events internally
//     *
//     * This Approach uses AsyncBatchEventSaveService to store the client event in a queue. The queue is shared between
//     * some worker-threads that batch gets and stores the events from the queue afterwards. this approach is less
//     * blocking from the caller perspective and also faster on store data by using bulk-insert
//     *
//     * A disadvantage is an eventually multiple event data loss on total server fail. The data in the queue is state
//     * that is not stored somewhere yet and can't be recovered on total server fail.
//     *
//     * If this performance is not enough or the potentially data loss on total server fail is a risk that not can be
//     * taken, we have to consider using a messaging system/server like rabbitMQ or Apache-Kafka that bring the ability
//     * to effectively store and recover message queues but also comes with more complexity on setup and installation
//     * side as well as for the whole server system.
//     *
//     * @param clientUUID The clients UUID
//     * @param event the client event data POJO */
//    public void sendClientEvent(final UUID clientUUID, final ClientEvent event) {
//        this.asyncBatchEventSaveService.add(event);
//    }

}
