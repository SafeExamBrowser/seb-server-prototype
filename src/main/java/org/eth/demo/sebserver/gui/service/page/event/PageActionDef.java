/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.event;

public class PageActionDef {

    public final String name;
    public final String displayName;
    public final String icon;
    public final Runnable run;

    public PageActionDef(final String name, final String displayName, final String icon, final Runnable run) {
        this.name = name;
        this.displayName = displayName;
        this.icon = icon;
        this.run = run;
    }

}
