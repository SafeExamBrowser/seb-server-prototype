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
            @Arg(column = "lms_setup_id", javaType = Long.class, jdbcType = JdbcType.BIGINT),
            @Arg(column = "external_uuid", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "type", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "supporter", javaType = String.class, jdbcType = JdbcType.VARCHAR),

            @Arg(column = "indicatorId", javaType = Long.class, jdbcType = JdbcType.BIGINT, id = true),
            @Arg(column = "indicatorType", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "indicatorName", javaType = String.class, jdbcType = JdbcType.VARCHAR),
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
                examRecord.lmsSetupId,
                examRecord.externalUuid,
                examRecord.type,
                examRecord.owner,
                examRecord.supporter,

                indicatorRecord.id.as("indicatorId"),
                indicatorRecord.type.as("indicatorType"),
                indicatorRecord.name.as("indicatorName"),
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
        public final Long lmsSetupId;
        public final String externalUuid;
        public final String type;
        public final String owner;
        public final String supporter;

        public final IndicatorDefinition indicator;
        public final ExamSEBConfigMapping configMapping;

        private ExamJoinRecord(
                final Long id,
                final Long lmsSetupId,
                final String externalUuid,
                final String type,
                final String owner,
                final String supporter,

                final Long indicatorId,
                final String indicatorType,
                final String indicatorName,
                final BigDecimal threshold1,
                final BigDecimal threshold2,
                final BigDecimal threshold3,
                final Long configMappingId,
                final Long configurationId,
                final String clientInfo) {

            this.id = id;
            this.lmsSetupId = lmsSetupId;
            this.externalUuid = externalUuid;
            this.type = type;
            this.owner = owner;
            this.supporter = supporter;

            indicator = (indicatorId != null)
                    ? new IndicatorDefinition(indicatorName, indicatorType, threshold1, threshold2, threshold3)
                    : null;
            configMapping = (configMappingId != null)
                    ? new ExamSEBConfigMapping(configMappingId, id, configurationId, clientInfo)
                    : null;
        }
    }

}
