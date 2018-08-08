package org.eth.demo.sebserver.batis.gen.mapper;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class ClientConnectionRecordDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-06T14:02:57.055+02:00", comments="Source Table: client_connection")
    public static final ClientConnectionRecord clientConnectionRecord = new ClientConnectionRecord();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-06T14:02:57.055+02:00", comments="Source field: client_connection.id")
    public static final SqlColumn<Long> id = clientConnectionRecord.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-06T14:02:57.056+02:00", comments="Source field: client_connection.exam_id")
    public static final SqlColumn<Long> examId = clientConnectionRecord.examId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-06T14:02:57.056+02:00", comments="Source field: client_connection.status")
    public static final SqlColumn<String> status = clientConnectionRecord.status;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-06T14:02:57.056+02:00", comments="Source field: client_connection.username")
    public static final SqlColumn<String> username = clientConnectionRecord.username;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-06T14:02:57.056+02:00", comments="Source field: client_connection.client_address")
    public static final SqlColumn<String> clientAddress = clientConnectionRecord.clientAddress;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-06T14:02:57.056+02:00", comments="Source field: client_connection.token")
    public static final SqlColumn<String> token = clientConnectionRecord.token;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-06T14:02:57.055+02:00", comments="Source Table: client_connection")
    public static final class ClientConnectionRecord extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Long> examId = column("exam_id", JDBCType.BIGINT);

        public final SqlColumn<String> status = column("status", JDBCType.VARCHAR);

        public final SqlColumn<String> username = column("username", JDBCType.VARCHAR);

        public final SqlColumn<String> clientAddress = column("client_address", JDBCType.VARCHAR);

        public final SqlColumn<String> token = column("token", JDBCType.VARCHAR);

        public ClientConnectionRecord() {
            super("client_connection");
        }
    }
}