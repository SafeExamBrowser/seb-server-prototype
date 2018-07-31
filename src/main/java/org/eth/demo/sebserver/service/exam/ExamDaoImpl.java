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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eth.demo.sebserver.batis.ExamJoinMapper;
import org.eth.demo.sebserver.batis.ExamJoinMapper.ExamJoinRecord;
import org.eth.demo.sebserver.batis.gen.mapper.ExamRecordMapper;
import org.eth.demo.sebserver.batis.gen.mapper.IndicatorRecordMapper;
import org.eth.demo.sebserver.batis.gen.model.ExamRecord;
import org.eth.demo.sebserver.domain.rest.exam.Exam;
import org.eth.demo.sebserver.domain.rest.exam.ExamSEBConfigMapping;
import org.eth.demo.sebserver.domain.rest.exam.ExamStatus;
import org.eth.demo.sebserver.domain.rest.exam.IndicatorDefinition;
import org.mybatis.dynamic.sql.select.MyBatis3SelectModelAdapter;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Lazy
@Component
public class ExamDaoImpl implements ExamDao {

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

    @Override
    public Exam createNew(final Exam exam) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eth.demo.sebserver.service.dao.ExamDao#byId(java.lang.Long)
     */
    @Transactional(readOnly = true)
    @Override
    public Exam byId(final Long id) {
        final QueryExpressionDSL<MyBatis3SelectModelAdapter<Collection<ExamJoinRecord>>>.JoinSpecificationFinisher selectByExample =
                this.examJoinMapper.selectByExample();

        final Exam exam = getOneFromJoinRecords(selectByExample
                .where(examId, isEqualTo(id))
                .build()
                .execute());

        return exam;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eth.demo.sebserver.service.dao.ExamDao#getAll()
     */
    @Transactional(readOnly = true)
    @Override
    public Collection<Exam> getAll() {
        return getManyFromJoinRecords(this.examJoinMapper
                .selectByExample()
                .build()
                .execute());
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

    private static final Collection<Exam> getManyFromJoinRecords(final Collection<ExamJoinRecord> joinRecords) {
        return joinRecords.stream()
                .reduce(
                        new HashMap<Long, Collection<ExamJoinRecord>>(),
                        ExamDaoImpl::accToMap,
                        (m1, m2) -> m1) // TODO if we allow parallelization we need a proper combiner here
                .values()
                .stream()
                .map(ExamDaoImpl::createExam)
                .collect(Collectors.toList());
    }

    private static final Map<Long, Collection<ExamJoinRecord>> accToMap(
            final Map<Long, Collection<ExamJoinRecord>> map,
            final ExamJoinRecord rec) {

        map.computeIfAbsent(rec.id, id -> new ArrayList<>())
                .add(rec);
        return map;
    }

    private static final Exam getOneFromJoinRecords(final Collection<ExamJoinRecord> joinRecords) {
        return getManyFromJoinRecords(joinRecords)
                .stream()
                .findFirst()
                .orElse(null);
    }

    private static final Exam createExam(final Collection<ExamJoinRecord> records) {
        assert records != null && !records.isEmpty() : "Expecting none-empty ExamJoinRecord Collection";

        Exam prototype = null;
        final ArrayList<IndicatorDefinition> indicator = new ArrayList<>();
        final ArrayList<ExamSEBConfigMapping> configMappings = new ArrayList<>();

        for (final ExamJoinRecord record : records) {
            if (prototype == null) {
                prototype = Exam.of(
                        record.id,
                        record.ownerId,
                        record.name,
                        ExamStatus.valueOf(record.status),
                        record.startTime,
                        record.endTime,
                        record.lmsLoginURL);
            }
            if (record.indicator != null) {
                indicator.add(record.indicator);
            }
            if (record.configMapping != null) {
                configMappings.add(record.configMapping);
            }
        }

        return Exam.of(
                prototype,
                indicator,
                configMappings);
    }
}
