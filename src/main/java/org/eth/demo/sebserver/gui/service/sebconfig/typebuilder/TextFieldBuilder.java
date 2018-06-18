/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig.typebuilder;

import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUIViewAttribute;
import org.eth.demo.sebserver.gui.service.sebconfig.InputComponentBuilder;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField.FieldType;
import org.eth.demo.sebserver.gui.service.sebconfig.ValueChangeListener;
import org.eth.demo.sebserver.gui.service.sebconfig.ViewContext;
import org.springframework.stereotype.Component;

@Component
public class TextFieldBuilder implements InputComponentBuilder {

    @Override
    public FieldType[] supportedTypes() {
        return new FieldType[] {
                FieldType.TEXT_FIELD,
                FieldType.TEXT_AREA,
                FieldType.INTEGER,
                FieldType.DECIMAL };
    };

    @Override
    public InputField createInputComponent(
            final Composite parent,
            final GUIViewAttribute attribute,
            final ViewContext viewContext) {

        final FieldType fieldType = attribute.getFieldType();
        final Text text;
        if (fieldType == FieldType.INTEGER || fieldType == FieldType.DECIMAL) {
            text = new Text(parent, SWT.RIGHT | SWT.BORDER);
        } else {
            text = new Text(parent, SWT.LEFT | SWT.BORDER);
        }

        addValueChangeListener(text, attribute, viewContext.getValueChangeListener());

        return new TextInputField(attribute, text);
    }

    public void addValueChangeListener(
            final Text control,
            final GUIViewAttribute attribute,
            final ValueChangeListener valueListener) {

        final FieldType fieldType = attribute.getFieldType();
        if (fieldType == FieldType.INTEGER) {
            addNumberCheckListener(control, attribute, s -> Integer.parseInt(s), valueListener);
        } else if (fieldType == FieldType.DECIMAL) {
            addNumberCheckListener(control, attribute, s -> Double.parseDouble(s), valueListener);
        } else {
            control.addListener(
                    SWT.FocusOut,
                    event -> valueListener.valueChanged(
                            attribute,
                            String.valueOf(control.getText()),
                            0));
        }
    }

    private void addNumberCheckListener(
            final Text control,
            final GUIViewAttribute attribute,
            final Consumer<String> numberCheck,
            final ValueChangeListener valueListener) {

        control.addListener(SWT.FocusOut, event -> {
            try {
                final String text = control.getText();
                numberCheck.accept(text);
                control.setBackground(null);
                valueListener.valueChanged(attribute, text, 0);
            } catch (final NumberFormatException e) {
                control.setBackground(new Color(control.getDisplay(), 255, 0, 0, 50));
            }
        });
    }

    static final class TextInputField extends ControlFieldAdapter<Text> {

        TextInputField(final GUIViewAttribute attribute, final Text control) {
            super(attribute, control);
        }

        @Override
        public InputValue getValue() {
            return InputField.createSingleValue(this.control.getText());
        }

        @Override
        public void setValue(final InputValue value) {
            if (!value.isSingle()) {
                throw new IllegalArgumentException("TextInputField only supports single InputValues");
            }

            this.control.setText(value.asString());
        }
    }
}
