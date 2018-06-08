package org.eth.demo.sebserver.batis.gen.mapper;

import static org.eth.demo.sebserver.batis.gen.mapper.ExamConfigurationMapRecordDynamicSqlSupport.*;
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
import org.eth.demo.sebserver.batis.gen.model.ExamConfigurationMapRecord;
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
public interface ExamConfigurationMapRecordMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.891+02:00", comments="Source Table: exam_configuration_map")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.891+02:00", comments="Source Table: exam_configuration_map")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.891+02:00", comments="Source Table: exam_configuration_map")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<ExamConfigurationMapRecord> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.892+02:00", comments="Source Table: exam_configuration_map")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({
        @Arg(column="id", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true),
        @Arg(column="exam_id", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="configuration_id", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="client_info", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    ExamConfigurationMapRecord selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.892+02:00", comments="Source Table: exam_configuration_map")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({
        @Arg(column="id", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true),
        @Arg(column="exam_id", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="configuration_id", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="client_info", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    List<ExamConfigurationMapRecord> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.892+02:00", comments="Source Table: exam_configuration_map")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.892+02:00", comments="Source Table: exam_configuration_map")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<Long>> countByExample() {
        return SelectDSL.selectWithMapper(this::count, SqlBuilder.count())
                .from(examConfigurationMapRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.892+02:00", comments="Source Table: exam_configuration_map")
    default DeleteDSL<MyBatis3DeleteModelAdapter<Integer>> deleteByExample() {
        return DeleteDSL.deleteFromWithMapper(this::delete, examConfigurationMapRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.892+02:00", comments="Source Table: exam_configuration_map")
    default int deleteByPrimaryKey(Long id_) {
        return DeleteDSL.deleteFromWithMapper(this::delete, examConfigurationMapRecord)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.892+02:00", comments="Source Table: exam_configuration_map")
    default int insert(ExamConfigurationMapRecord record) {
        return insert(SqlBuilder.insert(record)
                .into(examConfigurationMapRecord)
                .map(examId).toProperty("examId")
                .map(configurationId).toProperty("configurationId")
                .map(clientInfo).toProperty("clientInfo")
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.892+02:00", comments="Source Table: exam_configuration_map")
    default int insertSelective(ExamConfigurationMapRecord record) {
        return insert(SqlBuilder.insert(record)
                .into(examConfigurationMapRecord)
                .map(examId).toPropertyWhenPresent("examId", record::getExamId)
                .map(configurationId).toPropertyWhenPresent("configurationId", record::getConfigurationId)
                .map(clientInfo).toPropertyWhenPresent("clientInfo", record::getClientInfo)
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.893+02:00", comments="Source Table: exam_configuration_map")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<ExamConfigurationMapRecord>>> selectByExample() {
        return SelectDSL.selectWithMapper(this::selectMany, id, examId, configurationId, clientInfo)
                .from(examConfigurationMapRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.893+02:00", comments="Source Table: exam_configuration_map")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<ExamConfigurationMapRecord>>> selectDistinctByExample() {
        return SelectDSL.selectDistinctWithMapper(this::selectMany, id, examId, configurationId, clientInfo)
                .from(examConfigurationMapRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.893+02:00", comments="Source Table: exam_configuration_map")
    default ExamConfigurationMapRecord selectByPrimaryKey(Long id_) {
        return SelectDSL.selectWithMapper(this::selectOne, id, examId, configurationId, clientInfo)
                .from(examConfigurationMapRecord)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.893+02:00", comments="Source Table: exam_configuration_map")
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExample(ExamConfigurationMapRecord record) {
        return UpdateDSL.updateWithMapper(this::update, examConfigurationMapRecord)
                .set(examId).equalTo(record::getExamId)
                .set(configurationId).equalTo(record::getConfigurationId)
                .set(clientInfo).equalTo(record::getClientInfo);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.893+02:00", comments="Source Table: exam_configuration_map")
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExampleSelective(ExamConfigurationMapRecord record) {
        return UpdateDSL.updateWithMapper(this::update, examConfigurationMapRecord)
                .set(examId).equalToWhenPresent(record::getExamId)
                .set(configurationId).equalToWhenPresent(record::getConfigurationId)
                .set(clientInfo).equalToWhenPresent(record::getClientInfo);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.894+02:00", comments="Source Table: exam_configuration_map")
    default int updateByPrimaryKey(ExamConfigurationMapRecord record) {
        return UpdateDSL.updateWithMapper(this::update, examConfigurationMapRecord)
                .set(examId).equalTo(record::getExamId)
                .set(configurationId).equalTo(record::getConfigurationId)
                .set(clientInfo).equalTo(record::getClientInfo)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.894+02:00", comments="Source Table: exam_configuration_map")
    default int updateByPrimaryKeySelective(ExamConfigurationMapRecord record) {
        return UpdateDSL.updateWithMapper(this::update, examConfigurationMapRecord)
                .set(examId).equalToWhenPresent(record::getExamId)
                .set(configurationId).equalToWhenPresent(record::getConfigurationId)
                .set(clientInfo).equalToWhenPresent(record::getClientInfo)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }
}