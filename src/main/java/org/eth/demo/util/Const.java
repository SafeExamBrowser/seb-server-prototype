/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.util;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public interface Const {

    DateTimeFormatter DATE_TIME_PATTERN_UTC_NO_MILLIS = DateTimeFormat
            .forPattern("yyyy-MM-dd HH:mm:ss")
            .withZoneUTC();

    DateTimeFormatter DATE_TIME_PATTERN_UTC_MILLIS = DateTimeFormat
            .forPattern("yyyy-MM-dd HH:mm:ss.S")
            .withZoneUTC();

    String CONTENT_TYPE_APPLICATION_JSON = "application/json; charset=UTF-8";
    String CONTENT_TYPE_PLAIN_TEXT = "text/plain;charset=UTF-8";
    String CONTENT_TYPE_CSS_TEXT = "text/css;charset=UTF-8";

    String INSTITUTION_ID = "institutionId";

}
