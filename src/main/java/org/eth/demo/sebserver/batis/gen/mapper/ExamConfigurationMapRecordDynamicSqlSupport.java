package org.eth.demo.sebserver.batis.gen.mapper;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class ExamConfigurationMapRecordDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.890+02:00", comments="Source Table: exam_configuration_map")
    public static final ExamConfigurationMapRecord examConfigurationMapRecord = new ExamConfigurationMapRecord();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.891+02:00", comments="Source field: exam_configuration_map.id")
    public static final SqlColumn<Long> id = examConfigurationMapRecord.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.891+02:00", comments="Source field: exam_configuration_map.exam_id")
    public static final SqlColumn<Long> examId = examConfigurationMapRecord.examId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.891+02:00", comments="Source field: exam_configuration_map.configuration_id")
    public static final SqlColumn<Long> configurationId = examConfigurationMapRecord.configurationId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.891+02:00", comments="Source field: exam_configuration_map.client_info")
    public static final SqlColumn<String> clientInfo = examConfigurationMapRecord.clientInfo;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-08T10:59:17.890+02:00", comments="Source Table: exam_configuration_map")
    public static final class ExamConfigurationMapRecord extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Long> examId = column("exam_id", JDBCType.BIGINT);

        public final SqlColumn<Long> configurationId = column("configuration_id", JDBCType.BIGINT);

        public final SqlColumn<String> clientInfo = column("client_info", JDBCType.VARCHAR);

        public ExamConfigurationMapRecord() {
            super("exam_configuration_map");
        }
    }
}