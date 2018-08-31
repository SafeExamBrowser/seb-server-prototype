/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.composer;

import org.eclipse.rap.rwt.widgets.DialogCallback;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableItem;
import org.eth.demo.sebserver.gui.domain.sebconfig.ConfigTableRow;
import org.eth.demo.sebserver.gui.service.ViewService;
import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class SEBConfigTablePopupMenu extends AbstractTableRowMenuPopup<ConfigTableRow> {

    private final ViewService viewService;
    private final AuthorizationContextHolder authorizationContextHolder;

    public SEBConfigTablePopupMenu(
            final ViewService viewService,
            final AuthorizationContextHolder authorizationContextHolder) {

        this.viewService = viewService;
        this.authorizationContextHolder = authorizationContextHolder;
    }

    @Override
    protected void composeMenu(final Menu menu, final TableItem item, final ConfigTableRow tableRowData) {
        if (tableRowData.id == null) {
            menu.setVisible(false);
            return;
        }

        final String configNodeId = String.valueOf(tableRowData.id);
        addEditConfigAction(menu, configNodeId);
        addDeleteConfigAction(menu, configNodeId);
    }

    private final void addEditConfigAction(final Menu menu, final String configNodeId) {
        final MenuItem item = new MenuItem(menu, SWT.NULL);
        item.setText("Edit");
        item.addListener(SWT.Selection, event -> {
            System.out.println("**************** TODO goto config edit page");
        });
    }

    private final void addDeleteConfigAction(final Menu menu, final String configNodeId) {
        final MenuItem item = new MenuItem(menu, SWT.NULL);
        item.setText("Delete");
        item.addListener(SWT.Selection, event -> {
            final MessageBox dialog = new MessageBox(menu.getShell());
            dialog.setMessage("Are you sure to delete this configuration?");
            dialog.open(new DialogCallback() {
                @Override
                public void dialogClosed(final int returnCode) {
                    System.out.println("************* returnCode: " + returnCode);

                }
            });
        });
    }

}
