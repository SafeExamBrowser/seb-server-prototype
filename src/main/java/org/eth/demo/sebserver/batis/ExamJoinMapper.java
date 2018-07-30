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
import java.util.Collection;

import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;
import org.eth.demo.sebserver.domain.rest.exam.ExamSEBConfigMapping;
import org.eth.demo.sebserver.domain.rest.exam.IndicatorDefinition;
import org.joda.time.DateTime;
import org.mybatis.dynamic.sql.select.MyBatis3SelectModelAdapter;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.SelectDSL;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;

@Mapper
public interface ExamJoinMapper {

    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @ResultType(ExamJoinMapper.ExamJoinRecord.class)
    @ConstructorArgs({
            @Arg(column = "id", javaType = Long.class, jdbcType = JdbcType.BIGINT, id = true),
            @Arg(column = "owner_id", javaType = Long.class, jdbcType = JdbcType.BIGINT),
            @Arg(column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "status", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "start_time", javaType = DateTime.class, typeHandler = JodaTimeTypeResolver.class, jdbcType = JdbcType.TIMESTAMP),
            @Arg(column = "end_time", javaType = DateTime.class, typeHandler = JodaTimeTypeResolver.class, jdbcType = JdbcType.TIMESTAMP),
            @Arg(column = "lms_login_url", javaType = String.class, jdbcType = JdbcType.VARCHAR),

            @Arg(column = "indicatorId", javaType = Long.class, jdbcType = JdbcType.BIGINT, id = true),
            @Arg(column = "type", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "threshold1", javaType = BigDecimal.class, jdbcType = JdbcType.DECIMAL),
            @Arg(column = "threshold2", javaType = BigDecimal.class, jdbcType = JdbcType.DECIMAL),
            @Arg(column = "threshold3", javaType = BigDecimal.class, jdbcType = JdbcType.DECIMAL),

            @Arg(column = "configMappingId", javaType = Long.class, jdbcType = JdbcType.BIGINT),
            @Arg(column = "configuration_node_id", javaType = Long.class, jdbcType = JdbcType.BIGINT),
            @Arg(column = "client_info", javaType = String.class, jdbcType = JdbcType.VARCHAR)
    })
    Collection<ExamJoinRecord> selectMany(SelectStatementProvider select);

    default QueryExpressionDSL<MyBatis3SelectModelAdapter<Collection<ExamJoinRecord>>>.JoinSpecificationFinisher selectByExample() {
        return SelectDSL.selectWithMapper(
                this::selectMany,
                examRecord.id,
                examRecord.ownerId,
                examRecord.name,
                examRecord.status,
                examRecord.startTime,
                examRecord.endTime,
                examRecord.lmsLoginUrl,

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

    public static final class ExamJoinRecord {
        public final Long id;
        public final Long ownerId;
        public final String name;
        public final String status;
        public final DateTime startTime;
        public final DateTime endTime;
        public final String lmsLoginURL;

        public final IndicatorDefinition indicator;
        public final ExamSEBConfigMapping configMapping;

        private ExamJoinRecord(
                final Long id,
                final Long ownerId,
                final String name,
                final String status,
                final DateTime startTime,
                final DateTime endTime,
                final String lmsLoginURL,

                final Long indicatorId,
                final String type,
                final BigDecimal threshold1,
                final BigDecimal threshold2,
                final BigDecimal threshold3,
                final Long configMappingId,
                final Long configurationId,
                final String clientInfo) {

            this.id = id;
            this.ownerId = ownerId;
            this.name = name;
            this.status = status;
            this.startTime = startTime;
            this.endTime = endTime;
            this.lmsLoginURL = lmsLoginURL;

            indicator = (indicatorId != null)
                    ? new IndicatorDefinition(type, threshold1, threshold2, threshold3)
                    : null;
            configMapping = (configMappingId != null)
                    ? new ExamSEBConfigMapping(configMappingId, id, configurationId, clientInfo)
                    : null;
        }
    }

}
