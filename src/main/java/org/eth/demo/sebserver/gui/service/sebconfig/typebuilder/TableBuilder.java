/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig.typebuilder;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUIViewAttribute;
import org.eth.demo.sebserver.gui.service.sebconfig.InputComponentBuilder;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField.FieldType;
import org.eth.demo.sebserver.gui.service.sebconfig.ValueChangeListener;
import org.eth.demo.sebserver.gui.service.sebconfig.ViewContext;
import org.springframework.stereotype.Component;

@Component
public class TableBuilder implements InputComponentBuilder {

    @Override
    public FieldType[] supportedTypes() {
        return new FieldType[] { FieldType.TABLE };
    }

    @Override
    public InputField createInputComponent(
            final Composite parent,
            final GUIViewAttribute attribute,
            final ViewContext viewContext) {

        final List<GUIViewAttribute> columnAttributes = viewContext.getChildAttributes(attribute);
        final TableViewer tableViewer = new TableViewer(parent, SWT.NONE);
        final Table table = tableViewer.getTable();

        final TextCellEditor textCellEditor = new TextCellEditor(table);
        tableViewer.setCellEditors(new CellEditor[] { textCellEditor });

        tableViewer.setContentProvider(new ArrayContentProvider());

        tableViewer.setCellModifier(new ICellModifier() {

            @Override
            public boolean canModify(final Object element, final String property) {
                System.out.println("############### canModify property: " + property + " element: " + element);
                return true;
            }

            @Override
            public Object getValue(final Object element, final String property) {
                System.out.println("############### getValue property: " + property + " element: " + element);
                return null;
            }

            @Override
            public void modify(final Object element, final String property, final Object value) {
                // TODO Auto-generated method stub
                System.out.println(
                        "############### getValue property: " + property + " element: " + element + " value: " + value);
            }

        });

        final Menu menu = new Menu(table);
        table.setMenu(menu);

        for (final GUIViewAttribute columnAttr : columnAttributes) {
            final TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(columnAttr.name);

            // TODO this information should also come within the orientation form back-end
            column.setWidth(200);
        }

        final TableField tableField = new TableField(
                attribute,
                tableViewer,
                columnAttributes,
                viewContext.getValueChangeListener());

        table.addMenuDetectListener(new TableMenuListener(tableField));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.layout();

        return tableField;
    }

    static final class TableField extends ControlFieldAdapter<Table> {

        private final TableViewer tableViewer;
        private final List<GUIViewAttribute> columnAttributes;
        private final ValueChangeListener valueChangeListener;

        TableField(
                final GUIViewAttribute attribute,
                final TableViewer control,
                final List<GUIViewAttribute> columnAttributes,
                final ValueChangeListener valueChangeListener) {

            super(attribute, control.getTable());
            this.tableViewer = control;
            this.columnAttributes = columnAttributes;
            this.valueChangeListener = valueChangeListener;
        }

        @Override
        public InputValue getValue() {
            // TODO
            return null;
        }

        @Override
        public void setValue(final InputValue value) {
            // TODO
        }

        void addRow() {
            // TODO
            final TableItem item = new TableItem(this.control, SWT.NONE);
            int index = 0;
            for (final GUIViewAttribute attr : this.columnAttributes) {
                item.setText(index, "--");
                index++;
            }
        }

        void deleteRow(final int index) {
            System.out.println("******************* deleteRow: " + index);
            this.control.getItem(index).dispose();
            this.control.setSelection(-1);
        }
    };

    private static final class TableMenuListener implements MenuDetectListener {

        private final TableField tableField;

        public TableMenuListener(final TableField tableField) {
            this.tableField = tableField;
        }

        @Override
        public void menuDetected(final MenuDetectEvent event) {
            final Menu menu = this.tableField.control.getMenu();
            if (menu.getItemCount() > 0) {
                for (final MenuItem mItem : menu.getItems()) {
                    mItem.dispose();
                }
            }

            final MenuItem addItem = new MenuItem(menu, SWT.NULL);
            addItem.setText("Add");
            addItem.addListener(SWT.Selection, e -> {
                this.tableField.addRow();
            });

            final int selectionIndex = this.tableField.control.getSelectionIndex();
            System.out.println("******************* selectionIndex: " + selectionIndex);
            if (selectionIndex >= 0) {
                final MenuItem delItem = new MenuItem(menu, SWT.NULL);
                delItem.setText("Delete");
                delItem.addListener(SWT.Selection, e -> {
                    this.tableField.deleteRow(selectionIndex);
                });
            }
        }
    }

}
