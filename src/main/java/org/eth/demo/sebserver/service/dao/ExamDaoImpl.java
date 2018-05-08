package org.eth.demo.sebserver.service.dao;

import static org.eth.demo.sebserver.batis.gen.mapper.IndicatorRecordDynamicSqlSupport.examId;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eth.demo.sebserver.batis.ExamIndicatorJoinMapper;
import org.eth.demo.sebserver.batis.gen.mapper.ExamRecordMapper;
import org.eth.demo.sebserver.batis.gen.mapper.IndicatorRecordMapper;
import org.eth.demo.sebserver.batis.gen.model.ExamRecord;
import org.eth.demo.sebserver.batis.gen.model.IndicatorRecord;
import org.eth.demo.sebserver.domain.rest.Exam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(readOnly = true)
    @Override
    public Exam byId(final Long id) {
        try {
            final ExamRecord record = this.examMapper.selectByPrimaryKey(id);

            if (record == null) {
                return Exam.NULL_MODEL;
            }

            final List<IndicatorRecord> indicators = this.indicatorMapper.selectByExample()
                    .where(examId, isEqualTo(id))
                    .build()
                    .execute();

            return Exam.fromRecord(record, indicators);
        } catch (final Throwable t) {
            log.error("Unexpected Exception: ", t);
            return Exam.NULL_MODEL;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<Exam> getAll() {
        try {
            return this.examIndicatorJoinMapper.selectAll();
        } catch (final Throwable t) {
            log.error("Unexpected Exception: ", t);
            return Collections.emptyList();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<Exam> getAll(final Predicate<Exam> predicate) {
        try {
            return getAll()
                    .stream()
                    .filter(predicate)
                    .collect(Collectors.toList());
        } catch (final Throwable t) {
            log.error("Unexpected Exception: ", t);
            return Collections.emptyList();
        }
    }

    @Transactional
    @Override
    public Exam save(final Exam model) {
        try {
            Exam result;
            if (model.getId() == null) {
                final long id = this.examMapper.insert(model.toRecord());
                if (id < 0) {
                    return Exam.NULL_MODEL;
                }

                result = model.withId(id);

                // TODO

            } else {
                this.examMapper.updateByPrimaryKey(model.toRecord());
                result = model;
            }

            return result;
        } catch (final Throwable t) {
            log.error("Unexpected Exception: ", t);
            return Exam.NULL_MODEL;
        }
    }

    @Transactional
    @Override
    public Exam delete(final Long id) {
        try {
            final Exam exam = byId(id);
            if (isNull(exam)) {
                return exam;
            }

            final int del = this.examMapper.deleteByPrimaryKey(id);
            System.out.println("************************************ del:" + del);

            return exam.withId(null);
        } catch (final Throwable t) {
            log.error("Unexpected Exception: ", t);
            return Exam.NULL_MODEL;
        }
    }

    @Override
    public boolean isNull(final Exam model) {
        return Exam.NULL_MODEL == model;
    }

}
