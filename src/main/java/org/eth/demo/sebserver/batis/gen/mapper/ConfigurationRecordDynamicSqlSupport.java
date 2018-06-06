package org.eth.demo.sebserver.batis.gen.mapper;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class ConfigurationRecordDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-06T16:14:12.768+02:00", comments="Source Table: configuration")
    public static final ConfigurationRecord configurationRecord = new ConfigurationRecord();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-06T16:14:12.770+02:00", comments="Source field: configuration.id")
    public static final SqlColumn<Long> id = configurationRecord.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-06T16:14:12.771+02:00", comments="Source field: configuration.name")
    public static final SqlColumn<String> name = configurationRecord.name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-06T16:14:12.772+02:00", comments="Source field: configuration.type")
    public static final SqlColumn<String> type = configurationRecord.type;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-06-06T16:14:12.770+02:00", comments="Source Table: configuration")
    public static final class ConfigurationRecord extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> type = column("type", JDBCType.VARCHAR);

        public ConfigurationRecord() {
            super("configuration");
        }
    }
}