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
import org.eth.demo.util.Result;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class ExamRunner implements DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(ExamRunner.class);

    private final Executor executor;
    private final TransactionTemplate transactionTemplate;
    private final SqlSessionTemplate sqlSessionTemplate;

    private boolean running = false;

    public ExamRunner(
            final AsyncConfigurer asyncConfigurer,
            final PlatformTransactionManager transactionManager,
            final SqlSessionTemplate sqlSessionTemplate) {

        this.executor = asyncConfigurer.getAsyncExecutor();
        this.sqlSessionTemplate = sqlSessionTemplate;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    @EventListener(ApplicationReadyEvent.class)
    protected void initAndRecover() {
        log.debug("Initialize ExamRunner");

        if (this.running) {
            throw new IllegalStateException("ExamRunner is already running!");
        }

        // TODO some validation on existing exam records if they are in valid states and have valid dates

        log.debug("starting ExamRunner");

        this.executor.execute(examUpdate());
        this.running = true;

        log.debug("ExamRunner up and running");
    }

    private Runnable examUpdate() {
        final ExamRecordMapper examMapper = this.sqlSessionTemplate.getMapper(ExamRecordMapper.class);
        final ExamUpdateMapper examUpdateMapper = new ExamUpdateMapper(examMapper);
        final TransactionCallback<Result<Void>> updateExams = updateExams(examUpdateMapper);

        return () -> {
            while (this.running) {
                try {
                    Thread.sleep(5 * DateUtils.MILLIS_PER_SECOND);
                    this.transactionTemplate
                            .execute(updateExams)
                            .onError(t -> {
                                log.error("Error while trying to automatically update exam status", t);
                                return null;
                            });
                } catch (final Throwable t) {
                    log.error("Unexpected Exception while updating exams: " + t);
                }
            }
        };
    }

    private static final TransactionCallback<Result<Void>> updateExams(final ExamUpdateMapper examUpdateMapper) {
        return status -> {
            try {
                examUpdateMapper.getExamsToStart()
                        .stream()
                        .forEach(examUpdateMapper::startExam);

                log.debug("Check extams to finish");

                examUpdateMapper.getExamsToFinish()
                        .stream()
                        .forEach(examUpdateMapper::finishExam);

                return Result.of(null);
            } catch (final Exception e) {
                log.error("Error while trying to batch store client-events: ", e);
                status.setRollbackOnly();
                return Result.ofError(e);
            }
        };
    }

    @Override
    public void destroy() throws Exception {
        if (this.running) {
            this.running = false;
        }
    }

    private final class ExamUpdateMapper {

        private final ExamRecordMapper examMapper;

        ExamUpdateMapper(final ExamRecordMapper examMapper) {
            this.examMapper = examMapper;
        }

        void startExam(final Long examId) {
            log.debug("Start Exam with id: {}", id);
            updateExam(examId, ExamStatus.RUNNING);
        }

        void finishExam(final Long examId) {
            log.debug("Stop Exam with id: {}", id);
            updateExam(examId, ExamStatus.FINISHED);
        }

        void updateExam(final Long examId, final ExamStatus status) {
            final ExamRecord stateChange = new ExamRecord(
                    examId,
                    null, null, null,
                    status.name(),
                    null, null, null);

            this.examMapper.updateByPrimaryKeySelective(stateChange);
        }

        Collection<Long> getExamsToStart() {
            final DateTime now = LocalDateTime.now()
                    .toDateTime(DateTimeZone.UTC);

            return this.examMapper.selectByExample()
                    .where(status, isEqualTo(ExamStatus.READY.name()))
                    .and(startTime, isLessThan(now))
                    .build()
                    .execute()
                    .stream()
                    .map(r -> r.getId())
                    .collect(Collectors.toList());
        }

        Collection<Long> getExamsToFinish() {
            final DateTime now = LocalDateTime.now()
                    .toDateTime(DateTimeZone.UTC);

            return this.examMapper.selectByExample()
                    .where(status, isEqualTo(ExamStatus.RUNNING.name()))
                    .and(endTime, isLessThan(now))
                    .build()
                    .execute()
                    .stream()
                    .map(r -> r.getId())
                    .collect(Collectors.toList());
        }
    }

}
