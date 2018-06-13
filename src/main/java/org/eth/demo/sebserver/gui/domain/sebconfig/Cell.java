/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.domain.sebconfig;

import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;

public class Cell {

    public static final int GROUP_CELL_HEIGHT_ADJUSTMENT = 2;

    public final int column, row, columnSpan, rowSpan;
    public final int relWidth, relHeight, pixelWidth, pixelHeight;

    public Cell(
            final int column,
            final int row,
            final int relWidth,
            final int relHeight,
            final int pixelWidth,
            final int pixelHeight) {

        this(column, row, 1, 1, relWidth, relHeight, pixelWidth, pixelHeight);
    }

    public Cell(
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

    @Override
    public String toString() {
        return "Cell [column=" + this.column + ", row=" + this.row + ", columnSpan=" + this.columnSpan + ", rowSpan="
                + this.rowSpan
                + ", relWidth=" + this.relWidth + ", relHeight=" + this.relHeight + ", pixelWidth=" + this.pixelWidth
                + ", pixelHeight=" + this.pixelHeight + "]";
    }

    public Cell copyOf(final int column, final int row) {
        return new Cell(
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

    public static FormData createFormData(final Cell cell) {

        final FormData result = new FormData();
        result.left = new FormAttachment(cell.column * cell.relWidth);
        result.top = new FormAttachment(cell.row * cell.relHeight);
        result.right = new FormAttachment((cell.column + cell.columnSpan) * cell.relWidth);
        result.bottom = new FormAttachment((cell.row + cell.rowSpan) * cell.relHeight, -5);

        return result;
    }

}
