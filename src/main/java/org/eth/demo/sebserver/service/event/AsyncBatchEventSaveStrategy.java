/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.eth.demo.sebserver.batis.gen.mapper.ClientEventRecordMapper;
import org.eth.demo.sebserver.batis.gen.model.ClientEventRecord;
import org.eth.demo.sebserver.domain.rest.ClientEvent;
import org.eth.demo.sebserver.service.ClientConnectionService;

/** Approach 2 to handle/save client events internally
 *
 * This Approach uses a queue to collect ClientEvents that are stored later. The queue is shared between some
 * worker-threads that batch gets and stores the events from the queue afterwards. this approach is less blocking from
 * the caller perspective and also faster on store data by using bulk-insert
 *
 * A disadvantage is an potentially multiple event data loss on total server fail. The data in the queue is state that
 * is not stored somewhere yet and can't be recovered on total server fail.
 *
 * If the performance of this approach is not enough or the potentially data loss on total server fail is a risk that
 * not can be taken, we have to consider using a messaging system/server like rabbitMQ or Apache-Kafka that brings the
 * ability to effectively store and recover message queues but also comes with more complexity on setup and installation
 * side as well as for the whole server system.
 *
 * TODO trigger the run of worker threads on exam start and stop them if no exam is currently running */
public class AsyncBatchEventSaveStrategy implements EventHandlingStrategy {

    public static final int BATCH_SIZE = 100;

    private final ClientConnectionService clientConnectionService;
    private final SqlSessionFactory sqlSessionFactory;
    private final Executor executor;

    private final BlockingDeque<ClientEvent> eventQueue = new LinkedBlockingDeque<>();

    public AsyncBatchEventSaveStrategy(
            final ClientConnectionService clientConnectionService,
            final SqlSessionFactory sqlSessionFactory,
            final Executor executor) {

        this.clientConnectionService = clientConnectionService;
        this.sqlSessionFactory = sqlSessionFactory;
        this.executor = executor;

        executor.execute(batchSave());
        executor.execute(batchSave());
        executor.execute(batchSave());
        executor.execute(batchSave());
    }

    @Override
    public void accept(final ClientEvent event) {
        this.eventQueue.add(event);
    }

    // TODO add missing transaction
    private Runnable batchSave() {
        return () -> {
            final Collection<ClientEvent> events = new ArrayList<>();
            final SqlSession batchedSession = this.sqlSessionFactory.openSession(ExecutorType.BATCH, false);

            try {
                while (true) {
                    events.clear();
                    this.eventQueue.drainTo(events, 100);

                    final List<ClientEventRecord> records = events.stream()
                            .map(event -> this.clientConnectionService.getClientConnection(event.clientId)
                                    .map(cc -> event.toRecord(
                                            cc.examId,
                                            cc.clientId))
                                    .orElse(null))
                            .filter(cer -> cer != null)
                            .collect(Collectors.toList());

                    final ClientEventRecordMapper mapper = batchedSession.getMapper(ClientEventRecordMapper.class);
                    for (final ClientEventRecord record : records) {
                        mapper.insert(record);
                    }
                    batchedSession.commit();

                    try {
                        Thread.sleep(20);
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                batchedSession.close();
            }
        };
    }

}
