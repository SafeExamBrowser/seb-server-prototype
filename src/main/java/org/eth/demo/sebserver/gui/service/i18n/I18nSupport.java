/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.i18n;

import java.util.Collection;
import java.util.Locale;

import org.joda.time.DateTime;

public interface I18nSupport {

    Collection<Locale> supportedLanguages();

    /** Use this to get the current Locale either form a user if this is called form a logged in user context or the
     * applications default locale.
     *
     * @return the current Locale to use in context */
    Locale getCurrentLocale();

    void setSessionLocale(Locale locale);

    /** Use this to format a DateTime to displayable text format.
     *
     * @param date
     * @return */
    String formatDisplayDate(DateTime date);

    String getText(String key, Object... args);

    String getText(LocTextKey key);

    String getText(final String key, String def, Object... args);

    String getText(String key, Locale locale, Object... args);

    String getText(String key, Locale locale, String def, Object... args);

}
