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

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUIAttributeValue;
import org.eth.demo.sebserver.gui.domain.sebconfig.GUIViewAttribute;
import org.eth.demo.sebserver.gui.service.sebconfig.InputComponentBuilder;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField.FieldType;
import org.eth.demo.sebserver.gui.service.sebconfig.ViewContext;
import org.springframework.stereotype.Component;

@Component
public class ComboBuilder implements InputComponentBuilder {

    @Override
    public FieldType[] supportedTypes() {
        return new FieldType[] {
                FieldType.SINGLE_SELECTION,
                FieldType.MULTI_SELECTION };
    }

    @Override
    public InputField createInputComponent(
            final Composite parent,
            final GUIViewAttribute attribute,
            final ViewContext viewContext) {

        final Combo combo = new Combo(parent, SWT.READ_ONLY);
        combo.setItems(StringUtils.split(attribute.resources, ","));

        combo.addListener(
                SWT.Selection,
                event -> viewContext.getValueChangeListener().valueChanged(
                        viewContext.configurationId,
                        attribute,
                        String.valueOf(combo.getSelectionIndex()),
                        0));

        return new ComboField(attribute, combo);
    }

    public static final class ComboField extends ControlFieldAdapter<Combo> {

        ComboField(final GUIViewAttribute attribute, final Combo control) {
            super(attribute, control);
        }

        @Override
        public void initValue(final Collection<GUIAttributeValue> values) {
            final Optional<GUIAttributeValue> value = values.stream()
                    .filter(a -> this.attribute.name.equals(a.attributeName))
                    .findFirst();

            if (value.isPresent()) {
                this.control.select(Integer.parseInt(value.get().value));
            }
        }
    }

}
