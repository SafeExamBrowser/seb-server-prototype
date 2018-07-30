/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.exam.run;

import static org.eth.demo.sebserver.batis.gen.mapper.ExamRecordDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.isLessThan;

import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateUtils;
import org.eth.demo.sebserver.batis.gen.mapper.ExamRecordMapper;
import org.eth.demo.sebserver.batis.gen.model.ExamRecord;
import org.eth.demo.sebserver.domain.rest.exam.ExamStatus;
import org.eth.demo.util.TransactionalBounds;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class ExamRunner implements DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(ExamRunner.class);

    private final Executor executor;
    private final PlatformTransactionManager transactionManager;
    private final ExamRecordMapper examRecordMapper;

    private boolean running = false;

    public ExamRunner(
            final AsyncConfigurer asyncConfigurer,
            final PlatformTransactionManager transactionManager,
            final ExamRecordMapper examRecordMapper) {

        this.executor = asyncConfigurer.getAsyncExecutor();
        this.transactionManager = transactionManager;
        this.examRecordMapper = examRecordMapper;
    }

    @EventListener(ApplicationReadyEvent.class)
    protected void initAndRecover() {
        log.debug("Initialize ExamRunner");

        if (this.running) {
            throw new IllegalStateException("ExamRunner is already running!");
        }

        // TODO some validation on existing exam records if they are in valid states and have valid dates

        log.debug("starting ExamRunner");

        this.executor.execute(examRunner());
        this.running = true;

        log.debug("ExamRunner up and running");
    }

    private Runnable examRunner() {
        return () -> {
            while (this.running) {
                try {
                    Thread.sleep(5 * DateUtils.MILLIS_PER_SECOND);
                } catch (final InterruptedException e) {
                    log.error("Unexpected InterruptedException while Thread.sleep: " + e);
                }

                log.debug("Check extams to start");

                getExamsToStart()
                        .stream()
                        .forEach(this::startExam);

                log.debug("Check extams to finish");

                getExamsToFinish()
                        .stream()
                        .forEach(this::finishExam);
            }
        };
    }

    private void startExam(final Long examId) {
        log.debug("Start Exam with id: {}", id);
        saveExam(examId, ExamStatus.RUNNING);
    }

    private void finishExam(final Long examId) {
        log.debug("Stop Exam with id: {}", id);
        saveExam(examId, ExamStatus.FINISHED);
    }

    private void saveExam(final Long examId, final ExamStatus status) {
        new TransactionalBounds<>(this.transactionManager)
                .runInTransaction(() -> {
                    final ExamRecord stateChange = new ExamRecord(
                            examId, null, null,
                            status.name(), null, null, null);
                    this.examRecordMapper.updateByPrimaryKeySelective(stateChange);
                })
                .onError(t -> {
                    log.error("Unexpected Error while process sate change to {} on exam: {}", status, examId, t);
                    return null;
                }); // TODO error handling
    }

    private final Collection<Long> getExamsToStart() {
        final DateTime now = LocalDateTime.now()
                .toDateTime(DateTimeZone.UTC);

        return this.examRecordMapper.selectByExample()
                .where(status, isEqualTo(ExamStatus.READY.name()))
                .and(startTime, isLessThan(now))
                .build()
                .execute()
                .stream()
                .map(r -> r.getId())
                .collect(Collectors.toList());

    }

    private final Collection<Long> getExamsToFinish() {
        final DateTime now = LocalDateTime.now()
                .toDateTime(DateTimeZone.UTC);

        return this.examRecordMapper.selectByExample()
                .where(status, isEqualTo(ExamStatus.RUNNING.name()))
                .and(endTime, isLessThan(now))
                .build()
                .execute()
                .stream()
                .map(r -> r.getId())
                .collect(Collectors.toList());
    }

    @Override
    public void destroy() throws Exception {
        if (this.running) {
            this.running = false;
        }
    }

}
