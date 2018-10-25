/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig.typebuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigAttributeValue;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigViewAttribute;
import org.eth.demo.sebserver.gui.service.sebconfig.InputComponentBuilder;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField.FieldType;
import org.eth.demo.sebserver.gui.service.sebconfig.ViewContext;
import org.eth.demo.sebserver.gui.service.widgets.SingleSelection;
import org.eth.demo.sebserver.gui.service.widgets.WidgetFactory;
import org.eth.demo.util.Tuple;
import org.springframework.stereotype.Component;

@Component
public class SingleSelectionBuilder implements InputComponentBuilder {

    private final WidgetFactory widgetFactory;

    public SingleSelectionBuilder(final WidgetFactory widgetFactory) {
        this.widgetFactory = widgetFactory;
    }

    @Override
    public FieldType[] supportedTypes() {
        return new FieldType[] {
                FieldType.SINGLE_SELECTION,
                FieldType.MULTI_SELECTION };
    }

    @Override
    public InputField createInputComponent(
            final Composite parent,
            final ConfigViewAttribute attribute,
            final ViewContext viewContext) {

        final List<Tuple<String>> resources;
        if (attribute.resources != null) {
            resources = Arrays.asList(StringUtils.split(attribute.resources, ",")).stream()
                    .map(str -> new Tuple<>(str, InputComponentBuilder.createResourceBundleKey(attribute.name, str)))
                    .collect(Collectors.toList());
        } else {
            resources = Collections.emptyList();
        }
        final SingleSelection combo = this.widgetFactory.singleSelectionLocalized(parent, resources);
        combo.addListener(
                SWT.Selection,
                event -> viewContext.getValueChangeListener().valueChanged(
                        viewContext,
                        attribute,
                        String.valueOf(combo.getSelectionValue()),
                        0));

        return new SingleSelectionField(attribute, combo);
    }

    public static final class SingleSelectionField extends ControlFieldAdapter<SingleSelection> {

        private String initValue;

        SingleSelectionField(final ConfigViewAttribute attribute, final SingleSelection control) {
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
            this.control.select(this.initValue);
        }
    }

}
