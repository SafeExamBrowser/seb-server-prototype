/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.batis;

import static org.eth.demo.sebserver.batis.gen.mapper.ConfigurationAttributeRecordDynamicSqlSupport.configurationAttributeRecord;
import static org.eth.demo.sebserver.batis.gen.mapper.OrientationRecordDynamicSqlSupport.orientationRecord;
import static org.eth.demo.sebserver.batis.gen.mapper.OrientationRecordDynamicSqlSupport.view;
import static org.mybatis.dynamic.sql.SqlBuilder.equalTo;

import java.util.Collection;

import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.select.SelectDSL;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;

@Mapper
public interface AttributeOfViewJoinMapper {

    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @ResultType(AttributeOfViewJoinMapper.AttributeOfViewRecord.class)
    @ConstructorArgs({
            @Arg(column = "id", javaType = Long.class, jdbcType = JdbcType.BIGINT, id = true),
            @Arg(column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "type", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "parent_id", javaType = Long.class, jdbcType = JdbcType.BIGINT, id = true),
            @Arg(column = "resources", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "dependencies", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "orientationId", javaType = Long.class, jdbcType = JdbcType.BIGINT, id = true),
            @Arg(column = "view", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "group", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "x_position", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Arg(column = "y_position", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Arg(column = "width", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Arg(column = "height", javaType = Integer.class, jdbcType = JdbcType.INTEGER)
    })
    Collection<AttributeOfViewRecord> selectMany(SelectStatementProvider select);

    default Collection<AttributeOfViewRecord> selectOfView(final String viewName) {
        return SelectDSL.selectWithMapper(
                this::selectMany,
                configurationAttributeRecord.id,
                configurationAttributeRecord.name,
                configurationAttributeRecord.type,
                configurationAttributeRecord.parentId,
                configurationAttributeRecord.resources,
                configurationAttributeRecord.dependencies,
                orientationRecord.id.as("orientationId"),
                orientationRecord.view,
                orientationRecord.group,
                orientationRecord.xPosition,
                orientationRecord.yPosition,
                orientationRecord.width,
                orientationRecord.height)
                .from(configurationAttributeRecord)
                .leftJoin(orientationRecord)
                .on(configurationAttributeRecord.id, equalTo(orientationRecord.configAttributeId))
                .where(view, SqlBuilder.isEqualTo(viewName))
                .build()
                .execute();
    }

    public static final class AttributeOfViewRecord {
        public final Long id;
        public final String name;
        public final String type;
        public final Long parentId;
        public final String resources;
        public final String dependencies;
        public final Long orientationId;
        public final String view;
        public final String group;
        public final Integer xPosition;
        public final Integer yPosition;
        public final Integer width;
        public final Integer height;

        public AttributeOfViewRecord(
                final Long id, final String name, final String type, final Long parentId,
                final String resources, final String dependencies,
                final Long orientationId, final String view, final String group,
                final Integer xPosition, final Integer yPosition, final Integer width, final Integer height) {

            this.id = id;
            this.name = name;
            this.type = type;
            this.parentId = parentId;
            this.resources = resources;
            this.dependencies = dependencies;
            this.orientationId = orientationId;
            this.view = view;
            this.group = group;
            this.xPosition = xPosition;
            this.yPosition = yPosition;
            this.width = width;
            this.height = height;
        }
    }

}
