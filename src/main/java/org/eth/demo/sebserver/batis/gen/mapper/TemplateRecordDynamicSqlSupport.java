package org.eth.demo.sebserver.batis.gen.mapper;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class TemplateRecordDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.049+02:00", comments="Source Table: template")
    public static final TemplateRecord templateRecord = new TemplateRecord();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.049+02:00", comments="Source field: template.id")
    public static final SqlColumn<Long> id = templateRecord.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.049+02:00", comments="Source field: template.name")
    public static final SqlColumn<String> name = templateRecord.name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.049+02:00", comments="Source field: template.description")
    public static final SqlColumn<String> description = templateRecord.description;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-10-23T16:13:43.049+02:00", comments="Source Table: template")
    public static final class TemplateRecord extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> description = column("description", JDBCType.VARCHAR);

        public TemplateRecord() {
            super("template");
        }
    }
}