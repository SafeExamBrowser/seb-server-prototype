package org.eth.demo.sebserver.batis.gen.mapper;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class OrientationRecordDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.615+02:00", comments="Source Table: orientation")
    public static final OrientationRecord orientationRecord = new OrientationRecord();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.615+02:00", comments="Source field: orientation.id")
    public static final SqlColumn<Long> id = orientationRecord.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.615+02:00", comments="Source field: orientation.config_attribute_id")
    public static final SqlColumn<Long> configAttributeId = orientationRecord.configAttributeId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.615+02:00", comments="Source field: orientation.view")
    public static final SqlColumn<String> view = orientationRecord.view;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.615+02:00", comments="Source field: orientation.group")
    public static final SqlColumn<String> group = orientationRecord.group;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.616+02:00", comments="Source field: orientation.x_position")
    public static final SqlColumn<Integer> xPosition = orientationRecord.xPosition;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.616+02:00", comments="Source field: orientation.y_position")
    public static final SqlColumn<Integer> yPosition = orientationRecord.yPosition;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-09-26T09:47:00.615+02:00", comments="Source Table: orientation")
    public static final class OrientationRecord extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Long> configAttributeId = column("config_attribute_id", JDBCType.BIGINT);

        public final SqlColumn<String> view = column("view", JDBCType.VARCHAR);

        public final SqlColumn<String> group = column("group", JDBCType.VARCHAR);

        public final SqlColumn<Integer> xPosition = column("x_position", JDBCType.INTEGER);

        public final SqlColumn<Integer> yPosition = column("y_position", JDBCType.INTEGER);

        public OrientationRecord() {
            super("orientation");
        }
    }
}