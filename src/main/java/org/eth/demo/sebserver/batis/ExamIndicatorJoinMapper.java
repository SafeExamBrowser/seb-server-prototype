/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.batis;

import static org.eth.demo.sebserver.batis.gen.mapper.ExamRecordDynamicSqlSupport.examRecord;
import static org.eth.demo.sebserver.batis.gen.mapper.IndicatorRecordDynamicSqlSupport.indicatorRecord;
import static org.mybatis.dynamic.sql.SqlBuilder.equalTo;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.type.JdbcType;
import org.eth.demo.sebserver.domain.rest.exam.Exam;
import org.eth.demo.sebserver.domain.rest.exam.IndicatorDefinition;
import org.mybatis.dynamic.sql.select.MyBatis3SelectModelAdapter;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.SelectDSL;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;

@Mapper
public interface ExamIndicatorJoinMapper {

    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @ResultType(ExamIndicatorJoinMapper.JoinRecord.class)
    @ConstructorArgs({
            @Arg(column = "id", javaType = Long.class, jdbcType = JdbcType.BIGINT, id = true),
            @Arg(column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "status", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Arg(column = "indicatorId", javaType = Long.class, jdbcType = JdbcType.BIGINT, id = true),
            @Arg(column = "type", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "threshold1", javaType = BigDecimal.class, jdbcType = JdbcType.DECIMAL),
            @Arg(column = "threshold2", javaType = BigDecimal.class, jdbcType = JdbcType.DECIMAL),
            @Arg(column = "threshold3", javaType = BigDecimal.class, jdbcType = JdbcType.DECIMAL)
    })
    void selectMany(SelectStatementProvider select,
            ResultHandler<ExamIndicatorJoinMapper.JoinRecord> resultHandler);

    default Collection<Exam> selectMany(final SelectStatementProvider select) {
        final JoinResultHandler resultHandler = new JoinResultHandler();
        selectMany(select, resultHandler);
        return resultHandler.resultMap.values();
    }

    default QueryExpressionDSL<MyBatis3SelectModelAdapter<Collection<Exam>>>.JoinSpecificationFinisher selectJoinByExample() {
        return SelectDSL.selectWithMapper(
                this::selectMany,
                examRecord.id,
                examRecord.name,
                examRecord.status,
                indicatorRecord.id.as("indicatorId"),
                indicatorRecord.type,
                indicatorRecord.threshold1,
                indicatorRecord.threshold2,
                indicatorRecord.threshold3)
                .from(examRecord)
                .leftJoin(indicatorRecord)
                .on(examRecord.id, equalTo(indicatorRecord.examId));
    }

    default Collection<Exam> selectAll() {
        return selectJoinByExample()
                .build()
                .execute();
    }

    static final class JoinResultHandler implements ResultHandler<JoinRecord> {

        final Map<Long, Exam> resultMap = new HashMap<>();

        private JoinResultHandler() {
        }

        @Override
        public void handleResult(final ResultContext<? extends JoinRecord> resultContext) {
            final JoinRecord rec = resultContext.getResultObject();
            resultMap.computeIfAbsent(rec.id, id -> Exam.create(id, rec.name, rec.status))
                    .addIndicator(rec.indicator);
        }
    }

    static final class JoinRecord {
        private final Long id;
        private final String name;
        private final Integer status;
        public final IndicatorDefinition indicator;

        private JoinRecord(final Long id,
                final String name,
                final Integer status,
                final Long indicatorId,
                final String type,
                final BigDecimal threshold1,
                final BigDecimal threshold2,
                final BigDecimal threshold3) {

            this.id = id;
            this.name = name;
            this.status = status;
            indicator = (indicatorId != null)
                    ? new IndicatorDefinition(type, threshold1, threshold2, threshold3) : null;
        }
    }

}
