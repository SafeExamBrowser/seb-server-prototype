/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.domain.sebconfig.attribute;

import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eth.demo.sebserver.gui.service.sebconfig.ViewContext;

public class ConfigViewGridCell {

    public static final int GROUP_CELL_HEIGHT_ADJUSTMENT = 2;

    public final int column, row, columnSpan, rowSpan;
    public final int relWidth, relHeight, pixelWidth, pixelHeight;

    public ConfigViewGridCell(
            final int column,
            final int row,
            final int relWidth,
            final int relHeight,
            final int pixelWidth,
            final int pixelHeight) {

        this(column, row, 1, 1, relWidth, relHeight, pixelWidth, pixelHeight);
    }

    public ConfigViewGridCell(
            final int column,
            final int row,
            final int columnSpan,
            final int rowSpan,
            final int relWidth,
            final int relHeight,
            final int pixelWidth,
            final int pixelHeight) {

        this.column = column;
        this.row = row;
        this.columnSpan = columnSpan;
        this.rowSpan = rowSpan;
        this.relWidth = relWidth;
        this.relHeight = relHeight;
        this.pixelWidth = pixelWidth;
        this.pixelHeight = pixelHeight;
    }

    public ConfigViewGridCell span(final int columnSpan, final int rowSpan) {
        return new ConfigViewGridCell(
                this.column,
                this.row,
                columnSpan,
                rowSpan,
                this.relWidth,
                this.relHeight,
                this.pixelWidth,
                this.pixelHeight);
    }

    @Override
    public String toString() {
        return "ConfigViewGridCell [column=" + this.column + ", row=" + this.row + ", columnSpan=" + this.columnSpan
                + ", rowSpan="
                + this.rowSpan
                + ", relWidth=" + this.relWidth + ", relHeight=" + this.relHeight + ", pixelWidth=" + this.pixelWidth
                + ", pixelHeight=" + this.pixelHeight + "]";
    }

    public ConfigViewGridCell copyOf(final int column, final int row) {
        return new ConfigViewGridCell(
                column,
                row,
                this.relWidth,
                this.relHeight,
                this.pixelWidth,
                this.pixelHeight);
    }

    public static FormData createFormData(final int column, final int row, final ViewContext view) {
        return createFormData(view.getCell(column, row));
    }

    public static FormData createFormData(final ConfigViewGridCell configViewGridCell) {

        final FormData result = new FormData();
        result.left = new FormAttachment(configViewGridCell.column * configViewGridCell.relWidth);
        result.top = new FormAttachment(configViewGridCell.row * configViewGridCell.relHeight);
        result.right = new FormAttachment(
                (configViewGridCell.column + configViewGridCell.columnSpan) * configViewGridCell.relWidth);
        result.bottom = new FormAttachment(
                (configViewGridCell.row + configViewGridCell.rowSpan) * configViewGridCell.relHeight, -5);

        return result;
    }

}
