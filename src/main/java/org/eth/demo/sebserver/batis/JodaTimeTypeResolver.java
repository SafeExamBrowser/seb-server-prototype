/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.batis;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class JodaTimeTypeResolver extends BaseTypeHandler<DateTime> {

    // TODO: is this always the right patter or do we have to adapt when DB is changing!?
    private static final DateTimeFormatter DB_DATE_TIME_PATTERN = DateTimeFormat
            .forPattern("yyyy-MM-dd HH:mm:ss.S")
            .withZoneUTC();

    @Override
    public void setNonNullParameter(
            final PreparedStatement ps,
            final int i,
            final DateTime parameter,
            final JdbcType jdbcType) throws SQLException {

        ps.setDate(i, new Date(parameter.getMillis()));
    }

    @Override
    public DateTime getNullableResult(final ResultSet rs, final String columnName) throws SQLException {
        return getDateTime(() -> rs.getString(columnName));
    }

    @Override
    public DateTime getNullableResult(final ResultSet rs, final int columnIndex) throws SQLException {
        return getDateTime(() -> rs.getString(columnIndex));
    }

    @Override
    public DateTime getNullableResult(final CallableStatement cs, final int columnIndex) throws SQLException {
        return getDateTime(() -> cs.getString(columnIndex));
    }

    private DateTime getDateTime(final SupplierSQLExceptionAware<String> supplier) throws SQLException {
        final String dateFormattedString = supplier.get();
        if (dateFormattedString == null) {
            return null;
        }

        // NOTE: This create a DateTime in UTC time.zone with no time-zone-offset.
        //
        final LocalDateTime localDateTime = LocalDateTime.parse(dateFormattedString, DB_DATE_TIME_PATTERN);
        final DateTime dateTime = localDateTime.toDateTime(DateTimeZone.UTC);

        return dateTime;
    }

    @FunctionalInterface
    private interface SupplierSQLExceptionAware<T> {
        T get() throws SQLException;
    }

}
