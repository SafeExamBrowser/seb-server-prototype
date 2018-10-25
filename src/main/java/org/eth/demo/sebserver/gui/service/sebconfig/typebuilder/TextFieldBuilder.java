/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig.typebuilder;

import java.util.Collection;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigAttributeValue;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigViewAttribute;
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
            final ConfigViewAttribute attribute,
            final ViewContext viewContext) {

        final FieldType fieldType = attribute.getFieldType();
        final Text text;
        if (fieldType == FieldType.INTEGER || fieldType == FieldType.DECIMAL) {
            text = new Text(parent, SWT.RIGHT | SWT.BORDER);
        } else {
            text = new Text(parent, SWT.LEFT | SWT.BORDER);
        }

        addValueChangeListener(text, attribute, viewContext);

        return new TextInputField(attribute, text);
    }

    private void addValueChangeListener(
            final Text control,
            final ConfigViewAttribute attribute,
            final ViewContext viewContext) {

        final ValueChangeListener valueListener = viewContext.getValueChangeListener();
        final FieldType fieldType = attribute.getFieldType();
        if (fieldType == FieldType.INTEGER) {
            addNumberCheckListener(control, attribute, s -> Integer.parseInt(s), viewContext);
        } else if (fieldType == FieldType.DECIMAL) {
            addNumberCheckListener(control, attribute, s -> Double.parseDouble(s), viewContext);
        } else {
            control.addListener(
                    SWT.FocusOut,
                    event -> valueListener.valueChanged(
                            viewContext,
                            attribute,
                            String.valueOf(control.getText()),
                            0));
        }
    }

    private void addNumberCheckListener(
            final Text control,
            final ConfigViewAttribute attribute,
            final Consumer<String> numberCheck,
            final ViewContext viewContext) {

        final ValueChangeListener valueListener = viewContext.getValueChangeListener();
        control.addListener(SWT.FocusOut, event -> {
            try {
                final String text = control.getText();
                numberCheck.accept(text);
                control.setBackground(null);
                valueListener.valueChanged(viewContext, attribute, text, 0);
            } catch (final NumberFormatException e) {
                control.setBackground(new Color(control.getDisplay(), 255, 0, 0, 50));
            }
        });
    }

    static final class TextInputField extends ControlFieldAdapter<Text> {

        private String initValue = "";

        TextInputField(final ConfigViewAttribute attribute, final Text control) {
            super(attribute, control);
        }

        @Override
        public void initValue(final Collection<ConfigAttributeValue> values) {
            values.stream()
                    .filter(a -> this.attribute.name.equals(a.attributeName))
                    .findFirst()
                    .map(v -> {
                        this.initValue = v.value;
                        setDefaultValue();
                        return this.initValue;
                    });
        }

        @Override
        protected void setDefaultValue() {
            this.control.setText(this.initValue);
        }
    }
}
