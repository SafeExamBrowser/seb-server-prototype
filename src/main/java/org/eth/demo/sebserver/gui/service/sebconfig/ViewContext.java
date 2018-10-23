/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.swt.layout.FormData;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigAttributeValue;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigViewAttribute;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigViewGridCell;

public final class ViewContext {

    public final String name;
    public final String configurationId;

    public final int columns, rows;
    private final ConfigViewGridCell protoCell;

    private final Map<String, ConfigViewAttribute> attributes;
    private final Map<String, InputField> inputFields;

    private final ValueChangeListener valueChangeListener;

    ViewContext(
            final String name,
            final String configurationId,
            final int columns,
            final int rows,
            final Map<String, ConfigViewAttribute> attributes,
            final ValueChangeListener valueChangeListener) {

        this.name = name;
        this.configurationId = configurationId;
        this.columns = columns;
        this.rows = rows;

        // TODO use GridLayout instead of FormLayout
        this.protoCell = new ConfigViewGridCell(
                0, 0,
                100 / columns,
                100 / rows,
                800 / columns,
                500 / rows);

        this.attributes = attributes;
        this.inputFields = new HashMap<>();
        this.valueChangeListener = valueChangeListener;
    }

    public String getName() {
        return this.name;
    }

    public String getConfigurationId() {
        return this.configurationId;
    }

    public int getColumns() {
        return this.columns;
    }

    public int getRows() {
        return this.rows;
    }

    public int getCellRelativeWidth() {
        return this.protoCell.relWidth;
    }

    public int getCellRelativeHeight() {
        return this.protoCell.relHeight;
    }

    public int getCellPixelWidth() {
        return this.protoCell.pixelWidth;
    }

    public int getCellPixelHeight() {
        return this.protoCell.pixelHeight;
    }

    public ConfigViewGridCell getCell(final int column, final int row) {
        return this.protoCell.copyOf(column, row);
    }

    public FormData getFormData(final int column, final int row) {
        return ConfigViewGridCell.createFormData(getCell(column, row));
    }

    public List<ConfigViewAttribute> getAttributes() {
        return new ArrayList<>(this.attributes.values());
    }

    public List<String> getAttributeNames() {
        return new ArrayList<>(this.attributes.keySet());
    }

    public List<ConfigViewAttribute> getChildAttributes(final ConfigViewAttribute attribute) {
        return this.attributes.values().stream()
                .filter(a -> attribute.name.equals(a.parentAttributeName))
                .sorted((a1, a2) -> Integer.valueOf(a1.xpos).compareTo(a2.xpos))
                .collect(Collectors.toList());
    }

    public ValueChangeListener getValueChangeListener() {
        return this.valueChangeListener;
    }

    void registerInputField(final InputField inputField) {
        this.inputFields.put(inputField.getName(), inputField);
    }

    void setValuesToInputFields(final Collection<ConfigAttributeValue> values) {
        this.inputFields.values().stream()
                .forEach(field -> field.initValue(values));
    }

    @Override
    public String toString() {
        return "ViewContext [name=" + this.name + ", configurationId=" + this.configurationId + ", columns="
                + this.columns + ", rows="
                + this.rows + ", protoCell=" + this.protoCell + ", attributes=" + this.attributes + ", inputFields="
                + this.inputFields
                + ", valueChangeListener=" + this.valueChangeListener + "]";
    }

}
