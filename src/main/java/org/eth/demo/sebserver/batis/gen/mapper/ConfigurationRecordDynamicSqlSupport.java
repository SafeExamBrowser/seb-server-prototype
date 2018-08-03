package org.eth.demo.sebserver.batis.gen.mapper;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.joda.time.DateTime;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class ConfigurationRecordDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.467+02:00", comments="Source Table: configuration")
    public static final ConfigurationRecord configurationRecord = new ConfigurationRecord();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.468+02:00", comments="Source field: configuration.id")
    public static final SqlColumn<Long> id = configurationRecord.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.468+02:00", comments="Source field: configuration.configuration_node_id")
    public static final SqlColumn<Long> configurationNodeId = configurationRecord.configurationNodeId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.468+02:00", comments="Source field: configuration.version")
    public static final SqlColumn<String> version = configurationRecord.version;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.468+02:00", comments="Source field: configuration.version_date")
    public static final SqlColumn<DateTime> versionDate = configurationRecord.versionDate;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.468+02:00", comments="Source field: configuration.followup")
    public static final SqlColumn<Boolean> followup = configurationRecord.followup;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-03T08:27:35.468+02:00", comments="Source Table: configuration")
    public static final class ConfigurationRecord extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Long> configurationNodeId = column("configuration_node_id", JDBCType.BIGINT);

        public final SqlColumn<String> version = column("version", JDBCType.VARCHAR);

        public final SqlColumn<DateTime> versionDate = column("version_date", JDBCType.TIMESTAMP, "org.eth.demo.sebserver.batis.JodaTimeTypeResolver");

        public final SqlColumn<Boolean> followup = column("followup", JDBCType.BOOLEAN);

        public ConfigurationRecord() {
            super("configuration");
        }
    }
}