/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.action;

public class ActionEvent {

    public final ActionDefinition actionDefinition;
    public final Object source;

    public ActionEvent(final ActionDefinition actionDefinition, final Object source) {
        this.actionDefinition = actionDefinition;
        this.source = source;
    }

}
