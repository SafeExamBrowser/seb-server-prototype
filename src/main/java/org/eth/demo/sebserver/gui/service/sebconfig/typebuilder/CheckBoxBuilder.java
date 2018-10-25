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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigAttributeValue;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigViewAttribute;
import org.eth.demo.sebserver.gui.service.sebconfig.InputComponentBuilder;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField.FieldType;
import org.eth.demo.sebserver.gui.service.sebconfig.ViewContext;
import org.springframework.stereotype.Component;

@Component
public class CheckBoxBuilder implements InputComponentBuilder {

    @Override
    public FieldType[] supportedTypes() {
        return new FieldType[] {
                FieldType.CHECKBOX,
                FieldType.CHECK_FIELD };
    };

    @Override
    public InputField createInputComponent(
            final Composite parent,
            final ConfigViewAttribute attribute,
            final ViewContext viewContext) {

        final FieldType fieldType = attribute.getFieldType();
        final Button checkbox = new Button(parent, SWT.CHECK);
        if (fieldType == FieldType.CHECKBOX) {
            checkbox.setText(attribute.name);
        }

        checkbox.addListener(
                SWT.Selection,
                event -> viewContext.getValueChangeListener().valueChanged(
                        viewContext,
                        attribute,
                        String.valueOf(checkbox.getSelection()),
                        0));

        return new CheckboxField(attribute, checkbox);
    }

    static final class CheckboxField extends ControlFieldAdapter<Button> {

        private boolean initValue = false;

        CheckboxField(final ConfigViewAttribute attribute, final Button control) {
            super(attribute, control);
        }

        @Override
        public void initValue(final Collection<ConfigAttributeValue> values) {
            values.stream()
                    .filter(a -> this.attribute.name.equals(a.attributeName))
                    .findFirst()
                    .map(v -> {
                        this.initValue = Boolean.valueOf(v.value);
                        this.control.setSelection(this.initValue);
                        return this.initValue;
                    });
        }

        @Override
        protected void setDefaultValue() {
            this.control.setSelection(this.initValue);
        }
    }
}
