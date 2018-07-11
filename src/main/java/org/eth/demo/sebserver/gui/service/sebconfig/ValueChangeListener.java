/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig;

import org.eth.demo.sebserver.gui.domain.sebconfig.GUITableValue;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUIViewAttribute;

public interface ValueChangeListener {

    void valueChanged(String configId, GUIViewAttribute attribute, String value, int listIndex);

    void tableChanged(GUITableValue tableValue);

}
