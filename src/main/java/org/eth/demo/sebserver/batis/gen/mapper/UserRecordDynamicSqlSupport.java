package org.eth.demo.sebserver.batis.gen.mapper;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.joda.time.DateTime;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class UserRecordDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.875+02:00", comments="Source Table: user")
    public static final UserRecord userRecord = new UserRecord();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.875+02:00", comments="Source field: user.id")
    public static final SqlColumn<Long> id = userRecord.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.875+02:00", comments="Source field: user.institution_id")
    public static final SqlColumn<Long> institutionId = userRecord.institutionId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.875+02:00", comments="Source field: user.name")
    public static final SqlColumn<String> name = userRecord.name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.875+02:00", comments="Source field: user.user_name")
    public static final SqlColumn<String> userName = userRecord.userName;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.875+02:00", comments="Source field: user.password")
    public static final SqlColumn<String> password = userRecord.password;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.875+02:00", comments="Source field: user.email")
    public static final SqlColumn<String> email = userRecord.email;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.875+02:00", comments="Source field: user.creation_date")
    public static final SqlColumn<DateTime> creationDate = userRecord.creationDate;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.876+02:00", comments="Source field: user.active")
    public static final SqlColumn<Boolean> active = userRecord.active;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-08-10T09:36:55.875+02:00", comments="Source Table: user")
    public static final class UserRecord extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Long> institutionId = column("institution_id", JDBCType.BIGINT);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> userName = column("user_name", JDBCType.VARCHAR);

        public final SqlColumn<String> password = column("password", JDBCType.VARCHAR);

        public final SqlColumn<String> email = column("email", JDBCType.VARCHAR);

        public final SqlColumn<DateTime> creationDate = column("creation_date", JDBCType.TIMESTAMP, "org.eth.demo.sebserver.batis.JodaTimeTypeResolver");

        public final SqlColumn<Boolean> active = column("active", JDBCType.BOOLEAN);

        public UserRecord() {
            super("user");
        }
    }
}