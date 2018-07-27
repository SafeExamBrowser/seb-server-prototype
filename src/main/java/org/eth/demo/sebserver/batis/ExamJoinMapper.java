/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.batis;

import static org.eth.demo.sebserver.batis.gen.mapper.ExamConfigurationMapRecordDynamicSqlSupport.examConfigurationMapRecord;
import static org.eth.demo.sebserver.batis.gen.mapper.ExamRecordDynamicSqlSupport.examRecord;
import static org.eth.demo.sebserver.batis.gen.mapper.IndicatorRecordDynamicSqlSupport.indicatorRecord;
import static org.mybatis.dynamic.sql.SqlBuilder.equalTo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.type.JdbcType;
import org.eth.demo.sebserver.domain.rest.exam.Exam;
import org.eth.demo.sebserver.domain.rest.exam.ExamSEBConfigMapping;
import org.eth.demo.sebserver.domain.rest.exam.ExamStatus;
import org.eth.demo.sebserver.domain.rest.exam.IndicatorDefinition;
import org.mybatis.dynamic.sql.select.MyBatis3SelectModelAdapter;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.SelectDSL;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;

@Mapper
public interface ExamJoinMapper {

    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @ResultType(ExamJoinMapper.JoinRecord.class)
    @ConstructorArgs({
            @Arg(column = "id", javaType = Long.class, jdbcType = JdbcType.BIGINT, id = true),
            @Arg(column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "status", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "owner_id", javaType = Long.class, jdbcType = JdbcType.BIGINT),

            @Arg(column = "indicatorId", javaType = Long.class, jdbcType = JdbcType.BIGINT, id = true),
            @Arg(column = "type", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "threshold1", javaType = BigDecimal.class, jdbcType = JdbcType.DECIMAL),
            @Arg(column = "threshold2", javaType = BigDecimal.class, jdbcType = JdbcType.DECIMAL),
            @Arg(column = "threshold3", javaType = BigDecimal.class, jdbcType = JdbcType.DECIMAL),

            @Arg(column = "configMappingId", javaType = Long.class, jdbcType = JdbcType.BIGINT),
            @Arg(column = "configuration_node_id", javaType = Long.class, jdbcType = JdbcType.BIGINT),
            @Arg(column = "client_info", javaType = String.class, jdbcType = JdbcType.VARCHAR)
    })
    void selectMany(
            SelectStatementProvider select,
            ResultHandler<ExamJoinMapper.JoinRecord> resultHandler);

    default Collection<Exam> selectMany(final SelectStatementProvider select) {
        final JoinResultHandler resultHandler = new JoinResultHandler();
        selectMany(select, resultHandler);
        return resultHandler.recordMap
                .values()
                .stream()
                .map(JoinResultHandler::createExam)
                .collect(Collectors.toList());
    }

    default Exam selectOne(final SelectStatementProvider select) {
        final Collection<Exam> selectMany = selectMany(select);
        if (selectMany == null) {
            return null;
        }

        return selectMany.stream()
                .findFirst()
                .orElse(null);
    }

    default QueryExpressionDSL<MyBatis3SelectModelAdapter<Collection<Exam>>>.JoinSpecificationFinisher selectManyByExample() {
        return ExamJoinMapper.<Collection<Exam>> selectWithJoin(this::selectMany);
    }

    default QueryExpressionDSL<MyBatis3SelectModelAdapter<Exam>>.JoinSpecificationFinisher selectOneByExample() {
        return ExamJoinMapper.<Exam> selectWithJoin(this::selectOne);
    }

    static <T> QueryExpressionDSL<MyBatis3SelectModelAdapter<T>>.JoinSpecificationFinisher selectWithJoin(
            final Function<SelectStatementProvider, T> mapperMethod) {

        return SelectDSL.selectWithMapper(
                mapperMethod,
                examRecord.id,
                examRecord.name,
                examRecord.status,
                examRecord.ownerId,

                indicatorRecord.id.as("indicatorId"),
                indicatorRecord.type,
                indicatorRecord.threshold1,
                indicatorRecord.threshold2,
                indicatorRecord.threshold3,

                examConfigurationMapRecord.id.as("configMappingId"),
                examConfigurationMapRecord.configurationNodeId,
                examConfigurationMapRecord.clientInfo)

                .from(examRecord)

                .leftJoin(indicatorRecord)
                .on(examRecord.id, equalTo(indicatorRecord.examId))

                .leftJoin(examConfigurationMapRecord)
                .on(examRecord.id, equalTo(examConfigurationMapRecord.examId));
    }

    static final class JoinResultHandler implements ResultHandler<JoinRecord> {

        final Map<Long, Collection<JoinRecord>> recordMap = new HashMap<>();

        @Override
        public void handleResult(final ResultContext<? extends JoinRecord> resultContext) {
            final JoinRecord rec = resultContext.getResultObject();
            recordMap.computeIfAbsent(rec.id, id -> new ArrayList<>())
                    .add(rec);
        }

        public static final Exam createExam(final Collection<JoinRecord> records) {
            if (records == null) {
                return null;
            }

            Exam prototype = null;
            final List<IndicatorDefinition> indicators = new ArrayList<>();
            final List<ExamSEBConfigMapping> configMapping = new ArrayList<>();

            for (final JoinRecord record : records) {
                if (prototype == null) {
                    prototype = record.createPrototype();
                }

                if (record.indicator != null) {
                    indicators.add(record.indicator);
                }
                if (record.configMapping != null) {
                    configMapping.add(record.configMapping);
                }
            }

            return Exam.of(prototype, indicators, configMapping);
        }
    }

    public static final class JoinRecord {
        public final Long id;
        public final String name;
        public final String status;
        public final Long ownerId;
        public final IndicatorDefinition indicator;
        public final ExamSEBConfigMapping configMapping;

        private JoinRecord(
                final Long id,
                final String name,
                final String status,
                final Long ownerId,
                final Long indicatorId,
                final String type,
                final BigDecimal threshold1,
                final BigDecimal threshold2,
                final BigDecimal threshold3,
                final Long configMappingId,
                final Long configurationId,
                final String clientInfo) {

            this.id = id;
            this.name = name;
            this.status = status;
            this.ownerId = ownerId;
            indicator = (indicatorId != null)
                    ? new IndicatorDefinition(type, threshold1, threshold2, threshold3)
                    : null;
            configMapping = (configMappingId != null)
                    ? new ExamSEBConfigMapping(configMappingId, id, configurationId, clientInfo)
                    : null;
        }

        Exam createPrototype() {
            return new Exam(id, name, ExamStatus.valueOf(status), ownerId, null, null);
        }
    }

}
