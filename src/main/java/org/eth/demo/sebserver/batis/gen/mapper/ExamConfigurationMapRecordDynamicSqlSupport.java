package org.eth.demo.sebserver.batis.gen.mapper;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class ExamConfigurationMapRecordDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-06T14:02:57.047+02:00", comments="Source Table: exam_configuration_map")
    public static final ExamConfigurationMapRecord examConfigurationMapRecord = new ExamConfigurationMapRecord();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-06T14:02:57.047+02:00", comments="Source field: exam_configuration_map.id")
    public static final SqlColumn<Long> id = examConfigurationMapRecord.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-06T14:02:57.047+02:00", comments="Source field: exam_configuration_map.exam_id")
    public static final SqlColumn<Long> examId = examConfigurationMapRecord.examId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-06T14:02:57.048+02:00", comments="Source field: exam_configuration_map.configuration_node_id")
    public static final SqlColumn<Long> configurationNodeId = examConfigurationMapRecord.configurationNodeId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-06T14:02:57.048+02:00", comments="Source field: exam_configuration_map.client_info")
    public static final SqlColumn<String> clientInfo = examConfigurationMapRecord.clientInfo;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-06T14:02:57.047+02:00", comments="Source Table: exam_configuration_map")
    public static final class ExamConfigurationMapRecord extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Long> examId = column("exam_id", JDBCType.BIGINT);

        public final SqlColumn<Long> configurationNodeId = column("configuration_node_id", JDBCType.BIGINT);

        public final SqlColumn<String> clientInfo = column("client_info", JDBCType.VARCHAR);

        public ExamConfigurationMapRecord() {
            super("exam_configuration_map");
        }
    }
}