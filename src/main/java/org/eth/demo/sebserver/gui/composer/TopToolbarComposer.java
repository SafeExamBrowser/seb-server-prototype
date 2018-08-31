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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGBA;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eth.demo.sebserver.gui.service.AttributeKeys;
import org.eth.demo.sebserver.gui.service.ViewService;
import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class TopToolbarComposer implements ContentComponentComposer<ToolBar> {

    private final ViewService viewService;
    private final AuthorizationContextHolder authorizationContextHolder;

    public TopToolbarComposer(
            final ViewService viewService,
            final AuthorizationContextHolder authorizationContextHolder) {

        this.viewService = viewService;
        this.authorizationContextHolder = authorizationContextHolder;
    }

    @Override
    public ToolBar compose(final Composite parent, final Composite group) {

        final ToolBar toolBar = new ToolBar(group, SWT.HORIZONTAL | SWT.RIGHT);
        final GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        gridData.heightHint = 50;
        toolBar.setLayoutData(gridData);
        toolBar.setLayout(new GridLayout(3, false));

        final Label titleLabel = new Label(toolBar, SWT.NONE | SWT.NO_BACKGROUND);
        titleLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        titleLabel.setBackground(new Color(group.getDisplay(), new RGBA(255, 255, 255, 0)));
        titleLabel.setText("  Dashboard : ");

        final ToolItem sep = new ToolItem(toolBar, SWT.SEPARATOR);
        sep.setWidth(100);

        final Menu newMenu = new Menu(group.getShell(), SWT.POP_UP);
        final MenuItem config = new MenuItem(newMenu, SWT.PUSH);
        config.setText("Configuration");
        config.addListener(SWT.Selection, event -> {
            System.out.println("TODO: new Configuration View");
        });
        final MenuItem exam = new MenuItem(newMenu, SWT.PUSH);
        exam.setText("Exam");
        exam.addListener(SWT.Selection, event -> {
            System.out.println("TODO: new Exam View");
        });

        final ToolItem newItem = new ToolItem(toolBar, SWT.DROP_DOWN);
        newItem.setText("New");
        newItem.addListener(SWT.Selection, event -> {
            final Rectangle rect = newItem.getBounds();
            Point pt = new Point(rect.x, rect.y + rect.height);
            pt = toolBar.toDisplay(pt);
            newMenu.setLocation(pt.x, pt.y);
            newMenu.setVisible(true);
        });

        final Menu adminMenu = new Menu(group.getShell(), SWT.POP_UP);
        final MenuItem user = new MenuItem(adminMenu, SWT.PUSH);
        user.setText("User");
        user.addListener(SWT.Selection, event -> {
            System.out.println("TODO: go to user administration");
        });
        final MenuItem institutionMenuItem = new MenuItem(adminMenu, SWT.PUSH);
        institutionMenuItem.setText("Institution");
        institutionMenuItem.addListener(SWT.Selection, event -> {
            System.out.println("TODO: go to institution administration");
        });

        final ToolItem adminItem = new ToolItem(toolBar, SWT.DROP_DOWN);
        adminItem.setText("  Administration");
        adminItem.addListener(SWT.Selection, event -> {
            final Rectangle rect = adminItem.getBounds();
            Point pt = new Point(rect.x, rect.y + rect.height);
            pt = toolBar.toDisplay(pt);
            adminMenu.setLocation(pt.x, pt.y);
            adminMenu.setVisible(true);
        });

        final Button logout = new Button(toolBar, SWT.NONE);
        logout.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, true));
        logout.setText("Logout");
        logout.addListener(SWT.Selection, event -> {
            final boolean logoutSuccessful = this.authorizationContextHolder
                    .getAuthorizationContext()
                    .logout();

            if (!logoutSuccessful) {
                // TODO error handling
            }

            this.viewService
                    .createViewOn(parent)
                    .attribute(AttributeKeys.LGOUT_SUCCESS, "true")
                    .compose(ViewService.LOGIN_PAGE);
        });

        return toolBar;
    }

}
