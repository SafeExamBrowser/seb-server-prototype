/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.activity;

import org.eclipse.swt.widgets.Tree;
import org.eth.demo.sebserver.gui.service.page.ComposerService.PageContext;
import org.eth.demo.sebserver.gui.service.page.action.ActionDefinition;
import org.eth.demo.sebserver.gui.service.page.action.ActionEvent;

public interface ActivityActionHandler {

    public ActionDefinition handlesAction();

    void notifyAction(
            final ActionEvent event,
            final Tree navigation,
            final PageContext composerCtx);

}
