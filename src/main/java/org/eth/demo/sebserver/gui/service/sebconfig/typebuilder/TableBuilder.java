/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig.typebuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUIAttributeValue;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUITableValue;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUIViewAttribute;
import org.eth.demo.sebserver.gui.service.sebconfig.InputComponentBuilder;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField.FieldType;
import org.eth.demo.sebserver.gui.service.sebconfig.ViewContext;
import org.springframework.stereotype.Component;

@Component
public class TableBuilder implements InputComponentBuilder {

    public static final String LIST_INDEX_REF = "ListIndex";

    private Map<FieldType, TableCellEditorBuilder> cellEditorBuilderMap;

    public TableBuilder(final Collection<TableCellEditorBuilder> cellEditorBuilderList) {
        if (cellEditorBuilderList != null) {
            this.cellEditorBuilderMap = cellEditorBuilderList.stream()
                    .collect(Collectors.toMap(
                            b -> b.getType(),
                            b -> b));
        } else {
            this.cellEditorBuilderMap = Collections.emptyMap();
        }
    }

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
        final Table table = new Table(parent, SWT.NONE);
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
                table,
                columnAttributes,
                viewContext);

        table.addMenuDetectListener(new TableMenuListener(tableField));
        table.addMouseListener(new TableCellListener(tableField));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.layout();

        return tableField;
    }

    static final class TableField extends ControlFieldAdapter<Table> {

        final List<GUIViewAttribute> columnAttributes;
        final ViewContext viewContext;
        final TableEditor[] editor;

        TableField(
                final GUIViewAttribute attribute,
                final Table control,
                final List<GUIViewAttribute> columnAttributes,
                final ViewContext viewContext) {

            super(attribute, control);
            this.columnAttributes = columnAttributes;
            this.viewContext = viewContext;
            this.editor = new TableEditor[columnAttributes.size()];

            for (int i = 0; i < this.editor.length; i++) {
                this.editor[i] = new TableEditor(control);
                this.editor[i].horizontalAlignment = SWT.LEFT;
                this.editor[i].grabHorizontal = true;
            }

            // editor cleanup on row selection
            control.addListener(SWT.Selection, e -> editorCleanup());
            // and on focus loss on table
            //control.addListener(SWT.FocusOut, e -> editorCleanup());
        }

        @Override
        public void initValue(final Collection<GUIAttributeValue> values) {
            final List<GUIAttributeValue> allValues = values.stream()
                    .filter(a -> this.attribute.name.equals(a.parentAttributeName))
                    .collect(Collectors.toList());

            int index = 0;
            while (index >= 0) {
                final int listIndex = index;
                final Map<String, GUIAttributeValue> rowValues = allValues.stream()
                        .filter(a -> a.listIndex == listIndex)
                        .collect(Collectors.toMap(a -> a.attributeName, a -> a));

                if (!rowValues.isEmpty()) {
                    final TableItem item = new TableItem(this.control, SWT.NONE);
                    int columnIndex = 0;
                    for (final GUIViewAttribute attr : this.columnAttributes) {
                        final String value = (rowValues.containsKey(attr.name) ? rowValues.get(attr.name).value
                                : attr.getDefaultValue());
                        item.setText(columnIndex, value);
                        columnIndex++;
                    }
                    item.setData(LIST_INDEX_REF, listIndex);
                    index++;
                } else {
                    index = -1;
                }
            }
        }

        void addRow() {
            final int listIndex = this.control.getItemCount();
            final TableItem item = new TableItem(this.control, SWT.NONE);

            int index = 0;
            for (final GUIViewAttribute attr : this.columnAttributes) {
                final String defaultValue = attr.getDefaultValue();
                item.setText(index, defaultValue);
                valueChanged(index, listIndex, defaultValue);
                index++;
            }

            item.setData(LIST_INDEX_REF, listIndex);
        }

        void deleteRow(final int index) {
            this.control.getItem(index).dispose();
            this.control.setSelection(-1);

            final List<String> values = new ArrayList<>();
            for (final TableItem item : this.control.getItems()) {
                final int listIndex = (Integer) item.getData(LIST_INDEX_REF);
                if (listIndex > index) {
                    item.setData(LIST_INDEX_REF, listIndex - 1);
                }

                for (int i = 0; i < this.columnAttributes.size(); i++) {
                    values.add(item.getText(i));
                }
            }

            final List<String> columnNames = this.columnAttributes.stream()
                    .map(a -> a.name)
                    .collect(Collectors.toList());
            final GUITableValue tableValue = new GUITableValue(
                    this.viewContext.configurationId,
                    this.attribute.name,
                    columnNames,
                    values);

            this.viewContext.getValueChangeListener().tableChanged(tableValue);
        }

        public void valueChanged(final int columnIndex, final int rowIndex, final String value) {
            this.viewContext.getValueChangeListener().valueChanged(
                    this.columnAttributes.get(columnIndex),
                    value,
                    rowIndex);
        }

        void editorCleanup() {
            for (int i = 0; i < this.editor.length; i++) {
                final Control editor = this.editor[i].getEditor();
                if (editor != null) {
                    editor.dispose();
                }
            }
        }
    };

    private static final class TableMenuListener implements MenuDetectListener {

        private static final long serialVersionUID = 2097739147685555217L;
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
            this.tableField.editorCleanup();

            final MenuItem addItem = new MenuItem(menu, SWT.NULL);
            addItem.setText("Add");
            addItem.addListener(SWT.Selection, e -> {
                this.tableField.addRow();
            });

            final int selectionIndex = this.tableField.control.getSelectionIndex();
            if (selectionIndex >= 0) {
                final MenuItem delItem = new MenuItem(menu, SWT.NULL);
                delItem.setText("Delete");
                delItem.addListener(SWT.Selection, e -> {
                    this.tableField.deleteRow(selectionIndex);
                });
            }
        }
    }

    private final class TableCellListener extends MouseAdapter {

        private static final long serialVersionUID = -400266849205933608L;
        private final TableField tableField;

        public TableCellListener(final TableField tableField) {
            this.tableField = tableField;
        }

        @Override
        public void mouseDoubleClick(final MouseEvent event) {

            final int selectedIndex = this.tableField.control.getSelectionIndex();
            if (selectedIndex < 0) {
                return;
            }

            final TableItem item = this.tableField.control.getItem(selectedIndex);
            final int listIndex = (Integer) item.getData(LIST_INDEX_REF);

            for (int i = 0; i < this.tableField.editor.length; i++) {
                final GUIViewAttribute guiViewAttribute = this.tableField.columnAttributes.get(i);
                final TableCellEditorBuilder tableCellEditorBuilder = TableBuilder.this.cellEditorBuilderMap.get(
                        guiViewAttribute.getFieldType());
                final Control cellEditor = tableCellEditorBuilder.buildEditor(this.tableField, item, i, listIndex);
                this.tableField.editor[i].setEditor(cellEditor, item, i);
            }
        }
    }

}
