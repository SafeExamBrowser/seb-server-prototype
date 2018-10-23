/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.batis;

import static org.eth.demo.sebserver.batis.gen.mapper.ConfigurationNodeRecordDynamicSqlSupport.configurationNodeRecord;
import static org.eth.demo.sebserver.batis.gen.mapper.ConfigurationRecordDynamicSqlSupport.configurationRecord;
import static org.eth.demo.sebserver.batis.gen.mapper.ExamConfigurationMapRecordDynamicSqlSupport.examConfigurationMapRecord;
import static org.mybatis.dynamic.sql.SqlBuilder.equalTo;

import java.util.Collection;

import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;
import org.joda.time.DateTime;
import org.mybatis.dynamic.sql.select.MyBatis3SelectModelAdapter;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.SelectDSL;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;

@Mapper
public interface ConfigurationNodeJoinMapper {

    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @ResultType(ConfigNodeJoinRecord.class)
    @ConstructorArgs({
            @Arg(column = "id", javaType = Long.class, jdbcType = JdbcType.BIGINT, id = true),
            @Arg(column = "institution_id", javaType = Long.class, jdbcType = JdbcType.BIGINT, id = true),
            @Arg(column = "owner", javaType = String.class, jdbcType = JdbcType.VARCHAR, id = true),
            @Arg(column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "type", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "template", javaType = String.class, jdbcType = JdbcType.VARCHAR),

            @Arg(column = "configVersionId", javaType = Long.class, jdbcType = JdbcType.BIGINT, id = true),
            @Arg(column = "version", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "version_date", typeHandler = JodaTimeTypeResolver.class, javaType = DateTime.class,
                    jdbcType = JdbcType.DATE),
            @Arg(column = "followup", javaType = Boolean.class, jdbcType = JdbcType.BIT),

            @Arg(column = "examMappingId", javaType = Long.class, jdbcType = JdbcType.BIGINT, id = true),
            @Arg(column = "exam_id", javaType = Long.class, jdbcType = JdbcType.BIGINT, id = true),
            @Arg(column = "client_info", javaType = String.class, jdbcType = JdbcType.VARCHAR)
    })
    Collection<ConfigNodeJoinRecord> selectMany(SelectStatementProvider select);

    default QueryExpressionDSL<MyBatis3SelectModelAdapter<Collection<ConfigNodeJoinRecord>>>.JoinSpecificationFinisher selectByExample() {
        return SelectDSL.selectWithMapper(
                this::selectMany,
                configurationNodeRecord.id,
                configurationNodeRecord.institutionId,
                configurationNodeRecord.owner,
                configurationNodeRecord.name,
                configurationNodeRecord.type,
                configurationNodeRecord.template,

                configurationRecord.id.as("configVersionId"),
                configurationRecord.version,
                configurationRecord.versionDate,
                configurationRecord.followup,

                examConfigurationMapRecord.id.as("examMappingId"),
                examConfigurationMapRecord.examId,
                examConfigurationMapRecord.clientInfo)

                .from(configurationNodeRecord)
                .leftJoin(configurationRecord)
                .on(configurationNodeRecord.id, equalTo(configurationRecord.configurationNodeId))

                .leftJoin(examConfigurationMapRecord)
                .on(configurationNodeRecord.id, equalTo(examConfigurationMapRecord.configurationNodeId));
    }

    public static final class ConfigNodeJoinRecord {
        public final Long id;
        public final Long institutionId;
        public final String owner;
        public final String name;
        public final String type;
        public final String template;

        public final Long configVersionId;
        public final String version;
        public final DateTime versionDate;
        public final Boolean followup;

        public final Long examMappingId;
        public final Long examId;
        public final String clientInfo;

        public ConfigNodeJoinRecord(
                final Long id,
                final Long institutionId,
                final String owner,
                final String name,
                final String template,
                final String type,
                final Long configVersionId,
                final String version,
                final DateTime versionDate,
                final Boolean followup,
                final Long examMappingId,
                final Long examId,
                final String clientInfo) {

            this.id = id;
            this.institutionId = institutionId;
            this.owner = owner;
            this.name = name;
            this.type = type;
            this.template = template;
            this.configVersionId = configVersionId;
            this.version = version;
            this.versionDate = versionDate;
            this.followup = followup;
            this.examMappingId = examMappingId;
            this.examId = examId;
            this.clientInfo = clientInfo;
        }
    }

}
