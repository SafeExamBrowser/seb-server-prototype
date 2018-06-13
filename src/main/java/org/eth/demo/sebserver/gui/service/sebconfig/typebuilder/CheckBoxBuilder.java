/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig.typebuilder;

import java.util.function.BiConsumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUIViewAttribute;
import org.eth.demo.sebserver.gui.service.sebconfig.InputComponentBuilder;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField.FieldType;
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
    public InputField createInputComponent(final Composite parent, final GUIViewAttribute attribute) {
        final FieldType fieldType = attribute.getFieldType();
        final Button checkbox = new Button(parent, SWT.CHECK);
        if (fieldType == FieldType.CHECKBOX) {
            checkbox.setText(attribute.name);
        }

        // TODO send value to WebService on selection changed
        //checkbox.addSelectionListener(listener);

        return new CheckboxField(attribute, checkbox);
    }

    public static final class CheckboxField extends ControlFieldAdapter<Button> {

        CheckboxField(final GUIViewAttribute attribute, final Button control) {
            super(attribute, control);
        }

        @Override
        public String getValue() {
            return String.valueOf(this.control.getSelection());
        }

        @Override
        public void setValue(final String value) {
            this.control.setSelection(Boolean.valueOf(value));
        }

        @Override
        public void setValueListener(final BiConsumer<String, GUIViewAttribute> valueListener) {
            this.control.addListener(SWT.Selection, event -> valueListener.accept(
                    String.valueOf(this.control.getSelection()),
                    this.attribute));
        }
    }

}
