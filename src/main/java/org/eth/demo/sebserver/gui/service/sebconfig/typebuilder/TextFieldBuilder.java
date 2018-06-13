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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUIViewAttribute;
import org.eth.demo.sebserver.gui.service.sebconfig.InputComponentBuilder;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField.FieldType;
import org.springframework.stereotype.Component;

@Component
public class TextFieldBuilder implements InputComponentBuilder {

    @Override
    public FieldType[] supportedTypes() {
        return new FieldType[] {
                FieldType.TEXT_FIELD,
                FieldType.INTEGER,
                FieldType.DECIMAL };
    };

    @Override
    public InputField createInputComponent(final Composite parent, final GUIViewAttribute attribute) {
        final FieldType fieldType = attribute.getFieldType();
        final Text text;
        if (fieldType == FieldType.INTEGER || fieldType == FieldType.DECIMAL) {
            text = new Text(parent, SWT.RIGHT | SWT.BORDER);
        } else {
            text = new Text(parent, SWT.LEFT | SWT.BORDER);
        }

        return new TextInputField(attribute, text);
    }

    public static final class TextInputField extends ControlFieldAdapter<Text> {

        TextInputField(final GUIViewAttribute attribute, final Text control) {
            super(attribute, control);
        }

        @Override
        public String getValue() {
            return this.control.getText();
        }

        @Override
        public void setValue(final String value) {
            this.control.setText(value);
        }

        @Override
        public void setValueListener(final BiConsumer<String, GUIViewAttribute> valueListener) {
            final FieldType fieldType = this.attribute.getFieldType();
            if (fieldType == FieldType.INTEGER) {
                this.control.addListener(SWT.Verify, event -> {
                    try {
                        Integer.parseInt(event.text);
                        this.control.setBackground(null);
                        valueListener.accept(event.text, this.attribute);
                    } catch (final NumberFormatException e) {
                        this.control.setBackground(new Color(this.control.getDisplay(), 100, 0, 0));
                    }
                });
            } else if (fieldType == FieldType.DECIMAL) {
                this.control.addListener(SWT.Verify, event -> {
                    try {
                        Double.parseDouble(event.text);
                        this.control.setBackground(null);
                        valueListener.accept(event.text, this.attribute);
                    } catch (final NumberFormatException e) {
                        this.control.setBackground(new Color(this.control.getDisplay(), 100, 0, 0));
                    }
                });
            } else {
                this.control.addListener(
                        SWT.Verify,
                        event -> valueListener.accept(
                                String.valueOf(event.text),
                                this.attribute));
            }
        }
    };
}
