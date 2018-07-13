package org.eth.demo.sebserver.batis.gen.mapper;

import java.math.BigDecimal;
import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class IndicatorRecordDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-13T13:25:25.220+02:00", comments="Source Table: indicator")
    public static final IndicatorRecord indicatorRecord = new IndicatorRecord();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-13T13:25:25.220+02:00", comments="Source field: indicator.id")
    public static final SqlColumn<Long> id = indicatorRecord.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-13T13:25:25.221+02:00", comments="Source field: indicator.exam_id")
    public static final SqlColumn<Long> examId = indicatorRecord.examId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-13T13:25:25.221+02:00", comments="Source field: indicator.type")
    public static final SqlColumn<String> type = indicatorRecord.type;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-13T13:25:25.221+02:00", comments="Source field: indicator.threshold1")
    public static final SqlColumn<BigDecimal> threshold1 = indicatorRecord.threshold1;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-13T13:25:25.221+02:00", comments="Source field: indicator.threshold2")
    public static final SqlColumn<BigDecimal> threshold2 = indicatorRecord.threshold2;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-13T13:25:25.221+02:00", comments="Source field: indicator.threshold3")
    public static final SqlColumn<BigDecimal> threshold3 = indicatorRecord.threshold3;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-07-13T13:25:25.220+02:00", comments="Source Table: indicator")
    public static final class IndicatorRecord extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Long> examId = column("exam_id", JDBCType.BIGINT);

        public final SqlColumn<String> type = column("type", JDBCType.VARCHAR);

        public final SqlColumn<BigDecimal> threshold1 = column("threshold1", JDBCType.DECIMAL);

        public final SqlColumn<BigDecimal> threshold2 = column("threshold2", JDBCType.DECIMAL);

        public final SqlColumn<BigDecimal> threshold3 = column("threshold3", JDBCType.DECIMAL);

        public IndicatorRecord() {
            super("indicator");
        }
    }
}