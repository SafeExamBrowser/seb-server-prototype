package org.eth.demo.sebserver.batis.gen.mapper;

import static org.eth.demo.sebserver.batis.gen.mapper.ClientConnectionRecordDynamicSqlSupport.*;
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
import org.eth.demo.sebserver.batis.gen.model.ClientConnectionRecord;
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
public interface ClientConnectionRecordMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-30T08:49:18.487+02:00", comments="Source Table: client_connection")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-30T08:49:18.487+02:00", comments="Source Table: client_connection")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-30T08:49:18.487+02:00", comments="Source Table: client_connection")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<ClientConnectionRecord> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-30T08:49:18.487+02:00", comments="Source Table: client_connection")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({
        @Arg(column="id", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true),
        @Arg(column="exam_id", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="token", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    ClientConnectionRecord selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-30T08:49:18.488+02:00", comments="Source Table: client_connection")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({
        @Arg(column="id", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true),
        @Arg(column="exam_id", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="token", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    List<ClientConnectionRecord> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-30T08:49:18.488+02:00", comments="Source Table: client_connection")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-30T08:49:18.488+02:00", comments="Source Table: client_connection")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<Long>> countByExample() {
        return SelectDSL.selectWithMapper(this::count, SqlBuilder.count())
                .from(clientConnectionRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-30T08:49:18.488+02:00", comments="Source Table: client_connection")
    default DeleteDSL<MyBatis3DeleteModelAdapter<Integer>> deleteByExample() {
        return DeleteDSL.deleteFromWithMapper(this::delete, clientConnectionRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-30T08:49:18.488+02:00", comments="Source Table: client_connection")
    default int deleteByPrimaryKey(Long id_) {
        return DeleteDSL.deleteFromWithMapper(this::delete, clientConnectionRecord)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-30T08:49:18.488+02:00", comments="Source Table: client_connection")
    default int insert(ClientConnectionRecord record) {
        return insert(SqlBuilder.insert(record)
                .into(clientConnectionRecord)
                .map(examId).toProperty("examId")
                .map(token).toProperty("token")
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-30T08:49:18.488+02:00", comments="Source Table: client_connection")
    default int insertSelective(ClientConnectionRecord record) {
        return insert(SqlBuilder.insert(record)
                .into(clientConnectionRecord)
                .map(examId).toPropertyWhenPresent("examId", record::getExamId)
                .map(token).toPropertyWhenPresent("token", record::getToken)
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-30T08:49:18.488+02:00", comments="Source Table: client_connection")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<ClientConnectionRecord>>> selectByExample() {
        return SelectDSL.selectWithMapper(this::selectMany, id, examId, token)
                .from(clientConnectionRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-30T08:49:18.488+02:00", comments="Source Table: client_connection")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<ClientConnectionRecord>>> selectDistinctByExample() {
        return SelectDSL.selectDistinctWithMapper(this::selectMany, id, examId, token)
                .from(clientConnectionRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-30T08:49:18.488+02:00", comments="Source Table: client_connection")
    default ClientConnectionRecord selectByPrimaryKey(Long id_) {
        return SelectDSL.selectWithMapper(this::selectOne, id, examId, token)
                .from(clientConnectionRecord)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-30T08:49:18.488+02:00", comments="Source Table: client_connection")
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExample(ClientConnectionRecord record) {
        return UpdateDSL.updateWithMapper(this::update, clientConnectionRecord)
                .set(examId).equalTo(record::getExamId)
                .set(token).equalTo(record::getToken);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-30T08:49:18.488+02:00", comments="Source Table: client_connection")
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExampleSelective(ClientConnectionRecord record) {
        return UpdateDSL.updateWithMapper(this::update, clientConnectionRecord)
                .set(examId).equalToWhenPresent(record::getExamId)
                .set(token).equalToWhenPresent(record::getToken);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-30T08:49:18.488+02:00", comments="Source Table: client_connection")
    default int updateByPrimaryKey(ClientConnectionRecord record) {
        return UpdateDSL.updateWithMapper(this::update, clientConnectionRecord)
                .set(examId).equalTo(record::getExamId)
                .set(token).equalTo(record::getToken)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-30T08:49:18.488+02:00", comments="Source Table: client_connection")
    default int updateByPrimaryKeySelective(ClientConnectionRecord record) {
        return UpdateDSL.updateWithMapper(this::update, clientConnectionRecord)
                .set(examId).equalToWhenPresent(record::getExamId)
                .set(token).equalToWhenPresent(record::getToken)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }
}