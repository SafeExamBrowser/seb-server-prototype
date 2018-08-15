/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.exam.run;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.eth.demo.sebserver.appevents.ExamFinishedEvent;
import org.eth.demo.sebserver.appevents.ExamStartedEvent;
import org.eth.demo.sebserver.batis.gen.mapper.ClientEventRecordMapper;
import org.eth.demo.sebserver.batis.gen.mapper.ExamRecordDynamicSqlSupport;
import org.eth.demo.sebserver.batis.gen.mapper.ExamRecordMapper;
import org.eth.demo.sebserver.batis.gen.model.ClientEventRecord;
import org.eth.demo.sebserver.domain.rest.exam.ClientEvent;
import org.eth.demo.sebserver.domain.rest.exam.Exam.ExamStatus;
import org.eth.demo.util.Result;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

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
 * side as well as for the whole server system. */
@Lazy
@Component(EventHandlingStrategy.EVENT_CONSUMER_STRATEGY_ASYNC_BATCH_STORE)
public class AsyncBatchEventSaveStrategy implements EventHandlingStrategy {

    private static final Logger log = LoggerFactory.getLogger(AsyncBatchEventSaveStrategy.class);

    private static final int NUMBER_OF_WORKER_THREADS = 4;
    private static final int BATCH_SIZE = 100;

    private final SqlSessionFactory sqlSessionFactory;
    private final ExamRecordMapper examRecordMapper;
    private final Executor executor;
    private final TransactionTemplate transactionTemplate;

    private ClientConnectionDelegate clientConnectionDelegate;

    private final BlockingDeque<ClientEvent> eventQueue = new LinkedBlockingDeque<>();

    private boolean enabled = false;
    private boolean workersRunning = false;

    public AsyncBatchEventSaveStrategy(
            final SqlSessionFactory sqlSessionFactory,
            final ExamRecordMapper examRecordMapper,
            final AsyncConfigurer asyncConfigurer,
            final PlatformTransactionManager transactionManager) {

        this.sqlSessionFactory = sqlSessionFactory;
        this.examRecordMapper = examRecordMapper;
        this.executor = asyncConfigurer.getAsyncExecutor();

        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    @Override
    public void setClientConnectionDelegate(final ClientConnectionDelegate clientConnectionDelegate) {
        this.clientConnectionDelegate = clientConnectionDelegate;
    }

    @Override
    public void enable(final boolean enable) {
        this.enabled = enable;
    }

    @EventListener(ApplicationReadyEvent.class)
    protected void recover() {
        if (this.enabled) {
            runWorkers();
        }
    }

    @EventListener(ExamStartedEvent.class)
    protected void examStarted() {
        if (this.enabled && !this.workersRunning) {
            runWorkers();
        }
    }

    @EventListener(ExamFinishedEvent.class)
    protected void examFinished() {
        this.workersRunning = hasRunningExams();
    }

    @Override
    public void accept(final ClientEvent event) {
        if (!this.enabled) {
            log.warn("Received ClientEvent on none enabled AsyncBatchEventSaveStrategy. ClientEvent is ignored");
            return;
        }

        this.eventQueue.add(event);
    }

    private void runWorkers() {
        if (this.workersRunning) {
            log.warn("runWorkers called when workers are running already. Ignore that");
            return;
        }

        if (!hasRunningExams()) {
            log.info("runWorkers called but no exam is running");
            return;
        }

        this.workersRunning = true;

        log.info("Start {} Event-Batch-Store Worker-Threads", NUMBER_OF_WORKER_THREADS);
        for (int i = 0; i < NUMBER_OF_WORKER_THREADS; i++) {
            this.executor.execute(batchSave());
        }
    }

    private Runnable batchSave() {
        return () -> {

            log.debug("Worker Thread {} running", Thread.currentThread());

            final Collection<ClientEvent> events = new ArrayList<>();
            final SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(
                    this.sqlSessionFactory,
                    ExecutorType.BATCH);
            final ClientEventRecordMapper clientEventMapper = sqlSessionTemplate.getMapper(
                    ClientEventRecordMapper.class);
            final TransactionCallback<Result<Void>> batchStore = batchStore(
                    this.clientConnectionDelegate,
                    events,
                    clientEventMapper);

            try {
                while (this.workersRunning) {
                    events.clear();
                    this.eventQueue.drainTo(events, BATCH_SIZE);

                    if (!events.isEmpty()) {
                        this.transactionTemplate
                                .execute(batchStore)
                                .onError(t -> {
                                    log.error("Failed to batch store client events: {}", events);
                                    return null;
                                });
                    }

                    try {
                        Thread.sleep(20);
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                sqlSessionTemplate.close();
                log.debug("Worker Thread {} stopped", Thread.currentThread());
            }
        };
    }

    private static final TransactionCallback<Result<Void>> batchStore(
            final ClientConnectionDelegate clientConnectionDelegate,
            final Collection<ClientEvent> events,
            final ClientEventRecordMapper clientEventMapper) {

        return status -> {
            try {
                final List<ClientEventRecord> records = events.stream()
                        .map(event -> clientConnectionDelegate.getClientConnection(event.userIdentifier)
                                .map(cc -> event.toRecord(cc.clientConnection.examId))
                                .orElse(null))
                        .filter(cer -> cer != null)
                        .collect(Collectors.toList());

                for (final ClientEventRecord record : records) {
                    clientEventMapper.insert(record);
                }

                return Result.of(null);
            } catch (final Exception e) {
                log.error("Error while trying to batch store client-events: ", e);
                status.setRollbackOnly();
                return Result.ofError(e);
            }
        };
    }

    private boolean hasRunningExams() {
        return this.examRecordMapper.countByExample()
                .where(ExamRecordDynamicSqlSupport.status, isEqualTo(ExamStatus.RUNNING.name()))
                .build()
                .execute() > 0;
    }

}
