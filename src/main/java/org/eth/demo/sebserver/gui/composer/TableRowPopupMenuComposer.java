/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.composer;

import org.eclipse.swt.widgets.Event;

public interface TableRowPopupMenuComposer {

    String TABLE_ROW_DATA = "TABLE_ROW_DATA";
    String ROOT_COMPOSITE_SUPPLIER = "ROOT_COMPOSITE_SUPPLIER";

    void onTableRowMenuEvent(final Event event);

}
