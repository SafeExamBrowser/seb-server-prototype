package org.eth.demo.sebserver.batis.gen.mapper;

import static org.eth.demo.sebserver.batis.gen.mapper.ExamRecordDynamicSqlSupport.*;
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
import org.eth.demo.sebserver.batis.JodaTimeTypeResolver;
import org.eth.demo.sebserver.batis.gen.model.ExamRecord;
import org.joda.time.DateTime;
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
public interface ExamRecordMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.861+02:00", comments="Source Table: exam")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.861+02:00", comments="Source Table: exam")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.861+02:00", comments="Source Table: exam")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<ExamRecord> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.861+02:00", comments="Source Table: exam")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({
        @Arg(column="id", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true),
        @Arg(column="institution_id", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="owner_id", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="status", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="start_time", javaType=DateTime.class, typeHandler=JodaTimeTypeResolver.class, jdbcType=JdbcType.TIMESTAMP),
        @Arg(column="end_time", javaType=DateTime.class, typeHandler=JodaTimeTypeResolver.class, jdbcType=JdbcType.TIMESTAMP),
        @Arg(column="lms_exam_url", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    ExamRecord selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.861+02:00", comments="Source Table: exam")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({
        @Arg(column="id", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true),
        @Arg(column="institution_id", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="owner_id", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="name", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="status", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="start_time", javaType=DateTime.class, typeHandler=JodaTimeTypeResolver.class, jdbcType=JdbcType.TIMESTAMP),
        @Arg(column="end_time", javaType=DateTime.class, typeHandler=JodaTimeTypeResolver.class, jdbcType=JdbcType.TIMESTAMP),
        @Arg(column="lms_exam_url", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    List<ExamRecord> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.862+02:00", comments="Source Table: exam")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.862+02:00", comments="Source Table: exam")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<Long>> countByExample() {
        return SelectDSL.selectWithMapper(this::count, SqlBuilder.count())
                .from(examRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.862+02:00", comments="Source Table: exam")
    default DeleteDSL<MyBatis3DeleteModelAdapter<Integer>> deleteByExample() {
        return DeleteDSL.deleteFromWithMapper(this::delete, examRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.862+02:00", comments="Source Table: exam")
    default int deleteByPrimaryKey(Long id_) {
        return DeleteDSL.deleteFromWithMapper(this::delete, examRecord)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.862+02:00", comments="Source Table: exam")
    default int insert(ExamRecord record) {
        return insert(SqlBuilder.insert(record)
                .into(examRecord)
                .map(institutionId).toProperty("institutionId")
                .map(ownerId).toProperty("ownerId")
                .map(name).toProperty("name")
                .map(status).toProperty("status")
                .map(startTime).toProperty("startTime")
                .map(endTime).toProperty("endTime")
                .map(lmsExamUrl).toProperty("lmsExamUrl")
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.862+02:00", comments="Source Table: exam")
    default int insertSelective(ExamRecord record) {
        return insert(SqlBuilder.insert(record)
                .into(examRecord)
                .map(institutionId).toPropertyWhenPresent("institutionId", record::getInstitutionId)
                .map(ownerId).toPropertyWhenPresent("ownerId", record::getOwnerId)
                .map(name).toPropertyWhenPresent("name", record::getName)
                .map(status).toPropertyWhenPresent("status", record::getStatus)
                .map(startTime).toPropertyWhenPresent("startTime", record::getStartTime)
                .map(endTime).toPropertyWhenPresent("endTime", record::getEndTime)
                .map(lmsExamUrl).toPropertyWhenPresent("lmsExamUrl", record::getLmsExamUrl)
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.862+02:00", comments="Source Table: exam")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<ExamRecord>>> selectByExample() {
        return SelectDSL.selectWithMapper(this::selectMany, id, institutionId, ownerId, name, status, startTime, endTime, lmsExamUrl)
                .from(examRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.862+02:00", comments="Source Table: exam")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<ExamRecord>>> selectDistinctByExample() {
        return SelectDSL.selectDistinctWithMapper(this::selectMany, id, institutionId, ownerId, name, status, startTime, endTime, lmsExamUrl)
                .from(examRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.862+02:00", comments="Source Table: exam")
    default ExamRecord selectByPrimaryKey(Long id_) {
        return SelectDSL.selectWithMapper(this::selectOne, id, institutionId, ownerId, name, status, startTime, endTime, lmsExamUrl)
                .from(examRecord)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.862+02:00", comments="Source Table: exam")
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExample(ExamRecord record) {
        return UpdateDSL.updateWithMapper(this::update, examRecord)
                .set(institutionId).equalTo(record::getInstitutionId)
                .set(ownerId).equalTo(record::getOwnerId)
                .set(name).equalTo(record::getName)
                .set(status).equalTo(record::getStatus)
                .set(startTime).equalTo(record::getStartTime)
                .set(endTime).equalTo(record::getEndTime)
                .set(lmsExamUrl).equalTo(record::getLmsExamUrl);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.862+02:00", comments="Source Table: exam")
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExampleSelective(ExamRecord record) {
        return UpdateDSL.updateWithMapper(this::update, examRecord)
                .set(institutionId).equalToWhenPresent(record::getInstitutionId)
                .set(ownerId).equalToWhenPresent(record::getOwnerId)
                .set(name).equalToWhenPresent(record::getName)
                .set(status).equalToWhenPresent(record::getStatus)
                .set(startTime).equalToWhenPresent(record::getStartTime)
                .set(endTime).equalToWhenPresent(record::getEndTime)
                .set(lmsExamUrl).equalToWhenPresent(record::getLmsExamUrl);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.862+02:00", comments="Source Table: exam")
    default int updateByPrimaryKey(ExamRecord record) {
        return UpdateDSL.updateWithMapper(this::update, examRecord)
                .set(institutionId).equalTo(record::getInstitutionId)
                .set(ownerId).equalTo(record::getOwnerId)
                .set(name).equalTo(record::getName)
                .set(status).equalTo(record::getStatus)
                .set(startTime).equalTo(record::getStartTime)
                .set(endTime).equalTo(record::getEndTime)
                .set(lmsExamUrl).equalTo(record::getLmsExamUrl)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.863+02:00", comments="Source Table: exam")
    default int updateByPrimaryKeySelective(ExamRecord record) {
        return UpdateDSL.updateWithMapper(this::update, examRecord)
                .set(institutionId).equalToWhenPresent(record::getInstitutionId)
                .set(ownerId).equalToWhenPresent(record::getOwnerId)
                .set(name).equalToWhenPresent(record::getName)
                .set(status).equalToWhenPresent(record::getStatus)
                .set(startTime).equalToWhenPresent(record::getStartTime)
                .set(endTime).equalToWhenPresent(record::getEndTime)
                .set(lmsExamUrl).equalToWhenPresent(record::getLmsExamUrl)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }
}