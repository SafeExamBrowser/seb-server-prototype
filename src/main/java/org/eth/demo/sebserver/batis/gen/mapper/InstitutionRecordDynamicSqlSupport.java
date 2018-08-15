package org.eth.demo.sebserver.batis.gen.mapper;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class InstitutionRecordDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-15T14:53:13.437+02:00", comments="Source Table: institution")
    public static final InstitutionRecord institutionRecord = new InstitutionRecord();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-15T14:53:13.437+02:00", comments="Source field: institution.id")
    public static final SqlColumn<Long> id = institutionRecord.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-15T14:53:13.438+02:00", comments="Source field: institution.name")
    public static final SqlColumn<String> name = institutionRecord.name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-15T14:53:13.437+02:00", comments="Source Table: institution")
    public static final class InstitutionRecord extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public InstitutionRecord() {
            super("institution");
        }
    }
}