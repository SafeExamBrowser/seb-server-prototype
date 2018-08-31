/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.i18n;

import java.util.Locale;

import org.joda.time.DateTime;

public interface I18nSupport {

    /** Use this to format a DateTime to displayable text format.
     *
     * @param date
     * @return */
    String formatDisplayDate(DateTime date);

    String getText(String key, Object... args);

    String getText(final String key, String def, Object... args);

    String getText(String key, Locale locale, Object... args);

    String getText(String key, Locale locale, String def, Object... args);

}
