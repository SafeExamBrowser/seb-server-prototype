package org.eth.demo.sebserver.batis.gen.mapper;

import static org.eth.demo.sebserver.batis.gen.mapper.InstitutionRecordDynamicSqlSupport.*;
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
import org.eth.demo.sebserver.batis.gen.model.InstitutionRecord;
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
public interface InstitutionRecordMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.121+02:00", comments="Source Table: institution")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.121+02:00", comments="Source Table: institution")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.121+02:00", comments="Source Table: institution")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<InstitutionRecord> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.121+02:00", comments="Source Table: institution")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({
        @Arg(column="id", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true),
        @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="authtype", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    InstitutionRecord selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.121+02:00", comments="Source Table: institution")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({
        @Arg(column="id", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true),
        @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="authtype", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    List<InstitutionRecord> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.121+02:00", comments="Source Table: institution")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.122+02:00", comments="Source Table: institution")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<Long>> countByExample() {
        return SelectDSL.selectWithMapper(this::count, SqlBuilder.count())
                .from(institutionRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.122+02:00", comments="Source Table: institution")
    default DeleteDSL<MyBatis3DeleteModelAdapter<Integer>> deleteByExample() {
        return DeleteDSL.deleteFromWithMapper(this::delete, institutionRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.122+02:00", comments="Source Table: institution")
    default int deleteByPrimaryKey(Long id_) {
        return DeleteDSL.deleteFromWithMapper(this::delete, institutionRecord)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.122+02:00", comments="Source Table: institution")
    default int insert(InstitutionRecord record) {
        return insert(SqlBuilder.insert(record)
                .into(institutionRecord)
                .map(name).toProperty("name")
                .map(authtype).toProperty("authtype")
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.122+02:00", comments="Source Table: institution")
    default int insertSelective(InstitutionRecord record) {
        return insert(SqlBuilder.insert(record)
                .into(institutionRecord)
                .map(name).toPropertyWhenPresent("name", record::getName)
                .map(authtype).toPropertyWhenPresent("authtype", record::getAuthtype)
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.122+02:00", comments="Source Table: institution")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<InstitutionRecord>>> selectByExample() {
        return SelectDSL.selectWithMapper(this::selectMany, id, name, authtype)
                .from(institutionRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.122+02:00", comments="Source Table: institution")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<InstitutionRecord>>> selectDistinctByExample() {
        return SelectDSL.selectDistinctWithMapper(this::selectMany, id, name, authtype)
                .from(institutionRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.123+02:00", comments="Source Table: institution")
    default InstitutionRecord selectByPrimaryKey(Long id_) {
        return SelectDSL.selectWithMapper(this::selectOne, id, name, authtype)
                .from(institutionRecord)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.123+02:00", comments="Source Table: institution")
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExample(InstitutionRecord record) {
        return UpdateDSL.updateWithMapper(this::update, institutionRecord)
                .set(name).equalTo(record::getName)
                .set(authtype).equalTo(record::getAuthtype);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.123+02:00", comments="Source Table: institution")
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExampleSelective(InstitutionRecord record) {
        return UpdateDSL.updateWithMapper(this::update, institutionRecord)
                .set(name).equalToWhenPresent(record::getName)
                .set(authtype).equalToWhenPresent(record::getAuthtype);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.123+02:00", comments="Source Table: institution")
    default int updateByPrimaryKey(InstitutionRecord record) {
        return UpdateDSL.updateWithMapper(this::update, institutionRecord)
                .set(name).equalTo(record::getName)
                .set(authtype).equalTo(record::getAuthtype)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.123+02:00", comments="Source Table: institution")
    default int updateByPrimaryKeySelective(InstitutionRecord record) {
        return UpdateDSL.updateWithMapper(this::update, institutionRecord)
                .set(name).equalToWhenPresent(record::getName)
                .set(authtype).equalToWhenPresent(record::getAuthtype)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator",comments="Source Table: exam")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({@Arg(column="id", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true)})
    List<Long> selectIds(SelectStatementProvider select);

    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<Long>>> selectIdsByExample() {
        return SelectDSL.selectDistinctWithMapper(this::selectIds, id)
                        .from(institutionRecord);
    }
}