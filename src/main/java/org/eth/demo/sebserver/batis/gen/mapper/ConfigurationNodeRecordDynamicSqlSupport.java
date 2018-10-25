package org.eth.demo.sebserver.batis.gen.mapper;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class ConfigurationNodeRecordDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.835+02:00", comments="Source Table: configuration_node")
    public static final ConfigurationNodeRecord configurationNodeRecord = new ConfigurationNodeRecord();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.836+02:00", comments="Source field: configuration_node.id")
    public static final SqlColumn<Long> id = configurationNodeRecord.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.836+02:00", comments="Source field: configuration_node.institution_id")
    public static final SqlColumn<Long> institutionId = configurationNodeRecord.institutionId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.836+02:00", comments="Source field: configuration_node.owner")
    public static final SqlColumn<String> owner = configurationNodeRecord.owner;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.837+02:00", comments="Source field: configuration_node.name")
    public static final SqlColumn<String> name = configurationNodeRecord.name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.837+02:00", comments="Source field: configuration_node.description")
    public static final SqlColumn<String> description = configurationNodeRecord.description;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.837+02:00", comments="Source field: configuration_node.type")
    public static final SqlColumn<String> type = configurationNodeRecord.type;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.837+02:00", comments="Source field: configuration_node.template")
    public static final SqlColumn<String> template = configurationNodeRecord.template;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-25T10:22:09.835+02:00", comments="Source Table: configuration_node")
    public static final class ConfigurationNodeRecord extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Long> institutionId = column("institution_id", JDBCType.BIGINT);

        public final SqlColumn<String> owner = column("owner", JDBCType.VARCHAR);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> description = column("description", JDBCType.VARCHAR);

        public final SqlColumn<String> type = column("type", JDBCType.VARCHAR);

        public final SqlColumn<String> template = column("template", JDBCType.VARCHAR);

        public ConfigurationNodeRecord() {
            super("configuration_node");
        }
    }
}