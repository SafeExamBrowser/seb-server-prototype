package org.eth.demo.sebserver.batis.gen.mapper;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class ClientConnectionRecordDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.497+02:00", comments="Source Table: client_connection")
    public static final ClientConnectionRecord clientConnectionRecord = new ClientConnectionRecord();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.498+02:00", comments="Source field: client_connection.id")
    public static final SqlColumn<Long> id = clientConnectionRecord.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.498+02:00", comments="Source field: client_connection.exam_id")
    public static final SqlColumn<Long> examId = clientConnectionRecord.examId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.498+02:00", comments="Source field: client_connection.status")
    public static final SqlColumn<String> status = clientConnectionRecord.status;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.498+02:00", comments="Source field: client_connection.token")
    public static final SqlColumn<String> token = clientConnectionRecord.token;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.498+02:00", comments="Source field: client_connection.lms_auth_url")
    public static final SqlColumn<String> lmsAuthUrl = clientConnectionRecord.lmsAuthUrl;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.498+02:00", comments="Source Table: client_connection")
    public static final class ClientConnectionRecord extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Long> examId = column("exam_id", JDBCType.BIGINT);

        public final SqlColumn<String> status = column("status", JDBCType.VARCHAR);

        public final SqlColumn<String> token = column("token", JDBCType.VARCHAR);

        public final SqlColumn<String> lmsAuthUrl = column("lms_auth_url", JDBCType.VARCHAR);

        public ClientConnectionRecord() {
            super("client_connection");
        }
    }
}