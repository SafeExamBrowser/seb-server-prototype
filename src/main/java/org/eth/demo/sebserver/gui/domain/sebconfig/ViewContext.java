/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.domain.sebconfig;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormData;

public class ViewContext {

    public final String name;

    public final int xpos, ypos, width, height;
    public final int columns, rows;

    private final Cell protoCell;

    public ViewContext(final String name,
            final int xpos,
            final int ypos,
            final int width,
            final int height,
            final int columns,
            final int rows) {

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

    @Override
    public String toString() {
        return "ViewContext [name=" + this.name + ", xpos=" + this.xpos + ", ypos=" + this.ypos + ", width="
                + this.width + ", height="
                + this.height + ", columns=" + this.columns + ", rows=" + this.rows + ", protoCell=" + this.protoCell
                + "]";
    }

}
