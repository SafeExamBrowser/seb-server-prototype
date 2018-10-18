/*
 * Copyright (c) 2018 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.eth.demo.sebserver.gui.service.sebconfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigAttributeValue;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigViewAttribute;
import org.eth.demo.sebserver.gui.domain.sebconfig.attribute.ConfigViewGridCell;
import org.eth.demo.sebserver.gui.service.rest.auth.AuthorizationContextHolder;
import org.eth.demo.sebserver.gui.service.rest.sebconfig.GETConfigAttribute;
import org.eth.demo.sebserver.gui.service.rest.sebconfig.GETConfigAttributeValues;
import org.eth.demo.sebserver.gui.service.rest.sebconfig.POSTConfigValue;
import org.eth.demo.sebserver.gui.service.sebconfig.InputField.FieldType;
import org.eth.demo.sebserver.service.JSONMapper;
import org.springframework.stereotype.Service;

@Service
public class ConfigViewService {

    private final GETConfigAttribute configAttributeRequest;
    private final GETConfigAttributeValues configAttributeValuesRequest;
    private final POSTConfigValue saveConfigAttributeValue;
    private final Map<FieldType, InputComponentBuilder> builderTypeMapping;
    private final AuthorizationContextHolder authorizationContextHolder;
    private final JSONMapper jsonMapper;

    public ConfigViewService(
            final GETConfigAttribute configAttributeRequest,
            final GETConfigAttributeValues configAttributeValuesRequest,
            final POSTConfigValue saveConfigAttributeValue,
            final Collection<InputComponentBuilder> builders,
            final AuthorizationContextHolder authorizationContextHolder,
            final JSONMapper jsonMapper) {

        this.configAttributeRequest = configAttributeRequest;
        this.configAttributeValuesRequest = configAttributeValuesRequest;
        this.saveConfigAttributeValue = saveConfigAttributeValue;
        this.builderTypeMapping = new EnumMap<>(FieldType.class);
        this.jsonMapper = jsonMapper;
        for (final InputComponentBuilder builder : builders) {
            for (final FieldType type : builder.supportedTypes()) {
                this.builderTypeMapping.put(type, builder);
            }
        }
        this.authorizationContextHolder = authorizationContextHolder;
    }

    public ViewContext createViewContext(
            final String name,
            final String configurationId,
            final int xpos,
            final int ypos,
            final int width,
            final int height,
            final int columns,
            final int rows) {

        // request the configuration attributes for the view/page from the SEBServer Web-Service API
        final Map<String, ConfigViewAttribute> attributes = this.configAttributeRequest
                .with(this.authorizationContextHolder)
                .configViewName(name)
                .doAPICall()
                .onError(t -> {
                    throw new RuntimeException(t);
                }); // TODO error handling

        return new ViewContext(
                name,
                configurationId,
                xpos, ypos,
                width, height,
                columns, rows,
                attributes,
                new ViewValueChangeListener(
                        this.saveConfigAttributeValue,
                        this.authorizationContextHolder
                                .getAuthorizationContext()
                                .getRestTemplate(),
                        this.jsonMapper));
    }

    public ViewContext createComponents(final Composite parent, final ViewContext viewContext) {
        final Map<String, List<ConfigViewAttribute>> groups = new LinkedHashMap<>();
        for (final ConfigViewAttribute attribute : viewContext.getAttributes()) {

            // ignore nested attributes
            if (StringUtils.isNotBlank(attribute.parentAttributeName)) {
                continue;
            }

            // check and handle builder availability for specified type
            if (!this.builderTypeMapping.containsKey(attribute.getFieldType())) {
                final Label errorLabel = getErrorLabel(parent, "No Builder for type: " + attribute.type);
                errorLabel.setLayoutData(
                        ConfigViewGridCell.createFormData(viewContext.getCell(attribute.xpos, attribute.ypos)));
                continue;
            }

            // collect attributes that belongs to a group for later processing
            if (StringUtils.isNotBlank(attribute.group)) {
                groups.computeIfAbsent(
                        attribute.group,
                        a -> new ArrayList<>()).add(attribute);
                continue;
            }

            // TODO the span information should also come within the orientation form back-end
            ConfigViewGridCell configViewGridCell = viewContext.getCell(attribute.xpos, attribute.ypos);
            if (FieldType.TABLE == attribute.getFieldType()) {
                configViewGridCell = configViewGridCell.span(3, 6);
            }

            createSingleInputComponent(
                    parent,
                    attribute,
                    viewContext,
                    configViewGridCell);
        }

        if (!groups.isEmpty()) {
            for (final List<ConfigViewAttribute> group : groups.values()) {
                createInputComponentGroup(parent, group, viewContext);
            }
        }

        return viewContext;
    }

    public ViewContext initInputFieldValues(final ViewContext viewContext) {

        final String attributeNames = StringUtils.join(viewContext.getAttributeNames(), ",");
        final Collection<ConfigAttributeValue> attributeValues = this.configAttributeValuesRequest
                .with(this.authorizationContextHolder)
                .config(viewContext.configurationId)
                .configAttributeNames(attributeNames)
                .doAPICall()
                .onError(t -> {
                    throw new RuntimeException(t);
                }); // TODO error handling

        viewContext.setValuesToInputFields(attributeValues);
        return viewContext;
    }

    private void createInputComponentGroup(
            final Composite parent,
            final List<ConfigViewAttribute> groupAttrs,
            final ViewContext viewContext) {

        if (groupAttrs == null || groupAttrs.isEmpty()) {
            return;
        }

        final ConfigViewAttribute firstAttr = groupAttrs.get(0);
        final Rectangle groupBounds = groupAttrs.stream()
                .map(a -> new Rectangle(a.xpos, a.ypos, 1, 1))
                .reduce(new Rectangle(firstAttr.xpos, firstAttr.ypos, 0, 0), Rectangle::union);

        System.out.println("****************** group: " + firstAttr.group + " bounds: " + groupBounds);

        final Group group = new Group(parent, SWT.NONE);
        final FormLayout layout = new FormLayout();
        group.setLayout(layout);
        group.setText(firstAttr.group);
        group.setLayoutData(ConfigViewGridCell.createFormData(new ConfigViewGridCell(
                groupBounds.x,
                groupBounds.y,
                viewContext.getCellRelativeWidth() * groupBounds.width,
                viewContext.getCellRelativeHeight()
                        * (groupBounds.height + ConfigViewGridCell.GROUP_CELL_HEIGHT_ADJUSTMENT),
                viewContext.getCellPixelWidth() * groupBounds.width,
                viewContext.getCellPixelHeight()
                        * (groupBounds.height + ConfigViewGridCell.GROUP_CELL_HEIGHT_ADJUSTMENT))));

        final int cellWidth = 100 / groupBounds.width;
        final int cellHeight = 100 / groupBounds.height;

        for (final ConfigViewAttribute attr : groupAttrs) {
            createSingleInputComponent(
                    group,
                    attr,
                    viewContext,
                    new ConfigViewGridCell(
                            attr.xpos - groupBounds.x,
                            attr.ypos - groupBounds.y,
                            cellWidth,
                            cellHeight,
                            viewContext.getCellPixelWidth(),
                            viewContext.getCellPixelHeight()));
        }
    }

    private void createSingleInputComponent(
            final Composite parent,
            final ConfigViewAttribute attribute,
            final ViewContext viewContext,
            final ConfigViewGridCell configViewGridCell) {

        final InputComponentBuilder inputComponentBuilder = this.builderTypeMapping.get(attribute.getFieldType());
        final InputField inputField = inputComponentBuilder.createInputComponent(parent, attribute, viewContext);

        createTitleLabel(parent, inputField, configViewGridCell, attribute.name);
        inputField.getControl().setLayoutData(ConfigViewGridCell.createFormData(configViewGridCell));
        inputField.getControl().setToolTipText(attribute.name);
        viewContext.registerInputField(inputField);
    }

    private void createTitleLabel(
            final Composite parent,
            final InputField inputField,
            final ConfigViewGridCell configViewGridCell,
            final String title) {

        final FieldType fieldType = inputField.getType();
        final Label label = new Label(parent, SWT.NONE);
        label.setText(" " + title + "   ");
        switch (fieldType.titleOrientation) {
            case LEFT: {
                if (configViewGridCell.column > 0) {
                    label.setAlignment(SWT.RIGHT);
                    label.setLayoutData(ConfigViewGridCell.createFormData(
                            configViewGridCell.copyOf(configViewGridCell.column - 1, configViewGridCell.row)));
                }
                break;
            }
            case TOP: {
                if (configViewGridCell.row > 0) {
                    label.setAlignment(SWT.BOTTOM);
                    label.setLayoutData(ConfigViewGridCell.createFormData(
                            configViewGridCell.copyOf(configViewGridCell.column, configViewGridCell.row - 1)));
                }
                break;
            }
            case RIGHT: {
                label.setAlignment(SWT.LEFT);
                label.setLayoutData(ConfigViewGridCell.createFormData(
                        configViewGridCell.copyOf(configViewGridCell.column + 1, configViewGridCell.row)));
            }
            default: {
                label.dispose();
            }
        }
    }

    private Label getErrorLabel(final Composite parent, final String errorText) {
        final Label label = new Label(parent, SWT.NONE);
        label.setText(errorText);
        return label;
    }

}
