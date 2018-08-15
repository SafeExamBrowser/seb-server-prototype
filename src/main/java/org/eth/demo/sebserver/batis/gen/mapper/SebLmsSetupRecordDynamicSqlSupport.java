package org.eth.demo.sebserver.batis.gen.mapper;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class SebLmsSetupRecordDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-15T08:13:40.041+02:00", comments="Source Table: seb_lms_setup")
    public static final SebLmsSetupRecord sebLmsSetupRecord = new SebLmsSetupRecord();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-15T08:13:40.041+02:00", comments="Source field: seb_lms_setup.id")
    public static final SqlColumn<Long> id = sebLmsSetupRecord.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-15T08:13:40.041+02:00", comments="Source field: seb_lms_setup.institution_id")
    public static final SqlColumn<Long> institutionId = sebLmsSetupRecord.institutionId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-15T08:13:40.041+02:00", comments="Source field: seb_lms_setup.seb_clientname")
    public static final SqlColumn<String> sebClientname = sebLmsSetupRecord.sebClientname;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-15T08:13:40.041+02:00", comments="Source field: seb_lms_setup.seb_clientsecret")
    public static final SqlColumn<String> sebClientsecret = sebLmsSetupRecord.sebClientsecret;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-15T08:13:40.041+02:00", comments="Source field: seb_lms_setup.lms_clientname")
    public static final SqlColumn<String> lmsClientname = sebLmsSetupRecord.lmsClientname;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-15T08:13:40.042+02:00", comments="Source field: seb_lms_setup.lms_clientsecret")
    public static final SqlColumn<String> lmsClientsecret = sebLmsSetupRecord.lmsClientsecret;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-15T08:13:40.042+02:00", comments="Source field: seb_lms_setup.lms_url")
    public static final SqlColumn<String> lmsUrl = sebLmsSetupRecord.lmsUrl;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-15T08:13:40.041+02:00", comments="Source Table: seb_lms_setup")
    public static final class SebLmsSetupRecord extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Long> institutionId = column("institution_id", JDBCType.BIGINT);

        public final SqlColumn<String> sebClientname = column("seb_clientname", JDBCType.VARCHAR);

        public final SqlColumn<String> sebClientsecret = column("seb_clientsecret", JDBCType.VARCHAR);

        public final SqlColumn<String> lmsClientname = column("lms_clientname", JDBCType.VARCHAR);

        public final SqlColumn<String> lmsClientsecret = column("lms_clientsecret", JDBCType.VARCHAR);

        public final SqlColumn<String> lmsUrl = column("lms_url", JDBCType.VARCHAR);

        public SebLmsSetupRecord() {
            super("seb_lms_setup");
        }
    }
}