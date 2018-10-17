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
    INSTITUTION_NEW(
            "actions.new.institution",
            IconButtonType.NEW_ACTION),
    INSTITUTION_MODIFY(
            "actions.modify.institution",
            IconButtonType.SAVE_ACTION),
    INSTITUTION_DELETE(
            "actions.delete.institution",
            IconButtonType.DELETE_ACTION),

    ;

    public final String name;
    public final IconButtonType icon;

    private ActionDefinition(final String name, final IconButtonType icon) {
        this.name = name;
        this.icon = icon;
    }

}
