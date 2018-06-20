/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig.typebuilder;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TableItem;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUIViewAttribute;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField.FieldType;
import org.eth.demo.sebserver.gui.service.sebconfig.typebuilder.TableBuilder.TableField;
import org.springframework.stereotype.Component;

@Component
public class TableCellComboEditorBuilder implements TableCellEditorBuilder {

    @Override
    public FieldType getType() {
        return FieldType.SINGLE_SELECTION;
    }

    @Override
    public Control buildEditor(
            final TableField tableField,
            final TableItem item,
            final int columnIndex,
            final int rowIndex) {

        final String rawDataKey = TableBuilder.ROW_RAW_VALUE_PREFIX + columnIndex;
        final GUIViewAttribute columnAttribute = tableField.columnAttributes.get(columnIndex);
        final Combo cellEditor = new Combo(tableField.control, SWT.READ_ONLY);
        cellEditor.setItems(StringUtils.split(columnAttribute.resources, ","));
        final String rawData = (String) item.getData(rawDataKey);
        if (rawData != null) {
            cellEditor.select(Integer.parseInt(rawData));
        }

        cellEditor.addListener(
                SWT.Selection, event -> {
                    final int selectionIndex = cellEditor.getSelectionIndex();
                    final String selected = cellEditor.getItem(selectionIndex);
                    final TableItem rowItem = tableField.editor[columnIndex].getItem();
                    rowItem.setText(columnIndex, selected);
                    rowItem.setData(rawDataKey, String.valueOf(selectionIndex));
                    tableField.valueChanged(columnIndex, rowIndex, String.valueOf(selectionIndex));
                });

        return cellEditor;
    }

    @Override
    public String populateCell(
            final GUIViewAttribute attr,
            final String value,
            final TableItem item,
            final int columnIndex) {

        final String val = (value == null) ? attr.getDefaultValue() : value;
        final int selectionIndex = (StringUtils.isNotBlank(val)) ? Integer.parseInt(val) : -1;
        if (selectionIndex >= 0) {
            item.setData(TableBuilder.ROW_RAW_VALUE_PREFIX + columnIndex, String.valueOf(selectionIndex));
            item.setText(columnIndex, StringUtils.split(attr.getResources(), ",")[selectionIndex]);
            return val;
        } else {
            return null;
        }
    }

}
