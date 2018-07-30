/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class GuiI18nSupport implements I18nSupport {

    public static final String EMPTY_DISPLAY_VALUE = "--";

    private final DateTimeFormatter displayDateFormatter;

    public GuiI18nSupport(
            @Value("${sebserver.gui.date.displayformat}") final String displayDateFormat) {

        this.displayDateFormatter = DateTimeFormat
                .forPattern(displayDateFormat)
                .withZoneUTC();
    }

    @Override
    public String formatDisplayDate(final DateTime date) {
        if (date == null) {
            return EMPTY_DISPLAY_VALUE;
        }
        return date.toString(this.displayDateFormatter);
    }

}
