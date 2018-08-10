package org.eth.demo.sebserver.batis.gen.mapper;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.joda.time.DateTime;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class ExamRecordDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T15:49:36.983+02:00", comments="Source Table: exam")
    public static final ExamRecord examRecord = new ExamRecord();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T15:49:36.983+02:00", comments="Source field: exam.id")
    public static final SqlColumn<Long> id = examRecord.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T15:49:36.983+02:00", comments="Source field: exam.institution_id")
    public static final SqlColumn<Long> institutionId = examRecord.institutionId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T15:49:36.983+02:00", comments="Source field: exam.owner_id")
    public static final SqlColumn<Long> ownerId = examRecord.ownerId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T15:49:36.984+02:00", comments="Source field: exam.name")
    public static final SqlColumn<String> name = examRecord.name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T15:49:36.984+02:00", comments="Source field: exam.status")
    public static final SqlColumn<String> status = examRecord.status;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T15:49:36.984+02:00", comments="Source field: exam.start_time")
    public static final SqlColumn<DateTime> startTime = examRecord.startTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T15:49:36.984+02:00", comments="Source field: exam.end_time")
    public static final SqlColumn<DateTime> endTime = examRecord.endTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T15:49:36.984+02:00", comments="Source field: exam.lms_exam_url")
    public static final SqlColumn<String> lmsExamUrl = examRecord.lmsExamUrl;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T15:49:36.983+02:00", comments="Source Table: exam")
    public static final class ExamRecord extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Long> institutionId = column("institution_id", JDBCType.BIGINT);

        public final SqlColumn<Long> ownerId = column("owner_id", JDBCType.BIGINT);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> status = column("status", JDBCType.VARCHAR);

        public final SqlColumn<DateTime> startTime = column("start_time", JDBCType.TIMESTAMP, "org.eth.demo.sebserver.batis.JodaTimeTypeResolver");

        public final SqlColumn<DateTime> endTime = column("end_time", JDBCType.TIMESTAMP, "org.eth.demo.sebserver.batis.JodaTimeTypeResolver");

        public final SqlColumn<String> lmsExamUrl = column("lms_exam_url", JDBCType.VARCHAR);

        public ExamRecord() {
            super("exam");
        }
    }
}