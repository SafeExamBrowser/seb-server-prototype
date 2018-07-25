package org.eth.demo.sebserver.batis.gen.mapper;

import static org.eth.demo.sebserver.batis.gen.mapper.ConfigurationRecordDynamicSqlSupport.*;
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
import org.eth.demo.sebserver.batis.gen.model.ConfigurationRecord;
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
public interface ConfigurationRecordMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.541+02:00", comments="Source Table: configuration")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.543+02:00", comments="Source Table: configuration")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.544+02:00", comments="Source Table: configuration")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<ConfigurationRecord> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.547+02:00", comments="Source Table: configuration")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({
        @Arg(column="id", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true),
        @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="type", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="owner_id", javaType=Long.class, jdbcType=JdbcType.BIGINT)
    })
    ConfigurationRecord selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.551+02:00", comments="Source Table: configuration")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({
        @Arg(column="id", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true),
        @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="type", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="owner_id", javaType=Long.class, jdbcType=JdbcType.BIGINT)
    })
    List<ConfigurationRecord> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.553+02:00", comments="Source Table: configuration")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.554+02:00", comments="Source Table: configuration")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<Long>> countByExample() {
        return SelectDSL.selectWithMapper(this::count, SqlBuilder.count())
                .from(configurationRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.555+02:00", comments="Source Table: configuration")
    default DeleteDSL<MyBatis3DeleteModelAdapter<Integer>> deleteByExample() {
        return DeleteDSL.deleteFromWithMapper(this::delete, configurationRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.556+02:00", comments="Source Table: configuration")
    default int deleteByPrimaryKey(Long id_) {
        return DeleteDSL.deleteFromWithMapper(this::delete, configurationRecord)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.557+02:00", comments="Source Table: configuration")
    default int insert(ConfigurationRecord record) {
        return insert(SqlBuilder.insert(record)
                .into(configurationRecord)
                .map(name).toProperty("name")
                .map(type).toProperty("type")
                .map(ownerId).toProperty("ownerId")
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.559+02:00", comments="Source Table: configuration")
    default int insertSelective(ConfigurationRecord record) {
        return insert(SqlBuilder.insert(record)
                .into(configurationRecord)
                .map(name).toPropertyWhenPresent("name", record::getName)
                .map(type).toPropertyWhenPresent("type", record::getType)
                .map(ownerId).toPropertyWhenPresent("ownerId", record::getOwnerId)
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.560+02:00", comments="Source Table: configuration")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<ConfigurationRecord>>> selectByExample() {
        return SelectDSL.selectWithMapper(this::selectMany, id, name, type, ownerId)
                .from(configurationRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.561+02:00", comments="Source Table: configuration")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<ConfigurationRecord>>> selectDistinctByExample() {
        return SelectDSL.selectDistinctWithMapper(this::selectMany, id, name, type, ownerId)
                .from(configurationRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.562+02:00", comments="Source Table: configuration")
    default ConfigurationRecord selectByPrimaryKey(Long id_) {
        return SelectDSL.selectWithMapper(this::selectOne, id, name, type, ownerId)
                .from(configurationRecord)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.563+02:00", comments="Source Table: configuration")
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExample(ConfigurationRecord record) {
        return UpdateDSL.updateWithMapper(this::update, configurationRecord)
                .set(name).equalTo(record::getName)
                .set(type).equalTo(record::getType)
                .set(ownerId).equalTo(record::getOwnerId);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.565+02:00", comments="Source Table: configuration")
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExampleSelective(ConfigurationRecord record) {
        return UpdateDSL.updateWithMapper(this::update, configurationRecord)
                .set(name).equalToWhenPresent(record::getName)
                .set(type).equalToWhenPresent(record::getType)
                .set(ownerId).equalToWhenPresent(record::getOwnerId);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.567+02:00", comments="Source Table: configuration")
    default int updateByPrimaryKey(ConfigurationRecord record) {
        return UpdateDSL.updateWithMapper(this::update, configurationRecord)
                .set(name).equalTo(record::getName)
                .set(type).equalTo(record::getType)
                .set(ownerId).equalTo(record::getOwnerId)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.568+02:00", comments="Source Table: configuration")
    default int updateByPrimaryKeySelective(ConfigurationRecord record) {
        return UpdateDSL.updateWithMapper(this::update, configurationRecord)
                .set(name).equalToWhenPresent(record::getName)
                .set(type).equalToWhenPresent(record::getType)
                .set(ownerId).equalToWhenPresent(record::getOwnerId)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }
}