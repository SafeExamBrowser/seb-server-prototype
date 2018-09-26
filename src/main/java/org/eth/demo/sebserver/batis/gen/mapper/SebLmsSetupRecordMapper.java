package org.eth.demo.sebserver.batis.gen.mapper;

import static org.eth.demo.sebserver.batis.gen.mapper.SebLmsSetupRecordDynamicSqlSupport.*;
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
import org.eth.demo.sebserver.batis.gen.model.SebLmsSetupRecord;
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
public interface SebLmsSetupRecordMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.658+02:00", comments="Source Table: seb_lms_setup")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.658+02:00", comments="Source Table: seb_lms_setup")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.658+02:00", comments="Source Table: seb_lms_setup")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<SebLmsSetupRecord> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.658+02:00", comments="Source Table: seb_lms_setup")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({
        @Arg(column="id", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true),
        @Arg(column="institution_id", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="lms_type", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="lms_clientname", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="lms_clientsecret", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="lms_url", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="seb_clientname", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="seb_clientsecret", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    SebLmsSetupRecord selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.658+02:00", comments="Source Table: seb_lms_setup")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ConstructorArgs({
        @Arg(column="id", javaType=Long.class, jdbcType=JdbcType.BIGINT, id=true),
        @Arg(column="institution_id", javaType=Long.class, jdbcType=JdbcType.BIGINT),
        @Arg(column="lms_type", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="lms_clientname", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="lms_clientsecret", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="lms_url", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="seb_clientname", javaType=String.class, jdbcType=JdbcType.VARCHAR),
        @Arg(column="seb_clientsecret", javaType=String.class, jdbcType=JdbcType.VARCHAR)
    })
    List<SebLmsSetupRecord> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.658+02:00", comments="Source Table: seb_lms_setup")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.658+02:00", comments="Source Table: seb_lms_setup")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<Long>> countByExample() {
        return SelectDSL.selectWithMapper(this::count, SqlBuilder.count())
                .from(sebLmsSetupRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.658+02:00", comments="Source Table: seb_lms_setup")
    default DeleteDSL<MyBatis3DeleteModelAdapter<Integer>> deleteByExample() {
        return DeleteDSL.deleteFromWithMapper(this::delete, sebLmsSetupRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.658+02:00", comments="Source Table: seb_lms_setup")
    default int deleteByPrimaryKey(Long id_) {
        return DeleteDSL.deleteFromWithMapper(this::delete, sebLmsSetupRecord)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.658+02:00", comments="Source Table: seb_lms_setup")
    default int insert(SebLmsSetupRecord record) {
        return insert(SqlBuilder.insert(record)
                .into(sebLmsSetupRecord)
                .map(institutionId).toProperty("institutionId")
                .map(lmsType).toProperty("lmsType")
                .map(lmsClientname).toProperty("lmsClientname")
                .map(lmsClientsecret).toProperty("lmsClientsecret")
                .map(lmsUrl).toProperty("lmsUrl")
                .map(sebClientname).toProperty("sebClientname")
                .map(sebClientsecret).toProperty("sebClientsecret")
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.659+02:00", comments="Source Table: seb_lms_setup")
    default int insertSelective(SebLmsSetupRecord record) {
        return insert(SqlBuilder.insert(record)
                .into(sebLmsSetupRecord)
                .map(institutionId).toPropertyWhenPresent("institutionId", record::getInstitutionId)
                .map(lmsType).toPropertyWhenPresent("lmsType", record::getLmsType)
                .map(lmsClientname).toPropertyWhenPresent("lmsClientname", record::getLmsClientname)
                .map(lmsClientsecret).toPropertyWhenPresent("lmsClientsecret", record::getLmsClientsecret)
                .map(lmsUrl).toPropertyWhenPresent("lmsUrl", record::getLmsUrl)
                .map(sebClientname).toPropertyWhenPresent("sebClientname", record::getSebClientname)
                .map(sebClientsecret).toPropertyWhenPresent("sebClientsecret", record::getSebClientsecret)
                .build()
                .render(RenderingStrategy.MYBATIS3));
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.659+02:00", comments="Source Table: seb_lms_setup")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<SebLmsSetupRecord>>> selectByExample() {
        return SelectDSL.selectWithMapper(this::selectMany, id, institutionId, lmsType, lmsClientname, lmsClientsecret, lmsUrl, sebClientname, sebClientsecret)
                .from(sebLmsSetupRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.659+02:00", comments="Source Table: seb_lms_setup")
    default QueryExpressionDSL<MyBatis3SelectModelAdapter<List<SebLmsSetupRecord>>> selectDistinctByExample() {
        return SelectDSL.selectDistinctWithMapper(this::selectMany, id, institutionId, lmsType, lmsClientname, lmsClientsecret, lmsUrl, sebClientname, sebClientsecret)
                .from(sebLmsSetupRecord);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.659+02:00", comments="Source Table: seb_lms_setup")
    default SebLmsSetupRecord selectByPrimaryKey(Long id_) {
        return SelectDSL.selectWithMapper(this::selectOne, id, institutionId, lmsType, lmsClientname, lmsClientsecret, lmsUrl, sebClientname, sebClientsecret)
                .from(sebLmsSetupRecord)
                .where(id, isEqualTo(id_))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.659+02:00", comments="Source Table: seb_lms_setup")
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExample(SebLmsSetupRecord record) {
        return UpdateDSL.updateWithMapper(this::update, sebLmsSetupRecord)
                .set(institutionId).equalTo(record::getInstitutionId)
                .set(lmsType).equalTo(record::getLmsType)
                .set(lmsClientname).equalTo(record::getLmsClientname)
                .set(lmsClientsecret).equalTo(record::getLmsClientsecret)
                .set(lmsUrl).equalTo(record::getLmsUrl)
                .set(sebClientname).equalTo(record::getSebClientname)
                .set(sebClientsecret).equalTo(record::getSebClientsecret);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.659+02:00", comments="Source Table: seb_lms_setup")
    default UpdateDSL<MyBatis3UpdateModelAdapter<Integer>> updateByExampleSelective(SebLmsSetupRecord record) {
        return UpdateDSL.updateWithMapper(this::update, sebLmsSetupRecord)
                .set(institutionId).equalToWhenPresent(record::getInstitutionId)
                .set(lmsType).equalToWhenPresent(record::getLmsType)
                .set(lmsClientname).equalToWhenPresent(record::getLmsClientname)
                .set(lmsClientsecret).equalToWhenPresent(record::getLmsClientsecret)
                .set(lmsUrl).equalToWhenPresent(record::getLmsUrl)
                .set(sebClientname).equalToWhenPresent(record::getSebClientname)
                .set(sebClientsecret).equalToWhenPresent(record::getSebClientsecret);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.659+02:00", comments="Source Table: seb_lms_setup")
    default int updateByPrimaryKey(SebLmsSetupRecord record) {
        return UpdateDSL.updateWithMapper(this::update, sebLmsSetupRecord)
                .set(institutionId).equalTo(record::getInstitutionId)
                .set(lmsType).equalTo(record::getLmsType)
                .set(lmsClientname).equalTo(record::getLmsClientname)
                .set(lmsClientsecret).equalTo(record::getLmsClientsecret)
                .set(lmsUrl).equalTo(record::getLmsUrl)
                .set(sebClientname).equalTo(record::getSebClientname)
                .set(sebClientsecret).equalTo(record::getSebClientsecret)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.659+02:00", comments="Source Table: seb_lms_setup")
    default int updateByPrimaryKeySelective(SebLmsSetupRecord record) {
        return UpdateDSL.updateWithMapper(this::update, sebLmsSetupRecord)
                .set(institutionId).equalToWhenPresent(record::getInstitutionId)
                .set(lmsType).equalToWhenPresent(record::getLmsType)
                .set(lmsClientname).equalToWhenPresent(record::getLmsClientname)
                .set(lmsClientsecret).equalToWhenPresent(record::getLmsClientsecret)
                .set(lmsUrl).equalToWhenPresent(record::getLmsUrl)
                .set(sebClientname).equalToWhenPresent(record::getSebClientname)
                .set(sebClientsecret).equalToWhenPresent(record::getSebClientsecret)
                .where(id, isEqualTo(record::getId))
                .build()
                .execute();
    }
}