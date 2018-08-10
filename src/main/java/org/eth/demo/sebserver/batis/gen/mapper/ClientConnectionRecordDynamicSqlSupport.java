package org.eth.demo.sebserver.batis.gen.mapper;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class ClientConnectionRecordDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T15:49:36.989+02:00", comments="Source Table: client_connection")
    public static final ClientConnectionRecord clientConnectionRecord = new ClientConnectionRecord();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T15:49:36.989+02:00", comments="Source field: client_connection.id")
    public static final SqlColumn<Long> id = clientConnectionRecord.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T15:49:36.989+02:00", comments="Source field: client_connection.exam_id")
    public static final SqlColumn<Long> examId = clientConnectionRecord.examId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T15:49:36.989+02:00", comments="Source field: client_connection.status")
    public static final SqlColumn<String> status = clientConnectionRecord.status;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T15:49:36.989+02:00", comments="Source field: client_connection.connection_token")
    public static final SqlColumn<String> connectionToken = clientConnectionRecord.connectionToken;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T15:49:36.990+02:00", comments="Source field: client_connection.client_identifier")
    public static final SqlColumn<String> clientIdentifier = clientConnectionRecord.clientIdentifier;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T15:49:36.990+02:00", comments="Source field: client_connection.client_address")
    public static final SqlColumn<String> clientAddress = clientConnectionRecord.clientAddress;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T15:49:36.989+02:00", comments="Source Table: client_connection")
    public static final class ClientConnectionRecord extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Long> examId = column("exam_id", JDBCType.BIGINT);

        public final SqlColumn<String> status = column("status", JDBCType.VARCHAR);

        public final SqlColumn<String> connectionToken = column("connection_token", JDBCType.VARCHAR);

        public final SqlColumn<String> clientIdentifier = column("client_identifier", JDBCType.VARCHAR);

        public final SqlColumn<String> clientAddress = column("client_address", JDBCType.VARCHAR);

        public ClientConnectionRecord() {
            super("client_connection");
        }
    }
}