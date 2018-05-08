package org.eth.demo.sebserver.batis.gen.mapper;

import static org.eth.demo.sebserver.batis.gen.mapper.IndicatorValueRecordDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import java.math.BigDecimal;
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
import org.eth.demo.sebserver.batis.gen.model.IndicatorValueRecord;
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
public interface IndicatorValueRecordMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.905+02:00", comments="Source Table: indicator_value")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.905+02:00", comments="Source Table: indicator_value")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.906+02:00", comments="Source Table: indicator_value")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<IndicatorValueRecord> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.906+02:00", comments="Source Table: indicator_value")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({
        @Arg(column="id", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true),
        @Arg(column="indicator_id", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="client_id", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="value", javaType=BigDecimal.class, jdbcType=JdbcType.DECIMAL),
        @Arg(column="timestamp", javaType=Long.class, jdbcType=JdbcType.BIGINT)
    })
    IndicatorValueRecord selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.906+02:00", comments="Source Table: indicator_value")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({
        @Arg(column="id", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true),
        @Arg(column="indicator_id", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="client_id", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="value", javaType=BigDecimal.class, jdbcType=JdbcType.DECIMAL),
        @Arg(column="timestamp", javaType=Long.class, jdbcType=JdbcType.BIGINT)
    })
    List<IndicatorValueRecord> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.906+02:00", comments="Source Table: indicator_value")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.906+02:00", comments="Source Table: indicator_value")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<Long>> countByExample() {
        return SelectDSL.selectWithMapper(this::count, SqlBuilder.count())
                .from(indicatorValueRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.906+02:00", comments="Source Table: indicator_value")
    default DeleteDSL<MyBatis3DeleteModelAdapter<Integer>> deleteByExample() {
        return DeleteDSL.deleteFromWithMapper(this::delete, indicatorValueRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.906+02:00", comments="Source Table: indicator_value")
    default int deleteByPrimaryKey(Long id_) {
        return DeleteDSL.deleteFromWithMapper(this::delete, indicatorValueRecord)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.906+02:00", comments="Source Table: indicator_value")
    default int insert(IndicatorValueRecord record) {
        return insert(SqlBuilder.insert(record)
                .into(indicatorValueRecord)
                .map(indicatorId).toProperty("indicatorId")
                .map(clientId).toProperty("clientId")
                .map(value).toProperty("value")
                .map(timestamp).toProperty("timestamp")
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.907+02:00", comments="Source Table: indicator_value")
    default int insertSelective(IndicatorValueRecord record) {
        return insert(SqlBuilder.insert(record)
                .into(indicatorValueRecord)
                .map(indicatorId).toPropertyWhenPresent("indicatorId", record::getIndicatorId)
                .map(clientId).toPropertyWhenPresent("clientId", record::getClientId)
                .map(value).toPropertyWhenPresent("value", record::getValue)
                .map(timestamp).toPropertyWhenPresent("timestamp", record::getTimestamp)
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.907+02:00", comments="Source Table: indicator_value")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<IndicatorValueRecord>>> selectByExample() {
        return SelectDSL.selectWithMapper(this::selectMany, id, indicatorId, clientId, value, timestamp)
                .from(indicatorValueRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.907+02:00", comments="Source Table: indicator_value")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<IndicatorValueRecord>>> selectDistinctByExample() {
        return SelectDSL.selectDistinctWithMapper(this::selectMany, id, indicatorId, clientId, value, timestamp)
                .from(indicatorValueRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.907+02:00", comments="Source Table: indicator_value")
    default IndicatorValueRecord selectByPrimaryKey(Long id_) {
        return SelectDSL.selectWithMapper(this::selectOne, id, indicatorId, clientId, value, timestamp)
                .from(indicatorValueRecord)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.907+02:00", comments="Source Table: indicator_value")
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExample(IndicatorValueRecord record) {
        return UpdateDSL.updateWithMapper(this::update, indicatorValueRecord)
                .set(indicatorId).equalTo(record::getIndicatorId)
                .set(clientId).equalTo(record::getClientId)
                .set(value).equalTo(record::getValue)
                .set(timestamp).equalTo(record::getTimestamp);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.907+02:00", comments="Source Table: indicator_value")
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExampleSelective(IndicatorValueRecord record) {
        return UpdateDSL.updateWithMapper(this::update, indicatorValueRecord)
                .set(indicatorId).equalToWhenPresent(record::getIndicatorId)
                .set(clientId).equalToWhenPresent(record::getClientId)
                .set(value).equalToWhenPresent(record::getValue)
                .set(timestamp).equalToWhenPresent(record::getTimestamp);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.907+02:00", comments="Source Table: indicator_value")
    default int updateByPrimaryKey(IndicatorValueRecord record) {
        return UpdateDSL.updateWithMapper(this::update, indicatorValueRecord)
                .set(indicatorId).equalTo(record::getIndicatorId)
                .set(clientId).equalTo(record::getClientId)
                .set(value).equalTo(record::getValue)
                .set(timestamp).equalTo(record::getTimestamp)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.907+02:00", comments="Source Table: indicator_value")
    default int updateByPrimaryKeySelective(IndicatorValueRecord record) {
        return UpdateDSL.updateWithMapper(this::update, indicatorValueRecord)
                .set(indicatorId).equalToWhenPresent(record::getIndicatorId)
                .set(clientId).equalToWhenPresent(record::getClientId)
                .set(value).equalToWhenPresent(record::getValue)
                .set(timestamp).equalToWhenPresent(record::getTimestamp)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }
}