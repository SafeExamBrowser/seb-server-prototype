/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.action;

import org.eth.demo.sebserver.gui.service.widgets.WidgetFactory.IconButtonType;

public enum ActionDefinition {
    INSTITUTION_NEW("new.institution", "", IconButtonType.NEW_ACTION)

    ;

    public final String name;
    public final String displayNameKey;
    public final IconButtonType icon;

    private ActionDefinition(final String name, final String displayNameKey, final IconButtonType icon) {
        this.name = name;
        this.displayNameKey = displayNameKey;
        this.icon = icon;
    }

}
