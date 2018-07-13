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

public class JodaTimeTypeResolver extends BaseTypeHandler<DateTime> {

    @Override
    public void setNonNullParameter(final PreparedStatement ps, final int i, final DateTime parameter,
            final JdbcType jdbcType) throws SQLException {

        ps.setDate(i, new Date(parameter.getMillis()));
    }

    @Override
    public DateTime getNullableResult(final ResultSet rs, final String columnName) throws SQLException {
        return getDateTime(() -> rs.getDate(columnName));
    }

    @Override
    public DateTime getNullableResult(final ResultSet rs, final int columnIndex) throws SQLException {
        return getDateTime(() -> rs.getDate(columnIndex));
    }

    @Override
    public DateTime getNullableResult(final CallableStatement cs, final int columnIndex) throws SQLException {
        return getDateTime(() -> cs.getDate(columnIndex));
    }

    private DateTime getDateTime(final SupplierSQLExceptionAware<Date> supplier) throws SQLException {
        final Date date = supplier.get();
        if (date == null) {
            return null;
        }

        return new DateTime(date);
    }

    @FunctionalInterface
    private interface SupplierSQLExceptionAware<T> {
        T get() throws SQLException;
    }

}
