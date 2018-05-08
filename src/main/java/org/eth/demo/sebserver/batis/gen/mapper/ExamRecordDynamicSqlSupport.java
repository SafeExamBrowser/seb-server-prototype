package org.eth.demo.sebserver.batis.gen.mapper;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class ExamRecordDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.853+02:00", comments="Source Table: exam")
    public static final ExamRecord examRecord = new ExamRecord();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.856+02:00", comments="Source field: exam.id")
    public static final SqlColumn<Long> id = examRecord.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.857+02:00", comments="Source field: exam.name")
    public static final SqlColumn<String> name = examRecord.name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.857+02:00", comments="Source field: exam.status")
    public static final SqlColumn<Integer> status = examRecord.status;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.857+02:00", comments="Source field: exam.configuration_id")
    public static final SqlColumn<Long> configurationId = examRecord.configurationId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-08T10:10:47.856+02:00", comments="Source Table: exam")
    public static final class ExamRecord extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<Integer> status = column("status", JDBCType.INTEGER);

        public final SqlColumn<Long> configurationId = column("configuration_id", JDBCType.BIGINT);

        public ExamRecord() {
            super("exam");
        }
    }
}