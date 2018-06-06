package org.eth.demo.sebserver.batis.gen.mapper;

import static org.eth.demo.sebserver.batis.gen.mapper.ConfigurationAttributeRecordDynamicSqlSupport.*;
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
import org.eth.demo.sebserver.batis.gen.model.ConfigurationAttributeRecord;
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
public interface ConfigurationAttributeRecordMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-06T16:14:12.802+02:00", comments="Source Table: configuration_attribute")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-06T16:14:12.802+02:00", comments="Source Table: configuration_attribute")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-06T16:14:12.802+02:00", comments="Source Table: configuration_attribute")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<ConfigurationAttributeRecord> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-06T16:14:12.802+02:00", comments="Source Table: configuration_attribute")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({
        @Arg(column="id", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true),
        @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="type", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="parent_id", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="orientation_id", javaType=Long.class, jdbcType=JdbcType.BIGINT)
    })
    ConfigurationAttributeRecord selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-06T16:14:12.802+02:00", comments="Source Table: configuration_attribute")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({
        @Arg(column="id", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true),
        @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="type", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="parent_id", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="orientation_id", javaType=Long.class, jdbcType=JdbcType.BIGINT)
    })
    List<ConfigurationAttributeRecord> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-06T16:14:12.803+02:00", comments="Source Table: configuration_attribute")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-06T16:14:12.803+02:00", comments="Source Table: configuration_attribute")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<Long>> countByExample() {
        return SelectDSL.selectWithMapper(this::count, SqlBuilder.count())
                .from(configurationAttributeRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-06T16:14:12.803+02:00", comments="Source Table: configuration_attribute")
    default DeleteDSL<MyBatis3DeleteModelAdapter<Integer>> deleteByExample() {
        return DeleteDSL.deleteFromWithMapper(this::delete, configurationAttributeRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-06T16:14:12.803+02:00", comments="Source Table: configuration_attribute")
    default int deleteByPrimaryKey(Long id_) {
        return DeleteDSL.deleteFromWithMapper(this::delete, configurationAttributeRecord)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-06T16:14:12.803+02:00", comments="Source Table: configuration_attribute")
    default int insert(ConfigurationAttributeRecord record) {
        return insert(SqlBuilder.insert(record)
                .into(configurationAttributeRecord)
                .map(name).toProperty("name")
                .map(type).toProperty("type")
                .map(parentId).toProperty("parentId")
                .map(orientationId).toProperty("orientationId")
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-06T16:14:12.803+02:00", comments="Source Table: configuration_attribute")
    default int insertSelective(ConfigurationAttributeRecord record) {
        return insert(SqlBuilder.insert(record)
                .into(configurationAttributeRecord)
                .map(name).toPropertyWhenPresent("name", record::getName)
                .map(type).toPropertyWhenPresent("type", record::getType)
                .map(parentId).toPropertyWhenPresent("parentId", record::getParentId)
                .map(orientationId).toPropertyWhenPresent("orientationId", record::getOrientationId)
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-06T16:14:12.803+02:00", comments="Source Table: configuration_attribute")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<ConfigurationAttributeRecord>>> selectByExample() {
        return SelectDSL.selectWithMapper(this::selectMany, id, name, type, parentId, orientationId)
                .from(configurationAttributeRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-06T16:14:12.803+02:00", comments="Source Table: configuration_attribute")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<ConfigurationAttributeRecord>>> selectDistinctByExample() {
        return SelectDSL.selectDistinctWithMapper(this::selectMany, id, name, type, parentId, orientationId)
                .from(configurationAttributeRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-06T16:14:12.803+02:00", comments="Source Table: configuration_attribute")
    default ConfigurationAttributeRecord selectByPrimaryKey(Long id_) {
        return SelectDSL.selectWithMapper(this::selectOne, id, name, type, parentId, orientationId)
                .from(configurationAttributeRecord)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-06T16:14:12.803+02:00", comments="Source Table: configuration_attribute")
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExample(ConfigurationAttributeRecord record) {
        return UpdateDSL.updateWithMapper(this::update, configurationAttributeRecord)
                .set(name).equalTo(record::getName)
                .set(type).equalTo(record::getType)
                .set(parentId).equalTo(record::getParentId)
                .set(orientationId).equalTo(record::getOrientationId);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-06T16:14:12.804+02:00", comments="Source Table: configuration_attribute")
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExampleSelective(ConfigurationAttributeRecord record) {
        return UpdateDSL.updateWithMapper(this::update, configurationAttributeRecord)
                .set(name).equalToWhenPresent(record::getName)
                .set(type).equalToWhenPresent(record::getType)
                .set(parentId).equalToWhenPresent(record::getParentId)
                .set(orientationId).equalToWhenPresent(record::getOrientationId);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-06T16:14:12.804+02:00", comments="Source Table: configuration_attribute")
    default int updateByPrimaryKey(ConfigurationAttributeRecord record) {
        return UpdateDSL.updateWithMapper(this::update, configurationAttributeRecord)
                .set(name).equalTo(record::getName)
                .set(type).equalTo(record::getType)
                .set(parentId).equalTo(record::getParentId)
                .set(orientationId).equalTo(record::getOrientationId)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-06T16:14:12.804+02:00", comments="Source Table: configuration_attribute")
    default int updateByPrimaryKeySelective(ConfigurationAttributeRecord record) {
        return UpdateDSL.updateWithMapper(this::update, configurationAttributeRecord)
                .set(name).equalToWhenPresent(record::getName)
                .set(type).equalToWhenPresent(record::getType)
                .set(parentId).equalToWhenPresent(record::getParentId)
                .set(orientationId).equalToWhenPresent(record::getOrientationId)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }
}