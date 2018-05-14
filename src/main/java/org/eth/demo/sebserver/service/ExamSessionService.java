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

import org.eth.demo.sebserver.batis.gen.mapper.ClientEventRecordMapper;
import org.eth.demo.sebserver.batis.gen.mapper.ExamRecordMapper;
import org.eth.demo.sebserver.batis.gen.model.ExamRecord;
import org.eth.demo.sebserver.domain.rest.ClientEvent;
import org.eth.demo.sebserver.domain.rest.Exam;
import org.eth.demo.sebserver.service.dao.ExamDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExamSessionService {

    private final ClientEventRecordMapper clientEventRecordMapper;
    private final ExamRecordMapper examRecordMapper;
    private final ExamDao examDao;

    private final Map<Long, Exam> runningExamsCache = new HashMap<>();

    public ExamSessionService(final ClientEventRecordMapper clientEventRecordMapper,
            final ExamRecordMapper examRecordMapper,
            final ExamDao examDao) {

        super();
        this.clientEventRecordMapper = clientEventRecordMapper;
        this.examRecordMapper = examRecordMapper;
        this.examDao = examDao;
    }

    @Transactional
    public void startExam(final Long id) {
        final Long count = this.examRecordMapper.countByExample()
                .where(examRecord.id, isEqualTo(id))
                .and(examRecord.status, isEqualTo(Exam.Status.READY.id))
                .build()
                .execute();

        if (count <= 0) {
            throw new RuntimeException("No Exam in READY state with id: " + id);
        }

        this.examRecordMapper.updateByPrimaryKeySelective(
                new ExamRecord(id, null, Exam.Status.RUNNING.id, null));

        if (!cacheRunningExam(id)) {
            throw new RuntimeException("Unexpected Error while trying to cache started Exam; id: " + id);
        }
    }

    @Transactional
    public void endExam(final Long id) {
        if (!isRunning(id)) {
            return;
        }

        this.examRecordMapper.updateByPrimaryKeySelective(
                new ExamRecord(id, null, Exam.Status.FINISHED.id, null));

        this.runningExamsCache.remove(id);
    }

    @Transactional(readOnly = true)
    public boolean isRunning(final Long id) {
        if (this.runningExamsCache.containsKey(id)) {
            return true;
        }

        return cacheRunningExam(id);
    }

    @Transactional
    public void logClientEvent(final Long examId,
            final Long clientUUID,
            final ClientEvent event) {

        if (!isRunning(examId)) {
            return;
        }

        this.clientEventRecordMapper.insert(event.toRecord(examId, clientUUID));
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

}
