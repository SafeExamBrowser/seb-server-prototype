/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.page.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eth.demo.sebserver.gui.composer.PopupMenuComposer;
import org.eth.demo.sebserver.gui.domain.IdAware;

public class TableBuilder<R extends IdAware> {

    public static final String TABLE_ROW_DATA = "TABLE_ROW_DATA";
    @Deprecated
    public static final String ROOT_COMPOSITE_SUPPLIER = "ROOT_COMPOSITE_SUPPLIER";

    private final Collection<R> rows;
    private final List<ColumnDef<R>> columns = new ArrayList<>();
    private PopupMenuComposer menuComposer = null;
    private String emptyNote = "--";

    public TableBuilder(final Collection<R> rows) {
        this.rows = rows;
    }

    public final TableBuilder<R> withRowMenu(final PopupMenuComposer menuComposer) {
        this.menuComposer = menuComposer;
        return this;
    }

    public final TableBuilder<R> withColumn(
            final String name,
            final String tooltip,
            final int width,
            final Function<R, String> valueSupplier) {

        this.columns.add(new ColumnDef<>(name, tooltip, width, valueSupplier));
        return this;
    }

    public final TableBuilder<R> withEmptyNote(final String note) {
        this.emptyNote = note;
        return this;
    }

    public Table compose(final Composite parent) {
        return compose(null, parent);
    }

    public Table compose(@Deprecated final Composite parent, final Composite group) {
        final Table table = new Table(group, SWT.SINGLE);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        if (this.rows.isEmpty()) {
            final TableColumn tableColumn = new TableColumn(table, SWT.NONE);
            tableColumn.setText(this.emptyNote);
            table.addListener(SWT.Resize, event -> {
                tableColumn.setWidth(table.getClientArea().width);
            });
            return table;
        }

        if (this.menuComposer != null) {
            final Menu menu = new Menu(table);
            table.setMenu(menu);
            table.addListener(
                    SWT.MenuDetect,
                    this.menuComposer::onMenuEvent);
        }

        for (final ColumnDef<R> column : this.columns) {
            final TableColumn tableColumn = new TableColumn(table, SWT.NONE);
            tableColumn.setText(column.name);
            tableColumn.setToolTipText(column.tooltip);
            tableColumn.setWidth(column.width);
        }

        for (final R row : this.rows) {
            final TableItem item = new TableItem(table, SWT.NONE);
            item.setData(TABLE_ROW_DATA, row);
            int index = 0;
            for (final ColumnDef<R> column : this.columns) {
                item.setText(index, column.valueSupplier.apply(row));
                index++;
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
