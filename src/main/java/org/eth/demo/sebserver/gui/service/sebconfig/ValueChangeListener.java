/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig;

import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigTableValue;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigViewAttribute;

public interface ValueChangeListener {

    void valueChanged(String configId, ConfigViewAttribute attribute, String value, int listIndex);

    void tableChanged(ConfigTableValue tableValue);

}
