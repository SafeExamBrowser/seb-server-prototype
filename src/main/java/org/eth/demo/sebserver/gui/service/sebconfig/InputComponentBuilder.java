/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig;

import org.eclipse.swt.widgets.Composite;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigViewAttribute;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField.FieldType;

public interface InputComponentBuilder {

    String RES_BUNDLE_KEY_PREFIX = "org.sebserver.configuration_attribute.inputresources.";

    FieldType[] supportedTypes();

    InputField createInputComponent(
            final Composite parent,
            final ConfigViewAttribute attribute,
            final ViewContext viewContext);

    static String createResourceBundleKey(final String paramName, final String value) {
        return RES_BUNDLE_KEY_PREFIX + paramName + "." + value;
    }

}
