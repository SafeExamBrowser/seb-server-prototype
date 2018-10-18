/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.table;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eth.demo.sebserver.gui.service.page.PopupMenuComposer;

public abstract class AbstractTableRowMenuPopup<R> implements PopupMenuComposer {

    @Override
    public final void onMenuEvent(final Event event) {
        final Table table = (Table) event.widget;
        final TableItem[] selection = table.getSelection();
        if (selection != null && selection.length > 0) {
            final Rectangle bounds = getRealBounds(selection[0]);
            final Menu menu = table.getMenu();
            if (menu.getItemCount() > 0) {
                for (final MenuItem mItem : menu.getItems()) {
                    mItem.dispose();
                }
            }
            if (bounds.contains(event.x, event.y)) {
                final TableItem item = selection[0];
                @SuppressWarnings("unchecked")
                final R tableRowData = (R) item.getData(TableBuilder.TABLE_ROW_DATA);
                if (tableRowData == null) {
                    menu.setVisible(false);
                    return;
                }
                composeMenu(menu, selection[0], tableRowData);
            }
        }
    }

    private final Rectangle getRealBounds(final TableItem item) {
        Rectangle result = item.getBounds();
        for (int i = 0; i < item.getParent().getColumnCount(); i++) {
            result = result.union(item.getBounds(i));
        }
        Composite parent = item.getParent();
        while (parent != null) {
            result.x += parent.getBounds().x;
            result.y += parent.getBounds().y;
            parent = parent.getParent();
        }
        return result;
    }

    protected abstract void composeMenu(final Menu menu, final TableItem item, R tableRowData);

}
