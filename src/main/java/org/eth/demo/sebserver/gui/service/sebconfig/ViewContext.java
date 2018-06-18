/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormData;
import org.eth.demo.sebserver.gui.domain.sebconfig.Cell;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUIViewAttribute;

public final class ViewContext {

    public final String name;

    public final int xpos, ypos, width, height;
    public final int columns, rows;
    private final Cell protoCell;

    private final Map<String, GUIViewAttribute> attributes;
    private final Map<String, InputField> inputFields;

    private final ValueChangeListener valueChangeListener;

    ViewContext(final String name,
            final int xpos,
            final int ypos,
            final int width,
            final int height,
            final int columns,
            final int rows,
            final Map<String, GUIViewAttribute> attributes,
            final ValueChangeListener valueChangeListener) {

        this.name = name;
        this.xpos = xpos;
        this.ypos = ypos;
        this.width = width;
        this.height = height;
        this.columns = columns;
        this.rows = rows;

        this.protoCell = new Cell(
                0, 0,
                100 / columns,
                100 / rows,
                width / columns,
                height / rows);

        this.attributes = attributes;
        this.inputFields = new HashMap<>();
        this.valueChangeListener = valueChangeListener;
    }

    public String getName() {
        return this.name;
    }

    public int getXpos() {
        return this.xpos;
    }

    public int getYpos() {
        return this.ypos;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
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

    public Rectangle getViewBounds() {
        return new Rectangle(this.xpos, this.ypos, this.width, this.height);
    }

    public Cell getCell(final int column, final int row) {
        return this.protoCell.copyOf(column, row);
    }

    public FormData getFormData(final int column, final int row) {
        return Cell.createFormData(getCell(column, row));
    }

    public List<GUIViewAttribute> getAttributes() {
        return new ArrayList<>(this.attributes.values());
    }

    public List<GUIViewAttribute> getChildAttributes(final GUIViewAttribute attribute) {
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

    @Override
    public String toString() {
        return "ViewContext [name=" + this.name + ", xpos=" + this.xpos + ", ypos=" + this.ypos + ", width="
                + this.width + ", height="
                + this.height + ", columns=" + this.columns + ", rows=" + this.rows + ", protoCell=" + this.protoCell
                + "]";
    }

}
