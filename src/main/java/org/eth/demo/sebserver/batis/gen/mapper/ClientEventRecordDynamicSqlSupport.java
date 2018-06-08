package org.eth.demo.sebserver.batis.gen.mapper;

import java.math.BigDecimal;
import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class ClientEventRecordDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T16:06:32.985+02:00", comments="Source Table: client_event")
    public static final ClientEventRecord clientEventRecord = new ClientEventRecord();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T16:06:32.985+02:00", comments="Source field: client_event.id")
    public static final SqlColumn<Long> id = clientEventRecord.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T16:06:32.985+02:00", comments="Source field: client_event.exam_id")
    public static final SqlColumn<Long> examId = clientEventRecord.examId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T16:06:32.985+02:00", comments="Source field: client_event.client_id")
    public static final SqlColumn<Long> clientId = clientEventRecord.clientId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T16:06:32.986+02:00", comments="Source field: client_event.type")
    public static final SqlColumn<Integer> type = clientEventRecord.type;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T16:06:32.986+02:00", comments="Source field: client_event.timestamp")
    public static final SqlColumn<Long> timestamp = clientEventRecord.timestamp;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T16:06:32.986+02:00", comments="Source field: client_event.numeric_value")
    public static final SqlColumn<BigDecimal> numericValue = clientEventRecord.numericValue;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T16:06:32.986+02:00", comments="Source field: client_event.text")
    public static final SqlColumn<String> text = clientEventRecord.text;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T16:06:32.985+02:00", comments="Source Table: client_event")
    public static final class ClientEventRecord extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Long> examId = column("exam_id", JDBCType.BIGINT);

        public final SqlColumn<Long> clientId = column("client_id", JDBCType.BIGINT);

        public final SqlColumn<Integer> type = column("type", JDBCType.INTEGER);

        public final SqlColumn<Long> timestamp = column("timestamp", JDBCType.BIGINT);

        public final SqlColumn<BigDecimal> numericValue = column("numeric_value", JDBCType.DECIMAL);

        public final SqlColumn<String> text = column("text", JDBCType.VARCHAR);

        public ClientEventRecord() {
            super("client_event");
        }
    }
}