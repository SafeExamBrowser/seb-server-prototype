package org.eth.demo.sebserver.batis.gen.mapper;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class ConfigurationAttributeRecordDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.569+02:00", comments="Source Table: configuration_attribute")
    public static final ConfigurationAttributeRecord configurationAttributeRecord = new ConfigurationAttributeRecord();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.570+02:00", comments="Source field: configuration_attribute.id")
    public static final SqlColumn<Long> id = configurationAttributeRecord.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.570+02:00", comments="Source field: configuration_attribute.name")
    public static final SqlColumn<String> name = configurationAttributeRecord.name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.570+02:00", comments="Source field: configuration_attribute.type")
    public static final SqlColumn<String> type = configurationAttributeRecord.type;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.570+02:00", comments="Source field: configuration_attribute.parent_id")
    public static final SqlColumn<Long> parentId = configurationAttributeRecord.parentId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.570+02:00", comments="Source field: configuration_attribute.resources")
    public static final SqlColumn<String> resources = configurationAttributeRecord.resources;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.570+02:00", comments="Source field: configuration_attribute.validator")
    public static final SqlColumn<String> validator = configurationAttributeRecord.validator;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.570+02:00", comments="Source field: configuration_attribute.dependencies")
    public static final SqlColumn<String> dependencies = configurationAttributeRecord.dependencies;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-25T15:07:26.569+02:00", comments="Source Table: configuration_attribute")
    public static final class ConfigurationAttributeRecord extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> type = column("type", JDBCType.VARCHAR);

        public final SqlColumn<Long> parentId = column("parent_id", JDBCType.BIGINT);

        public final SqlColumn<String> resources = column("resources", JDBCType.VARCHAR);

        public final SqlColumn<String> validator = column("validator", JDBCType.VARCHAR);

        public final SqlColumn<String> dependencies = column("dependencies", JDBCType.VARCHAR);

        public ConfigurationAttributeRecord() {
            super("configuration_attribute");
        }
    }
}