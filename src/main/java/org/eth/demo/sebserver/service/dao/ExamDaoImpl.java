/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.dao;

import static org.eth.demo.sebserver.batis.gen.mapper.IndicatorRecordDynamicSqlSupport.examId;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eth.demo.sebserver.batis.ExamIndicatorJoinMapper;
import org.eth.demo.sebserver.batis.gen.mapper.ExamRecordMapper;
import org.eth.demo.sebserver.batis.gen.mapper.IndicatorRecordMapper;
import org.eth.demo.sebserver.batis.gen.model.ExamRecord;
import org.eth.demo.sebserver.batis.gen.model.IndicatorRecord;
import org.eth.demo.sebserver.domain.rest.exam.Exam;
import org.eth.demo.sebserver.service.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Lazy
@Component
public class ExamDaoImpl implements ExamDao {

    private static final Logger log = LoggerFactory.getLogger(ExamDaoImpl.class);

    private final ExamRecordMapper examMapper;
    private final IndicatorRecordMapper indicatorMapper;
    private final ExamIndicatorJoinMapper examIndicatorJoinMapper;

    public ExamDaoImpl(
            final ExamRecordMapper examMapper,
            final IndicatorRecordMapper indicatorMapper,
            final ExamIndicatorJoinMapper examIndicatorJoinMapper) {

        super();
        this.examMapper = examMapper;
        this.indicatorMapper = indicatorMapper;
        this.examIndicatorJoinMapper = examIndicatorJoinMapper;
    }

    // TODO It should also be possible to use a Join here...
    // NOTE Joins are a bit more complicated to implement and cannot be generated directly.
    //      A join example is implements within getAll using ExamIndicatorJoinMapper
    //      We should decide in case if it makes sense to use join. usually if a there
    //      are a lot of rows to fetch we better implement and use a join to perform better.
    /*
     * (non-Javadoc)
     * 
     * @see org.eth.demo.sebserver.service.dao.ExamDao#byId(java.lang.Long)
     */
    @Transactional(readOnly = true)
    @Override
    public Exam byId(final Long id) {
        final ExamRecord record = this.examMapper.selectByPrimaryKey(id);
        if (record == null) {
            log.info("Exam with id {} requested but not exists", id);
            throw new ResourceNotFoundException("Exam", String.valueOf(id));
        }

        final List<IndicatorRecord> indicators = this.indicatorMapper.selectByExample()
                .where(examId, isEqualTo(id))
                .build()
                .execute();

        return Exam.fromRecord(record, indicators);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eth.demo.sebserver.service.dao.ExamDao#getAll()
     */
    @Transactional(readOnly = true)
    @Override
    public Collection<Exam> getAll() {
        return this.examIndicatorJoinMapper.selectAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eth.demo.sebserver.service.dao.ExamDao#getAll(java.util.function.Predicate)
     */
    @Transactional(readOnly = true)
    @Override
    public Collection<Exam> getAll(final Predicate<Exam> predicate) {
        return getAll().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eth.demo.sebserver.service.dao.ExamDao#save(org.eth.demo.sebserver.domain.rest.exam.Exam)
     */
    @Transactional
    @Override
    public Exam save(final Exam model) {
        Long id;
        if (model.getId() == null) {
            final ExamRecord record = model.toRecord();
            this.examMapper.insert(record);
            id = record.getId();
        } else {
            this.examMapper.updateByPrimaryKeySelective(model.toRecord());

            // NOTE: delete all existing indicators for this exam. The new ones form request gets inserted after
            this.indicatorMapper.deleteByExample()
                    .where(examId, isEqualTo(model.id))
                    .build()
                    .execute();

            id = model.id;
        }

        // save Indicators
        model.getIndicators().stream()
                .map(indicator -> indicator.toRecord(id))
                .forEach(this.indicatorMapper::insert);

        return byId(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eth.demo.sebserver.service.dao.ExamDao#delete(java.lang.Long)
     */
    @Transactional
    @Override
    public boolean delete(final Long id) {
        this.indicatorMapper.deleteByExample()
                .where(examId, isEqualTo(id))
                .build()
                .execute();

        final int del = this.examMapper.deleteByPrimaryKey(id);
        return del > 0;
    }

}
