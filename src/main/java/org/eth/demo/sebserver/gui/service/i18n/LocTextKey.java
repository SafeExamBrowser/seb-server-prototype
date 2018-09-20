/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.i18n;

public class LocTextKey {

    public final String name;
    public final Object[] args;

    public LocTextKey(final String key) {
        this(key, null);
    }

    public LocTextKey(final String name, final Object[] args) {
        this.name = name;
        this.args = args;
    }
}
