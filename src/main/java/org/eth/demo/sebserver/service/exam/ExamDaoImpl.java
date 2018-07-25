/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.service.exam;

import static org.eth.demo.sebserver.batis.gen.mapper.IndicatorRecordDynamicSqlSupport.examId;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eth.demo.sebserver.batis.ExamJoinMapper;
import org.eth.demo.sebserver.batis.gen.mapper.ExamRecordMapper;
import org.eth.demo.sebserver.batis.gen.mapper.IndicatorRecordMapper;
import org.eth.demo.sebserver.batis.gen.model.ExamRecord;
import org.eth.demo.sebserver.domain.rest.exam.Exam;
import org.mybatis.dynamic.sql.select.MyBatis3SelectModelAdapter;
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
    private final ExamJoinMapper examJoinMapper;

    public ExamDaoImpl(
            final ExamRecordMapper examMapper,
            final IndicatorRecordMapper indicatorMapper,
            final ExamJoinMapper examJoinMapper) {

        super();
        this.examMapper = examMapper;
        this.indicatorMapper = indicatorMapper;
        this.examJoinMapper = examJoinMapper;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eth.demo.sebserver.service.dao.ExamDao#byId(java.lang.Long)
     */
    @Transactional(readOnly = true)
    @Override
    public Exam byId(final Long id) {
        final MyBatis3SelectModelAdapter<Exam> select = this.examJoinMapper
                .selectOneByExample()
                .where(examId, isEqualTo(id))
                .build();

        return select.execute();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eth.demo.sebserver.service.dao.ExamDao#getAll()
     */
    @Transactional(readOnly = true)
    @Override
    public Collection<Exam> getAll() {
        final MyBatis3SelectModelAdapter<Collection<Exam>> select = this.examJoinMapper
                .selectManyByExample()
                .build();

        return select.execute();
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
            final ExamRecord record = model.toExamRecord();
            this.examMapper.insert(record);
            id = record.getId();
        } else {
            this.examMapper.updateByPrimaryKeySelective(model.toExamRecord());

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
