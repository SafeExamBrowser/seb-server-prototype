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
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eth.demo.sebserver.batis.ExamJoinMapper;
import org.eth.demo.sebserver.batis.ExamJoinMapper.ExamJoinRecord;
import org.eth.demo.sebserver.batis.gen.mapper.ExamRecordMapper;
import org.eth.demo.sebserver.batis.gen.mapper.IndicatorRecordMapper;
import org.eth.demo.sebserver.domain.rest.exam.Exam;
import org.eth.demo.sebserver.domain.rest.exam.ExamSEBConfigMapping;
import org.eth.demo.sebserver.domain.rest.exam.IndicatorDefinition;
import org.eth.demo.sebserver.service.lms.CourseData;
import org.eth.demo.sebserver.service.lms.LmsAPIConnectionFactory;
import org.eth.demo.sebserver.service.lms.LmsConnectionTemplate;
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
    private final LmsAPIConnectionFactory lmsAPIConnectionFactory;

    public ExamDaoImpl(
            final ExamRecordMapper examMapper,
            final IndicatorRecordMapper indicatorMapper,
            final ExamJoinMapper examJoinMapper,
            final LmsAPIConnectionFactory lmsAPIConnectionFactory) {

        super();
        this.examMapper = examMapper;
        this.indicatorMapper = indicatorMapper;
        this.examJoinMapper = examJoinMapper;
        this.lmsAPIConnectionFactory = lmsAPIConnectionFactory;
    }

    @Override
    public Exam importExam(final Long lmsSetupId, final String externalUuid) {
        // TODO Auto-generated method stub
        return null;
    }

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

    @Override
    public Optional<Exam> runningExam(final Long id) {
        final Exam exam = byId(id);
        return (isRunning(exam)) ? Optional.of(exam) : Optional.empty();
    }

    @Override
    public boolean isRunning(final Long id) {
        return isRunning(byId(id));
    }

    private boolean isRunning(final Exam exam) {
        if (exam == null) {
            return false;
        }
        return exam.getStartTime().isBeforeNow() && exam.getEndTime().isAfterNow();
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<Exam> getAll() {
        return getManyFromJoinRecords(this.examJoinMapper
                .selectByExample()
                .build()
                .execute());
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<Exam> getAll(final Predicate<Exam> predicate) {
        return getAll().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean remove(final Long id) {

        // TODO check integrity. E.g. exam has no relations so far

        this.indicatorMapper.deleteByExample()
                .where(examId, isEqualTo(id))
                .build()
                .execute();

        final int del = this.examMapper.deleteByPrimaryKey(id);
        return del > 0;
    }

    private final Collection<Exam> getManyFromJoinRecords(final Collection<ExamJoinRecord> joinRecords) {
        return joinRecords.stream()
                .reduce(
                        new HashMap<Long, Collection<ExamJoinRecord>>(),
                        ExamDaoImpl::accToMap,
                        (m1, m2) -> {
                            m1.putAll(m2);
                            return m1;
                        })
                .values()
                .stream()
                .map(recs -> createExam(recs, createPrototype(recs)))
                .collect(Collectors.toList());
    }

    private static final Map<Long, Collection<ExamJoinRecord>> accToMap(
            final Map<Long, Collection<ExamJoinRecord>> map,
            final ExamJoinRecord rec) {

        map.computeIfAbsent(rec.id, id -> new ArrayList<>())
                .add(rec);
        return map;
    }

    private final Exam getOneFromJoinRecords(final Collection<ExamJoinRecord> joinRecords) {
        return getManyFromJoinRecords(joinRecords)
                .stream()
                .findFirst()
                .orElse(null);
    }

    private final Exam createPrototype(final Collection<ExamJoinRecord> records) {
        if (records != null && !records.isEmpty()) {
            final ExamJoinRecord first = records.iterator().next();
            final LmsConnectionTemplate lmsAPIConnection = this.lmsAPIConnectionFactory
                    .getLmsAPIConnection(first.lmsSetupId);
            final CourseData course = lmsAPIConnection.course(first.externalUuid);
            return Exam.of(
                    first.id,
                    lmsAPIConnection.lmsSetup().getInstitutionId(),
                    first.lmsSetupId,
                    course.name,
                    course.description,
                    course.getStatus(),
                    course.startTime,
                    course.endTime,
                    course.enrollmentURL);
        }

        return null;
    }

    private final Exam createExam(final Collection<ExamJoinRecord> records, final Exam prototype) {

        final ArrayList<IndicatorDefinition> indicator = new ArrayList<>();
        final ArrayList<ExamSEBConfigMapping> configMappings = new ArrayList<>();

        for (final ExamJoinRecord record : records) {
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
