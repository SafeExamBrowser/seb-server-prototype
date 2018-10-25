/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig.typebuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TableItem;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigViewAttribute;
import org.eth.demo.sebserver.gui.service.i18n.I18nSupport;
import org.eth.demo.sebserver.gui.service.sebconfig.InputComponentBuilder;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField.FieldType;
import org.eth.demo.sebserver.gui.service.sebconfig.typebuilder.TableBuilder.TableField;
import org.eth.demo.sebserver.gui.service.widgets.SingleSelection;
import org.eth.demo.sebserver.gui.service.widgets.WidgetFactory;
import org.eth.demo.util.Tuple;
import org.springframework.stereotype.Component;

@Component
public class TableCellSingleSelectionEditorBuilder implements TableCellEditorBuilder {

    private final I18nSupport i18nSupport;
    private final WidgetFactory widgetFactory;

    public TableCellSingleSelectionEditorBuilder(final I18nSupport i18nSupport, final WidgetFactory widgetFactory) {
        this.i18nSupport = i18nSupport;
        this.widgetFactory = widgetFactory;
    }

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
        final ConfigViewAttribute columnAttribute = tableField.columnAttributes.get(columnIndex);
        final List<Tuple<String>> resources;
        if (columnAttribute.resources != null) {
            resources = Arrays.asList(StringUtils.split(columnAttribute.resources, ","))
                    .stream()
                    .map(str -> new Tuple<>(str, InputComponentBuilder.createResourceBundleKey(
                            columnAttribute.name,
                            str)))
                    .collect(Collectors.toList());
        } else {
            resources = Collections.emptyList();
        }
        final SingleSelection cellEditor = this.widgetFactory.singleSelectionLocalized(tableField.control, resources);
        final String rawData = (String) item.getData(rawDataKey);
        if (rawData != null) {
            cellEditor.select(rawData);
        }

        cellEditor.addListener(
                SWT.Selection, event -> {
                    final int selectionIndex = cellEditor.getSelectionIndex();
                    final String selectionValue = cellEditor.getSelectionValue();
                    final String selected = cellEditor.getItem(selectionIndex);
                    final TableItem rowItem = tableField.editor[columnIndex].getItem();
                    rowItem.setText(columnIndex, selected);
                    rowItem.setData(rawDataKey, selectionValue);
                    tableField.valueChanged(columnIndex, rowIndex, selectionValue);
                });

        return cellEditor;
    }

    @Override
    public String populateCell(
            final ConfigViewAttribute attr,
            final String value,
            final TableItem item,
            final int columnIndex) {

        final String val = (value == null) ? attr.getDefaultValue() : value;
        if (val != null) {
            final String resourceBundleKey = InputComponentBuilder.createResourceBundleKey(attr.name, val);
            item.setData(TableBuilder.ROW_RAW_VALUE_PREFIX + columnIndex, value);
            item.setText(columnIndex, this.i18nSupport.getText(resourceBundleKey, val));
            return val;
        } else {
            return null;
        }
    }

}
