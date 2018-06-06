/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service;

import static org.eth.demo.sebserver.batis.gen.mapper.ExamRecordDynamicSqlSupport.examRecord;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import java.util.HashMap;
import java.util.Map;

import org.eth.demo.sebserver.batis.gen.mapper.ExamRecordMapper;
import org.eth.demo.sebserver.batis.gen.model.ExamRecord;
import org.eth.demo.sebserver.domain.rest.Exam;
import org.eth.demo.sebserver.domain.rest.Exam.Status;
import org.eth.demo.sebserver.service.dao.ExamDao;
import org.eth.demo.sebserver.service.events.ExamFinishedEvent;
import org.eth.demo.sebserver.service.events.ExamStartedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Lazy
@Service
public class ExamStateService {

    private static final Logger log = LoggerFactory.getLogger(ExamStateService.class);

    private final ExamRecordMapper examRecordMapper;
    private final ExamDao examDao;
    private final ApplicationEventPublisher publisher;

    private final Map<Long, Exam> runningExamsCache = new HashMap<>();

    public ExamStateService(
            final ExamRecordMapper examRecordMapper,
            final ExamDao examDao,
            final ApplicationEventPublisher publisher) {

        this.examRecordMapper = examRecordMapper;
        this.examDao = examDao;
        this.publisher = publisher;
    }

    @Transactional
    public Exam processStateChange(final Long examId, final Exam.Status toState) {
        final ExamRecord examRecord = this.examRecordMapper.selectByPrimaryKey(examId);
        final Status currentStatus = Exam.Status.byId(examRecord.getStatus());

        switch (toState) {
            case IN_PROGRESS: {
                if (currentStatus != Status.READY) {
                    invalidStateException(toState, currentStatus);
                }

                save(examId, toState);
                log.info("Exam: {} is now back to edit", examId);
                return this.examDao.byId(examId);
            }
            case READY: {
                if (currentStatus != Status.IN_PROGRESS) {
                    invalidStateException(toState, currentStatus);
                }
                save(examId, toState);
                log.info("Exam: {} is now ready to run", examId);
                return this.examDao.byId(examId);
            }
            case RUNNING: {
                if (currentStatus != Status.READY) {
                    invalidStateException(toState, currentStatus);
                }
                save(examId, toState);
                final Exam exam = this.examDao.byId(examId);
                this.runningExamsCache.put(examId, exam);

                log.info("Exam: {} is now running", examId);
                this.publisher.publishEvent(new ExamStartedEvent(exam));

                return exam;
            }
            case FINISHED: {
                if (currentStatus != Status.RUNNING) {
                    invalidStateException(toState, currentStatus);
                }
                save(examId, toState);
                this.runningExamsCache.remove(examId);

                log.info("Exam: {} is now finished", examId);
                this.publisher.publishEvent(new ExamFinishedEvent(examId));

                return this.examDao.byId(examId);
            }
            default: {
                throw new RuntimeException("Invalid state change request form: " + currentStatus.displayName
                        + " to: " + toState.displayName);
            }
        }

    }

    @Transactional(readOnly = true)
    public boolean isRunning(final Long id) {
        if (this.runningExamsCache.containsKey(id)) {
            return true;
        }

        return cacheRunningExam(id);
    }

    public Exam getRunningExam(final long examId) {
        if (!this.runningExamsCache.containsKey(examId)) {
            throw new IllegalArgumentException("Exam with id: " + examId + " is not running");
        }

        return this.runningExamsCache.get(examId);
    }

    private boolean cacheRunningExam(final Long id) {
        final Long count = this.examRecordMapper.countByExample()
                .where(examRecord.id, isEqualTo(id))
                .and(examRecord.status, isEqualTo(Exam.Status.RUNNING.id))
                .build()
                .execute();

        if (count <= 0) {
            return false;
        }

        this.runningExamsCache.put(id, this.examDao.byId(id));
        return true;
    }

    private void save(final Long examId, final Exam.Status toState) {
        this.examRecordMapper.updateByPrimaryKeySelective(new ExamRecord(examId, null, toState.id));
    }

    private void invalidStateException(final Exam.Status toState, final Status currentStatus) {
        throw new RuntimeException("Invalid state change request form: " + currentStatus.displayName
                + " to: " + toState.displayName);
    }

}
