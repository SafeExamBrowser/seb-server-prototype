/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig.typebuilder;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TableItem;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUIViewAttribute;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField.FieldType;
import org.eth.demo.sebserver.gui.service.sebconfig.typebuilder.TableBuilder.TableField;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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

        final GUIViewAttribute columnAttribute = tableField.columnAttributes.get(columnIndex);
        final Combo cellEditor = new Combo(tableField.control, SWT.READ_ONLY);
        cellEditor.setItems(StringUtils.split(columnAttribute.resources, ","));
        cellEditor.addListener(
                SWT.Selection, event -> {
                    final int selectionIndex = cellEditor.getSelectionIndex();
                    final String selected = cellEditor.getItem(selectionIndex);
                    tableField.editor[columnIndex].getItem().setText(columnIndex, selected);
                    tableField.valueChanged(columnIndex, rowIndex, String.valueOf(selectionIndex));
                });

        return cellEditor;
    }

    @Override
    public String getValue(final GUIViewAttribute attribute, final String displayValue) {
        // TODO
        final List<String> selectionResources =
                CollectionUtils.arrayToList(StringUtils.split(attribute.resources, ","));
        return displayValue;
    }

}
