package org.eth.demo.sebserver.batis.gen.mapper;

import java.math.BigDecimal;
import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class IndicatorValueRecordDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-09T14:50:25.081+02:00", comments="Source Table: indicator_value")
    public static final IndicatorValueRecord indicatorValueRecord = new IndicatorValueRecord();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-09T14:50:25.081+02:00", comments="Source field: indicator_value.id")
    public static final SqlColumn<Long> id = indicatorValueRecord.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-09T14:50:25.082+02:00", comments="Source field: indicator_value.indicator_id")
    public static final SqlColumn<Long> indicatorId = indicatorValueRecord.indicatorId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-09T14:50:25.082+02:00", comments="Source field: indicator_value.client_id")
    public static final SqlColumn<Long> clientId = indicatorValueRecord.clientId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-09T14:50:25.082+02:00", comments="Source field: indicator_value.value")
    public static final SqlColumn<BigDecimal> value = indicatorValueRecord.value;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-09T14:50:25.083+02:00", comments="Source field: indicator_value.timestamp")
    public static final SqlColumn<Long> timestamp = indicatorValueRecord.timestamp;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2018-05-09T14:50:25.081+02:00", comments="Source Table: indicator_value")
    public static final class IndicatorValueRecord extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Long> indicatorId = column("indicator_id", JDBCType.BIGINT);

        public final SqlColumn<Long> clientId = column("client_id", JDBCType.BIGINT);

        public final SqlColumn<BigDecimal> value = column("value", JDBCType.DECIMAL);

        public final SqlColumn<Long> timestamp = column("timestamp", JDBCType.BIGINT);

        public IndicatorValueRecord() {
            super("indicator_value");
        }
    }
}