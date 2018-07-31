/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.testing;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Locale;

import org.junit.Test;

public class SupportedLocales {

    @Test
    public void supportedLocales() {
        final Locale list[] = DateFormat.getAvailableLocales();
        assertEquals("", Arrays.asList(list));
    }

}
