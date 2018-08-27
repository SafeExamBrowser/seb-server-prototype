/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.composer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class TableComposer<R> {

    private final Composite parent;
    private final Composite group;
    private final List<ColumnDef<R>> columns = new ArrayList<>();
    private TableRowPopupMenuComposer menuComposer = null;

    public TableComposer(final Composite parent, final Composite group) {
        this.parent = parent;
        this.group = group;
    }

    public final TableComposer withRowMenu(final TableRowPopupMenuComposer menuComposer) {
        this.menuComposer = menuComposer;
        return this;
    }

    public final TableComposer withColumn(final String name, final String tooltip, final int width,
            final Function<R, String> valueSupplier) {
        this.columns.add(new ColumnDef<>(name, tooltip, width, valueSupplier));
        return this;
    }

    public Table compose(final Collection<R> rows) {
        final Table table = new Table(this.group, SWT.NO_SCROLL);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        if (this.menuComposer != null) {
            final Menu menu = new Menu(table);
            menu.setData(
                    TableRowPopupMenuComposer.ROOT_COMPOSITE_SUPPLIER,
                    (Supplier<Composite>) () -> this.parent);
            table.setMenu(menu);
            table.addListener(
                    SWT.MenuDetect,
                    this.menuComposer::onTableRowMenuEvent);
        }

        for (final ColumnDef<R> column : this.columns) {
            final TableColumn tableColumn = new TableColumn(table, SWT.NONE);
            tableColumn.setText(column.name);
            tableColumn.setToolTipText(column.tooltip);
            tableColumn.setWidth(column.width);
        }

        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        for (final R row : rows) {
            final TableItem item = new TableItem(table, SWT.NONE);
            item.setData(TableRowPopupMenuComposer.TABLE_ROW_DATA, row);
            final int index = 0;
            for (final ColumnDef<R> column : this.columns) {
                item.setText(index, column.valueSupplier.apply(row));
            }
        }

        return table;
    }

    private static final class ColumnDef<R> {
        public final String name;
        public final String tooltip;
        public final int width;
        public final Function<R, String> valueSupplier;

        public ColumnDef(final String name, final String tooltip, final int width,
                final Function<R, String> valueSupplier) {
            this.name = name;
            this.tooltip = tooltip;
            this.width = width;
            this.valueSupplier = valueSupplier;
        }
    }

}
