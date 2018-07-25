package org.eth.demo.sebserver.batis.gen.mapper;

import static org.eth.demo.sebserver.batis.gen.mapper.ConfigurationValueRecordDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import java.util.List;
import javax.annotation.Generated;
import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.eth.demo.sebserver.batis.gen.model.ConfigurationValueRecord;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.delete.DeleteDSL;
import org.mybatis.dynamic.sql.delete.MyBatis3DeleteModelAdapter;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategy;
import org.mybatis.dynamic.sql.select.MyBatis3SelectModelAdapter;
import org.mybatis.dynamic.sql.select.QueryExpressionDSL;
import org.mybatis.dynamic.sql.select.SelectDSL;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.MyBatis3UpdateModelAdapter;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;

@Mapper
public interface ConfigurationValueRecordMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.574+02:00", comments="Source Table: configuration_value")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.574+02:00", comments="Source Table: configuration_value")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.575+02:00", comments="Source Table: configuration_value")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<ConfigurationValueRecord> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.575+02:00", comments="Source Table: configuration_value")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({
        @Arg(column="id", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true),
        @Arg(column="configuration_id", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="configuration_attribute_id", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="list_index", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="value", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="text", javaType=String.class, jdbcType=JdbcType.CLOB)
    })
    ConfigurationValueRecord selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.575+02:00", comments="Source Table: configuration_value")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({
        @Arg(column="id", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true),
        @Arg(column="configuration_id", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="configuration_attribute_id", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="list_index", javaType=Integer.class, jdbcType=JdbcType.INTEGER),
        @Arg(column="value", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="text", javaType=String.class, jdbcType=JdbcType.CLOB)
    })
    List<ConfigurationValueRecord> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.575+02:00", comments="Source Table: configuration_value")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.575+02:00", comments="Source Table: configuration_value")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<Long>> countByExample() {
        return SelectDSL.selectWithMapper(this::count, SqlBuilder.count())
                .from(configurationValueRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.575+02:00", comments="Source Table: configuration_value")
    default DeleteDSL<MyBatis3DeleteModelAdapter<Integer>> deleteByExample() {
        return DeleteDSL.deleteFromWithMapper(this::delete, configurationValueRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.575+02:00", comments="Source Table: configuration_value")
    default int deleteByPrimaryKey(Long id_) {
        return DeleteDSL.deleteFromWithMapper(this::delete, configurationValueRecord)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.575+02:00", comments="Source Table: configuration_value")
    default int insert(ConfigurationValueRecord record) {
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

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.575+02:00", comments="Source Table: configuration_value")
    default int insertSelective(ConfigurationValueRecord record) {
        return insert(SqlBuilder.insert(record)
                .into(configurationValueRecord)
                .map(configurationId).toPropertyWhenPresent("configurationId", record::getConfigurationId)
                .map(configurationAttributeId).toPropertyWhenPresent("configurationAttributeId", record::getConfigurationAttributeId)
                .map(listIndex).toPropertyWhenPresent("listIndex", record::getListIndex)
                .map(value).toPropertyWhenPresent("value", record::getValue)
                .map(text).toPropertyWhenPresent("text", record::getText)
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.575+02:00", comments="Source Table: configuration_value")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<ConfigurationValueRecord>>> selectByExample() {
        return SelectDSL.selectWithMapper(this::selectMany, id, configurationId, configurationAttributeId, listIndex, value, text)
                .from(configurationValueRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.575+02:00", comments="Source Table: configuration_value")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<ConfigurationValueRecord>>> selectDistinctByExample() {
        return SelectDSL.selectDistinctWithMapper(this::selectMany, id, configurationId, configurationAttributeId, listIndex, value, text)
                .from(configurationValueRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.576+02:00", comments="Source Table: configuration_value")
    default ConfigurationValueRecord selectByPrimaryKey(Long id_) {
        return SelectDSL.selectWithMapper(this::selectOne, id, configurationId, configurationAttributeId, listIndex, value, text)
                .from(configurationValueRecord)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.576+02:00", comments="Source Table: configuration_value")
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExample(ConfigurationValueRecord record) {
        return UpdateDSL.updateWithMapper(this::update, configurationValueRecord)
                .set(configurationId).equalTo(record::getConfigurationId)
                .set(configurationAttributeId).equalTo(record::getConfigurationAttributeId)
                .set(listIndex).equalTo(record::getListIndex)
                .set(value).equalTo(record::getValue)
                .set(text).equalTo(record::getText);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.576+02:00", comments="Source Table: configuration_value")
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExampleSelective(ConfigurationValueRecord record) {
        return UpdateDSL.updateWithMapper(this::update, configurationValueRecord)
                .set(configurationId).equalToWhenPresent(record::getConfigurationId)
                .set(configurationAttributeId).equalToWhenPresent(record::getConfigurationAttributeId)
                .set(listIndex).equalToWhenPresent(record::getListIndex)
                .set(value).equalToWhenPresent(record::getValue)
                .set(text).equalToWhenPresent(record::getText);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.576+02:00", comments="Source Table: configuration_value")
    default int updateByPrimaryKey(ConfigurationValueRecord record) {
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

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.576+02:00", comments="Source Table: configuration_value")
    default int updateByPrimaryKeySelective(ConfigurationValueRecord record) {
        return UpdateDSL.updateWithMapper(this::update, configurationValueRecord)
                .set(configurationId).equalToWhenPresent(record::getConfigurationId)
                .set(configurationAttributeId).equalToWhenPresent(record::getConfigurationAttributeId)
                .set(listIndex).equalToWhenPresent(record::getListIndex)
                .set(value).equalToWhenPresent(record::getValue)
                .set(text).equalToWhenPresent(record::getText)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }
}