/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig.typebuilder;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TableItem;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUIViewAttribute;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField.FieldType;
import org.eth.demo.sebserver.gui.service.sebconfig.typebuilder.TableBuilder.TableField;

public interface TableCellEditorBuilder {

    FieldType getType();

    Control buildEditor(TableField tableField, TableItem item, int columnIndex, final int rowIndex);

    String getValue(GUIViewAttribute attribute, String displayValue);

}
