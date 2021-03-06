/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig.typebuilder;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigAttributeValue;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigViewAttribute;
import org.eth.demo.sebserver.gui.service.sebconfig.InputComponentBuilder;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField.FieldType;
import org.eth.demo.sebserver.gui.service.sebconfig.ViewContext;
import org.springframework.stereotype.Component;

@Component
public class LabelBuilder implements InputComponentBuilder {

    @Override
    public FieldType[] supportedTypes() {
        return new FieldType[] { FieldType.LABEL };
    };

    @Override
    public InputField createInputComponent(
            final Composite parent,
            final ConfigViewAttribute attribute,
            final ViewContext viewContext) {

        final Label label = new Label(parent, SWT.NONE);
        label.setText(attribute.name);

        return new LabelField(attribute, label);
    }

    static final class LabelField extends ControlFieldAdapter<Label> {

        LabelField(final ConfigViewAttribute attribute, final Label control) {
            super(attribute, control);
        }

        @Override
        public void initValue(final Collection<ConfigAttributeValue> values) {
            // Does Nothing
        }

        @Override
        protected void setDefaultValue() {
            // Does Nothing
        }
    };

}
