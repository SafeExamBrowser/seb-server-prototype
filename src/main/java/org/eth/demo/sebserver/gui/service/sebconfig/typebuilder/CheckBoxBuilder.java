/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig.typebuilder;

import java.util.Collection;
import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUIAttributeValue;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUIViewAttribute;
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
            final GUIViewAttribute attribute,
            final ViewContext viewContext) {

        final FieldType fieldType = attribute.getFieldType();
        final Button checkbox = new Button(parent, SWT.CHECK);
        if (fieldType == FieldType.CHECKBOX) {
            checkbox.setText(attribute.name);
        }

        checkbox.addListener(
                SWT.Selection,
                event -> viewContext.getValueChangeListener().valueChanged(
                        attribute,
                        String.valueOf(checkbox.getSelection()),
                        0));

        return new CheckboxField(attribute, checkbox);
    }

    static final class CheckboxField extends ControlFieldAdapter<Button> {

        CheckboxField(final GUIViewAttribute attribute, final Button control) {
            super(attribute, control);
        }

        @Override
        public void initValue(final Collection<GUIAttributeValue> values) {
            final Optional<GUIAttributeValue> value = values.stream()
                    .filter(a -> this.attribute.name.equals(a.attributeName))
                    .findFirst();

            if (value.isPresent()) {
                this.control.setSelection(Boolean.valueOf(value.get().value));
            }
        }
    }

}
