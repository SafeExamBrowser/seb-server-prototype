/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.batis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.eth.demo.util.Const;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JodaTimeTypeResolver extends BaseTypeHandler<DateTime> {

    private static final Logger log = LoggerFactory.getLogger(JodaTimeTypeResolver.class);

    @Override
    public void setNonNullParameter(
            final PreparedStatement ps,
            final int i,
            final DateTime parameter,
            final JdbcType jdbcType) throws SQLException {

        ps.setTimestamp(i, new Timestamp(parameter.getMillis()));
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
        String dateFormattedString = supplier.get();
        if (dateFormattedString == null) {
            return null;
        }

        try {
            // cutting milliseconds if there are some. This is needed to be able to use a general pattern
            // independently from the different data-base-drivers format the date-time values
            if (dateFormattedString.contains(".")) {
                dateFormattedString = dateFormattedString.substring(
                        0,
                        dateFormattedString.indexOf("."));
            }

            // NOTE: This create a DateTime in UTC time.zone with no time-zone-offset.
            final LocalDateTime localDateTime = LocalDateTime.parse(
                    dateFormattedString,
                    Const.DATE_TIME_PATTERN_UTC_NO_MILLIS);
            final DateTime dateTime = localDateTime.toDateTime(DateTimeZone.UTC);

            return dateTime;
        } catch (final Exception e) {
            log.error("while trying to parse LocalDateTime); value: " + dateFormattedString + " format: "
                    + Const.DATE_TIME_PATTERN_UTC_NO_MILLIS, e);
            return null;
        }
    }

    @FunctionalInterface
    private interface SupplierSQLExceptionAware<T> {
        T get() throws SQLException;
    }

}
