/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.batis;

import static org.eth.demo.sebserver.batis.gen.mapper.ConfigurationValueRecordDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

import java.util.List;

import org.apache.ibatis.annotations.Flush;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.executor.BatchResult;
import org.eth.demo.sebserver.batis.gen.model.ConfigurationValueRecord;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategy;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;

@Mapper
public interface BulkSaveAttributeValuesMapper {

    @InsertProvider(type = SqlProviderAdapter.class, method = "insert")
    @SelectKey(
            statement = "SELECT LAST_INSERT_ID()",
            keyProperty = "record.id",
            before = false,
            resultType = Long.class)
    int insert(InsertStatementProvider<ConfigurationValueRecord> insertStatement);

    @UpdateProvider(type = SqlProviderAdapter.class, method = "update")
    int update(UpdateStatementProvider updateStatement);

    default int insert(final ConfigurationValueRecord record) {
        return insert(SqlBuilder.insert(record)
                .into(configurationValueRecord)
                .map(configurationId).toProperty("configurationId")
                .map(configurationAttributeId).toProperty("configurationAttributeId")
                .map(listIndex).toProperty("listIndex")
                .map(value).toProperty("value")
                .map(text).toProperty("text")
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    default int updateByPrimaryKey(final ConfigurationValueRecord record) {
        return UpdateDSL.updateWithMapper(this::update, configurationValueRecord)
                .set(configurationId).equalTo(record::getConfigurationId)
                .set(configurationAttributeId).equalTo(record::getConfigurationAttributeId)
                .set(listIndex).equalTo(record::getListIndex)
                .set(value).equalTo(record::getValue)
                .set(text).equalTo(record::getText)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }

    // TODO test this and check what exactly flush is returning
    @Flush
    List<BatchResult> flush();

}
