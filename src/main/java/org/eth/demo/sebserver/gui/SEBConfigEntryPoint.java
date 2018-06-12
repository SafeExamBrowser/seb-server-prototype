/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui;

import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.widgets.Composite;
import org.eth.demo.sebserver.gui.service.ViewService;
import org.eth.demo.sebserver.gui.views.SEBConfigView1;
import org.springframework.context.ApplicationContext;

public class SEBConfigEntryPoint extends AbstractEntryPoint {

    private static final long serialVersionUID = -5074876749330076628L;

    @Override
    protected void createContents(final Composite parent) {
        final ApplicationContext applicationContext = RAPSpringConfig.RAPSpringContext.getApplicationContext();
        final ViewService viewService = applicationContext.getBean(ViewService.class);
        viewService.composeView(parent, SEBConfigView1.class);
    }

}
