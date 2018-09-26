package org.eth.demo.sebserver.batis.gen.mapper;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class ExamRecordDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.631+02:00", comments="Source Table: exam")
    public static final ExamRecord examRecord = new ExamRecord();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.631+02:00", comments="Source field: exam.id")
    public static final SqlColumn<Long> id = examRecord.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.631+02:00", comments="Source field: exam.lms_setup_id")
    public static final SqlColumn<Long> lmsSetupId = examRecord.lmsSetupId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.631+02:00", comments="Source field: exam.external_uuid")
    public static final SqlColumn<String> externalUuid = examRecord.externalUuid;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.631+02:00", comments="Source Table: exam")
    public static final class ExamRecord extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Long> lmsSetupId = column("lms_setup_id", JDBCType.BIGINT);

        public final SqlColumn<String> externalUuid = column("external_uuid", JDBCType.VARCHAR);

        public ExamRecord() {
            super("exam");
        }
    }
}