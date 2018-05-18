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
import org.eth.demo.sebserver.gui.view.ViewService;
import org.eth.demo.sebserver.gui.views.ExamOverview;
import org.springframework.context.ApplicationContext;

public class ExamViewEntryPoint extends AbstractEntryPoint {

    private static final long serialVersionUID = -1106183608032715804L;

    @Override
    protected void createContents(final Composite parent) {
        final ApplicationContext applicationContext = RAPSpringConfig.RAPSpringContext.getApplicationContext();
        final ViewService viewService = applicationContext.getBean(ViewService.class);
        viewService.composeView(parent, ExamOverview.class);
    }

}
