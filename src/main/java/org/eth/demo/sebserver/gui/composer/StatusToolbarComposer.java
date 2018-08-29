/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.composer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGBA;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eth.demo.sebserver.gui.service.ViewService;
import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.eth.demo.sebserver.gui.service.rest.auth.SEBServerAuthorizationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class StatusToolbarComposer implements ContentComponentComposer<ToolBar> {

    private final ViewService viewService;
    private final AuthorizationContextHolder authorizationContextHolder;

    public StatusToolbarComposer(
            final ViewService viewService,
            final AuthorizationContextHolder authorizationContextHolder) {

        this.viewService = viewService;
        this.authorizationContextHolder = authorizationContextHolder;
    }

    @Override
    public ToolBar compose(final Composite parent, final Composite group) {

        final SEBServerAuthorizationContext authorizationContext = this.authorizationContextHolder
                .getAuthorizationContext();

        final ToolBar statusBar = new ToolBar(group, SWT.HORIZONTAL | SWT.RIGHT);
        final GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData2.heightHint = 30;
        statusBar.setLayoutData(gridData2);
        statusBar.setLayout(new GridLayout(3, true));

        final Label statusLabel = new Label(statusBar, SWT.NONE | SWT.NO_BACKGROUND);
        statusLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, true, true));
        statusLabel.setBackground(new Color(parent.getDisplay(), new RGBA(255, 255, 255, 0)));
        statusLabel.setText("  You are logged in as : " + authorizationContext.getLoggedInUser().getName());

        return statusBar;
    }

}
