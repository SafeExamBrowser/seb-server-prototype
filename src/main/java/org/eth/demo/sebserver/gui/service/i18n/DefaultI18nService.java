/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.i18n;

import java.util.Locale;

import org.eth.demo.sebserver.gui.service.rest.auth.CurrentUser;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class DefaultI18nService implements I18nSupport {

    public static final String EMPTY_DISPLAY_VALUE = "--";

    private final DateTimeFormatter displayDateFormatter;
    private final CurrentUser currentUser;
    private final MessageSource messageSource;

    public DefaultI18nService(
            final CurrentUser currentUser,
            final MessageSource messageSource,
            @Value("${sebserver.gui.date.displayformat}") final String displayDateFormat) {

        this.currentUser = currentUser;
        this.messageSource = messageSource;
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

    @Override
    public String getText(final String key, final Object... args) {
        return this.messageSource.getMessage(key, args, this.currentUser.get().locale);
    }

    @Override
    public String getText(final String key, final String def, final Object... args) {
        return this.messageSource.getMessage(key, args, def, this.currentUser.get().locale);
    }

    @Override
    public String getText(final String key, final Locale locale, final Object... args) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getText(final String key, final Locale locale, final String def, final Object... args) {
        // TODO Auto-generated method stub
        return null;
    }

}
