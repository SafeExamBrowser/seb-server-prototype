/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig.typebuilder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUIViewAttribute;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField.FieldType;
import org.eth.demo.sebserver.gui.service.sebconfig.typebuilder.TableBuilder.TableField;
import org.springframework.stereotype.Component;

@Component
public class TableCellTextEditorBuilder implements TableCellEditorBuilder {

    @Override
    public FieldType getType() {
        return FieldType.TEXT_FIELD;
    }

    @Override
    public Control buildEditor(
            final TableField tableField,
            final TableItem item,
            final int columnIndex,
            final int rowIndex) {

        final Text cellEditor = new Text(tableField.control, SWT.BORDER);
        cellEditor.setText(item.getText(columnIndex));
        cellEditor.addListener(SWT.FocusOut, event -> {
            final Text text = (Text) tableField.editor[columnIndex].getEditor();
            final String value = text.getText();
            tableField.editor[columnIndex].getItem().setText(columnIndex, value);
            tableField.valueChanged(columnIndex, rowIndex, value);
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
        item.setText(columnIndex, val);
        return val;
    }
}
